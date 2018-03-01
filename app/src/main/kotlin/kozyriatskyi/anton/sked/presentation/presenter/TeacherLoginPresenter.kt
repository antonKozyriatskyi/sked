package kozyriatskyi.anton.sked.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kozyriatskyi.anton.sked.data.pojo.Item
import kozyriatskyi.anton.sked.data.pojo.Teacher
import kozyriatskyi.anton.sked.domain.interactor.TeacherLoginInteractor
import kozyriatskyi.anton.sked.presentation.statemodel.TeacherLoginStateModel
import kozyriatskyi.anton.sked.presentation.view.TeacherLoginView
import kozyriatskyi.anton.sked.ui.fragment.TeacherLoginFragment
import kozyriatskyi.anton.sked.util.logD
import java.util.*

/**
 * Created by Anton on 07.07.2017.
 */

@InjectViewState
class TeacherLoginPresenter(private val interactor: TeacherLoginInteractor) : MvpPresenter<TeacherLoginView>() {

    private val disposables = CompositeDisposable()

    private val uiModel = TeacherLoginStateModel()

    private val teacher = Teacher()

    private var retryAction: (() -> Unit)? = { setLoadingState(); interactor.getDepartments() }

    override fun onFirstViewAttach() {
        initSubscriptions()
    }

    fun onDepartmentChosen(department: String, departmentId: String, position: Int) {
        uiModel.departmentPosition = position

        teacher.department = department
        teacher.departmentId = departmentId

        setLoadingState()
        retryAction = { interactor.getTeachers(departmentId) }
        interactor.getTeachers(departmentId)
    }

    fun onTeacherChosen(teacher: String, teacherId: String, position: Int) {
        uiModel.teacherPosition = position

        this.teacher.teacher = teacher
        this.teacher.teacherId = teacherId

        setLoadedState()
    }

    fun retry() {
        retryAction?.let {
            viewState.switchError(show = false)
            setLoadingState()
            it.invoke()
        }
    }

    fun onLoadButtonClick() {
        setLoadingState()
        retryAction = { interactor.loadSchedule(teacher) }
        interactor.loadSchedule(teacher)
    }

    private fun setLoadingState() {
        with(uiModel) {
            isLoading = true
            viewState.enableUi(enableUi)
            viewState.switchProgress(showProgress)
            viewState.switchError(show = false)
        }

        viewState.setLoaded(false)
    }

    private fun setLoadedState() {
        with(uiModel) {
            isLoaded = true
            viewState.enableUi(enableUi)
            viewState.switchProgress(showProgress)
        }

        viewState.setLoaded(true)
    }

    private fun setNotLoadedState() {
        with(uiModel) {
            isError = false
            isLoading = false
            isLoaded = false
            viewState.enableUi(enableUi)
            viewState.switchProgress(showProgress)
        }

        viewState.setLoaded(false)
    }

    private fun setErrorState() {
        with(uiModel) {
            isError = true
            viewState.enableUi(enableUi)
            viewState.switchProgress(showProgress)
            viewState.switchError(show = false)
        }

        viewState.setLoaded(false)
    }

    private fun subscribeSchedule() {
        disposables.add(interactor.scheduleLoadingStatus()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError {
                    logD("Error loading teacher schedule: ${it.message}")
                    if (uiModel.isConnectionAvailable) {
                        setErrorState()
                        viewState.switchError(TeacherLoginFragment.ERROR_SCHEDULE, "${it.message}", true)
                    }
                }
                .retry()
                .subscribe({
                    interactor.saveUser(teacher)
                    viewState.openScheduleScreen()
                }, {
//                    //TODO
//                    logD("Error loading teacher schedule: ${it.message}")
//                    if (uiModel.isConnectionAvailable) {
//                        setErrorState()
//                        viewState.switchError(TeacherLoginFragment.ERROR_SCHEDULE, "${it.message}", true)
//                    }
//                    subscribeSchedule()
                }))
    }

    private fun subscribeDepartments() {
        disposables.add(interactor.departments()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { retryAction = null }
                .subscribe({ departments: ArrayList<Item> ->
                    viewState.showDepartments(departments)
                    if (departments.isEmpty()) {
                        // since departments is an empty list I can use it to remove items from adapter
                        viewState.showTeachers(departments)
                        setNotLoadedState()
                    }
                }, {
                    if (uiModel.isConnectionAvailable) {
                        setErrorState()
                        logD("Error loading departments: ${it.message}")
                        viewState.switchError(TeacherLoginFragment.ERROR_DEPARTMENTS, "${it.message}", true)
                    }
                    subscribeDepartments()
                }))
    }

    private fun subscribeTeachers() {
        disposables.add(interactor.teachers()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { retryAction = null }
                .subscribe({ teachers: ArrayList<Item> ->
                    viewState.showTeachers(teachers)
                    if (teachers.isEmpty()) {
                        setNotLoadedState()
                    }
                }, {
                    if (uiModel.isConnectionAvailable) {
                        setErrorState()
                        logD("Error loading teachers: ${it.message}")
                        viewState.switchError(TeacherLoginFragment.ERROR_TEACHER, "${it.message}", true)
                    }
                    subscribeTeachers()
                }))
    }

    private fun subscribeConnectionState() {
        disposables.add(interactor.connectionStateChanges()
                .subscribe { onConnectionStateChanged(it) })
    }

    private fun initSubscriptions() {
        subscribeDepartments()
        subscribeTeachers()
        subscribeSchedule()
        subscribeConnectionState()
    }

    private fun onConnectionStateChanged(isConnectionAvailableNow: Boolean) {
        uiModel.isConnectionAvailable = isConnectionAvailableNow
        viewState.enableUi(uiModel.enableUi)
        viewState.switchProgress(uiModel.showProgress)
        if (isConnectionAvailableNow.not()) viewState.switchError(show = false)
        viewState.onConnectionChanged(isConnectionAvailableNow)

        if (isConnectionAvailableNow) retry()
    }

    override fun attachView(view: TeacherLoginView) {
        super.attachView(view)

        viewState.restorePositions(uiModel.departmentPosition, uiModel.teacherPosition)
    }

    override fun onDestroy() = disposables.dispose()
}