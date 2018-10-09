package kozyriatskyi.anton.sked.audiences.sheet

import android.annotation.TargetApi
import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetBehavior.STATE_COLLAPSED
import android.support.design.widget.BottomSheetBehavior.STATE_EXPANDED
import android.support.v7.app.AppCompatActivity
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.*
import kozyriatskyi.anton.sked.R
import kozyriatskyi.anton.sked.repository.Time
import kozyriatskyi.anton.sked.util.setDisabled
import kozyriatskyi.anton.sked.util.setEnabled
import kozyriatskyi.anton.sked.util.setInvisible
import kozyriatskyi.anton.sked.util.setVisible
import java.text.SimpleDateFormat
import java.util.*


class AudiencesTimeSelectionSheet : LinearLayout, OverlayView.OnTapListener, View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private lateinit var behavior: BlockingBottomSheetBehavior<AudiencesTimeSelectionSheet>
    private lateinit var overlayView: OverlayView

    private lateinit var timeStartSpinner: Spinner
    private lateinit var timeEndSpinner: Spinner
    private lateinit var dateText: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var chooseText: TextView
    private lateinit var upIcon: ImageView

    private val isExpanded: Boolean
        get() = behavior.state == BottomSheetBehavior.STATE_EXPANDED

    private var selectedDateCalendar = Calendar.getInstance()
    private val dateFormatter = SimpleDateFormat("dd.MM.yyyy")

    lateinit var onTimeSelectListener: OnTimeSelectListener

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        setWillNotDraw(true)
        LayoutInflater.from(context).inflate(R.layout.sheet_audiences_time_selection, this)
    }

    fun setupWithOverlayView(view: OverlayView) {
        overlayView = view
        overlayView.onTapListener = this
    }

    fun expandAndLock() {
        // using [post] so this code will be executed
        // after [onAttachToWindow]
        post {
            expandSheet()
            lock()
        }
    }

    fun collapseAndLock() {
        // using [post] so this code will be executed
        // after [onAttachToWindow]
        post {
            collapseSheet()
            lock()
        }
    }

    fun collapse() {
        // using [post] so this code will be executed
        // after [onAttachToWindow]
        post {
            collapseSheet()
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            upIcon.id -> expandSheet()
            R.id.audiences_sheet_header -> if (isExpanded) collapseSheet() else expandSheet()
            dateText.id -> showDatePickerDialog()
            chooseText.id -> {
                onTimeSelectListener.onTimeSelected(
                        date = dateText.text.toString(),
                        start = timeStartSpinner.selectedItem as Time,
                        end = timeEndSpinner.selectedItem as Time)
            }
        }
    }

    fun showLoading() {
        progressBar.setVisible()
        timeStartSpinner.setDisabled()
        timeEndSpinner.setDisabled()
        dateText.setDisabled()
        chooseText.setDisabled()
    }

    fun showTimes(start: List<Time>, end: List<Time>) {
        (timeStartSpinner.adapter as TimeSpinnerAdapter).setData(start)
        (timeEndSpinner.adapter as TimeSpinnerAdapter).setData(end)

        hideLoading()
    }

    private fun lock() {
        // using [post] so this code will be executed
        // after [onAttachToWindow]
        post {
            behavior.isLocked = true
        }
    }

    fun unlock() {
        // using [post] so this code will be executed
        // after [onAttachToWindow]
        post {
            behavior.isLocked = false
        }
    }

    private fun hideLoading() {
        progressBar.setInvisible()
        timeStartSpinner.setEnabled()
        timeEndSpinner.setEnabled()
        dateText.setEnabled()
        chooseText.setEnabled()
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
                .apply {
                    set(Calendar.YEAR, year)
                    set(Calendar.MONTH, month)
                    set(Calendar.DAY_OF_MONTH, dayOfMonth)
                }

        showDate(calendar)
    }

    override fun onTapped() {
        if (behavior.isLocked.not()) collapseSheet()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        timeStartSpinner = findViewById(R.id.audiences_time_start_spinner)
        timeEndSpinner = findViewById(R.id.audiences_time_end_spinner)
        dateText = findViewById(R.id.audiences_time_date_edittext)
        progressBar = findViewById(R.id.audiences_time_progress)
        chooseText = findViewById(R.id.audiences_time_save_text)
        upIcon = findViewById(R.id.audiences_time_up_image)

        timeStartSpinner.adapter = TimeSpinnerAdapter()
        timeEndSpinner.adapter = TimeSpinnerAdapter()

        chooseText.setOnClickListener(this)
        dateText.setOnClickListener(this)
        findViewById<View>(R.id.audiences_sheet_header).setOnClickListener(this)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        behavior = (BottomSheetBehavior.from(this) as BlockingBottomSheetBehavior).also {
            val peekHeight = context.resources.getDimensionPixelOffset(R.dimen.audiences_sheet_header_height)
            it.peekHeight = peekHeight
            it.isHideable = false
            it.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                if (slideOffset in 0f..1f) {
                    overlayView.alpha = slideOffset * 0.6f
                    upIcon.alpha = 1 - slideOffset
                    chooseText.alpha = slideOffset
                    upIcon.translationY = upIcon.height * (slideOffset)
                    chooseText.translationY = -chooseText.height * (1 - slideOffset)
                }
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {

            }
        })

        // show current date
        showDate(Calendar.getInstance())
    }

    // prevent touch event from reaching to the OverlayView
    override fun onTouchEvent(ev: MotionEvent): Boolean = true

    private fun collapseSheet() {
        if (behavior.isLocked.not() && behavior.state != BottomSheetBehavior.STATE_COLLAPSED) {
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    private fun expandSheet() {
        if (behavior.isLocked.not() && behavior.state != BottomSheetBehavior.STATE_EXPANDED) {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private fun showDatePickerDialog() {
        // shit
        val fragmentManager = (context as AppCompatActivity).supportFragmentManager
        DatePickerDialogFragment.show(fragmentManager, selectedDateCalendar)
    }

    private fun showDate(calendar: Calendar) {
        selectedDateCalendar = calendar
        dateText.text = dateFormatter.format(calendar.time)
    }

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()

        val state = SheetSavedState(superState)
        val behaviorState = behavior.state

        // state should be either [STATE_EXPANDED] or [STATE_COLLAPSED]
        // to prevent crash when restoring state
        state.sheetState = if (behaviorState == STATE_EXPANDED) STATE_EXPANDED else STATE_COLLAPSED

        selectedDateCalendar.also {
            state.year = it.get(Calendar.YEAR)
            state.month = it.get(Calendar.MONTH)
            state.day = it.get(Calendar.DAY_OF_MONTH)
        }

        return state
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state !is SheetSavedState) {
            super.onRestoreInstanceState(state)
            return
        }

        post {
            behavior.state = state.sheetState

            // fix bug when text overlaps the icon
            if (behavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
                chooseText.translationY = -chooseText.height.toFloat()
                chooseText.alpha = 0f
            }

            selectedDateCalendar.also {
                it.set(Calendar.YEAR, state.year)
                it.set(Calendar.MONTH, state.month)
                it.set(Calendar.DAY_OF_MONTH, state.day)
            }

            showDate(selectedDateCalendar)
        }

        super.onRestoreInstanceState(state.superState)
    }

    interface OnTimeSelectListener {
        fun onTimeSelected(date: String, start: Time, end: Time)
    }

    private class SheetSavedState : BaseSavedState {

        var sheetState: Int = BottomSheetBehavior.STATE_COLLAPSED

        var day: Int = 0
        var month: Int = 0
        var year: Int = 0

        constructor(superState: Parcelable) : super(superState)

        private constructor(parcel: Parcel) : super(parcel) {
            sheetState = parcel.readInt()
            day = parcel.readInt()
            month = parcel.readInt()
            year = parcel.readInt()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)

            out.writeInt(sheetState)
            out.writeInt(day)
            out.writeInt(month)
            out.writeInt(year)
        }

        override fun describeContents(): Int = 0

        @JvmField
        val CREATOR: Parcelable.Creator<SheetSavedState> = object : Parcelable.Creator<SheetSavedState> {
            override fun createFromParcel(parcel: Parcel): SheetSavedState = SheetSavedState(parcel)

            override fun newArray(size: Int): Array<SheetSavedState?> = arrayOfNulls(size)
        }
    }
}
