package kozyriatskyi.anton.sked.audiences

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kozyriatskyi.anton.sked.R
import kozyriatskyi.anton.sked.audiences.sheet.AudiencesTimeSelectionSheet
import kozyriatskyi.anton.sked.audiences.sheet.OverlayView
import kozyriatskyi.anton.sked.di.Injector
import kozyriatskyi.anton.sked.repository.Time
import kozyriatskyi.anton.sked.util.setGone
import kozyriatskyi.anton.sked.util.setVisible
import javax.inject.Inject

class AudiencesActivity : MvpAppCompatActivity(), AudiencesView, AudiencesTimeSelectionSheet.OnTimeSelectListener,
        View.OnClickListener, DatePickerDialog.OnDateSetListener {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, AudiencesActivity::class.java))
        }
    }

    @Inject
    @InjectPresenter
    lateinit var presenter: AudiencesPresenter

    private lateinit var adapter: AudiencesAdapter

    private lateinit var audiencesList: RecyclerView
    private lateinit var errorView: View
    private lateinit var timesErrorView: View
    private lateinit var loadingView: View

    private lateinit var timeSheet: AudiencesTimeSelectionSheet
    private lateinit var onDateSetListener: DatePickerDialog.OnDateSetListener

    @ProvidePresenter
    fun providePresenter(): AudiencesPresenter {
        Injector.inject(this)
        return presenter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_free_audiences)

        errorView = findViewById(R.id.audiences_error)
        timesErrorView = findViewById(R.id.audiences_times_error)
        loadingView = findViewById(R.id.audiences_loading)

        val retryLoadingAudiencesButton: Button = findViewById(R.id.audiences_error_retry_btn)
        retryLoadingAudiencesButton.setOnClickListener(this)
        val retryLoadingTimesButton: Button = findViewById(R.id.audiences_times_error_retry_btn)
        retryLoadingTimesButton.setOnClickListener(this)

        adapter = AudiencesAdapter()
        audiencesList = findViewById<RecyclerView>(R.id.audiences_list).also {
            val columnCount = resources.getInteger(R.integer.audiences_column_number)
            it.layoutManager = GridLayoutManager(this@AudiencesActivity, columnCount)
            it.adapter = adapter
        }

        val overlayView = findViewById<OverlayView>(R.id.overlay_view)
        timeSheet = findViewById<AudiencesTimeSelectionSheet>(R.id.audience_sheet).also {
            it.setupWithOverlayView(overlayView)
            it.onTimeSelectListener = this
            onDateSetListener = it
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.audiences_error_retry_btn -> presenter.onRetryLoadingAudiencesButtonClick()
            R.id.audiences_times_error_retry_btn -> presenter.onRetryLoadingTimesButtonClick()
        }
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
        timeSheet.onDateSet(view, year, month, dayOfMonth)
    }

    override fun onTimeSelected(date: String, start: Time, end: Time) {
        presenter.onLoadAudiencesButtonClick(
                date = date,
                timeStart = start,
                timeEnd = end)
    }

    override fun showAudiences(audiences: List<AudienceUi>) {
        adapter.setData(audiences)

        audiencesList.setVisible()
        errorView.setGone()
        timesErrorView.setGone()
        loadingView.setGone()

        timeSheet.unlock()
    }

    override fun showAudiencesLoading() {
        audiencesList.setGone()
        loadingView.setVisible()
        errorView.setGone()
        timesErrorView.setGone()

        timeSheet.unlock()
        timeSheet.collapse()
    }

    override fun showErrorLoadingAudiences() {
        audiencesList.setGone()
        errorView.setVisible()
        timesErrorView.setGone()
        loadingView.setGone()
    }

    override fun showTimes(start: List<Time>, end: List<Time>) {
        timeSheet.showTimes(start, end)
    }

    override fun showTimesLoading() {
        audiencesList.setGone()
        errorView.setGone()
        timesErrorView.setGone()
        loadingView.setGone()

        with(timeSheet) {
            showLoading()
            unlock()
            expandAndLock()
        }
    }

    override fun showErrorLoadingTimes() {
        audiencesList.setGone()
        errorView.setGone()
        timesErrorView.setVisible()
        loadingView.setGone()

        timeSheet.unlock()
        timeSheet.collapseAndLock()
    }

    override fun onDestroy() {
        super.onDestroy()

        if (isFinishing) Injector.release(this)
    }
}