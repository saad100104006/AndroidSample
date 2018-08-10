package uk.co.transferx.app.view

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.GridLayout
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity.CENTER
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.custom_calendar.view.*
import uk.co.transferx.app.R
import uk.co.transferx.app.util.Constants.EMPTY
import java.text.DateFormatSymbols
import java.util.*
import kotlin.collections.ArrayList

class CustomCalendar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), View.OnClickListener {

    interface DataListener {
        fun onDataSetted(date: Date)
    }

    private val days: ArrayList<Button?> = ArrayList(MAX_DAYS)
    private val calendar: Calendar
    private var daysInCurrentMonth: Int = 0
    private var dayOfWeekFirs: Int = 0
    private var dayOfweekLast: Int = 0
    private var dataListener: DataListener? = null
    private var shouldShowHeader = true

    init {

        inflate(context, R.layout.custom_calendar, this)
        if (attrs != null) {
            val values = context.obtainStyledAttributes(attrs, R.styleable.CustomCalendar)
            shouldShowHeader = values.getBoolean(R.styleable.CustomCalendar_headerVisible, true)
            values.recycle()
        }
        calendar = Calendar.getInstance()
        currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        initializeCalendar()
        initListeners()
        invalidateCalendar()
    }

    private fun initListeners() {
        previousMonth.setOnClickListener { previousMonth() }
        nextMonth.setOnClickListener { nextMonth() }
    }

    private fun initializeCalendar() {
        val daysOfWeeks = DateFormatSymbols(Locale.getDefault()).weekdays
        for (i: Int in 0 until MAX_DAYS) {
            val day = getButton()
            if (i < 7) {
                day.text = daysOfWeeks[i+1].take(1)
                day.setTextColor(ContextCompat.getColor(context, R.color.hint))
            } else {
                days.add(day)
            }
            calendarContainer.addView(day)
        }
    }

    private fun getButton(): Button {
        val day = AppCompatButton(context)
        day.setTextColor(ContextCompat.getColor(context, R.color.black))
        day.isEnabled = false
        day.gravity = CENTER
        day.setPadding(PADDING, PADDING, PADDING, PADDING)
        day.layoutParams = getDaysLayoutParams()
        day.setTextAppearance(context, R.style.BlackFontSmall)
        day.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent))
        day.maxLines = 1
        day.setOnClickListener(this)
        day.id = generateViewId()
        return day
    }

    override fun onClick(v: View?) {
        resetColor()
        val day = v as AppCompatButton
        day.background = ContextCompat.getDrawable(context, R.drawable.circle_calendar_green)
        v.setTextColor(ContextCompat.getColor(context, R.color.white))
        calendar.set(Calendar.DAY_OF_MONTH, v.tag as Int)
        dataListener?.onDataSetted(calendar.time)
    }


    private fun resetColor() {
        for (day in days) {
            if (day?.isEnabled!!) {
                day.background = ContextCompat.getDrawable(context, R.drawable.circle_calendar)
                day.setTextColor(ContextCompat.getColor(context, R.color.black))
            }
        }
    }

    fun setDataListener(dataListener: (Date) -> Unit) {
        this.dataListener = object : DataListener {
            override fun onDataSetted(date: Date) {
                dataListener(date)
            }
        }
    }

    private fun nextMonth() {
        currentDay = -1
        resetColor()
        calendar.add(Calendar.MONTH, 1)
        invalidateCalendar()
        dataListener?.onDataSetted(Date())
    }

    private fun previousMonth() {
        if (isCurrentMonth()) {
            return
        }
        resetColor()
        calendar.add(Calendar.MONTH, -1)
        if (isCurrentMonth()) {
            currentDay = getCurrentDayOfMonth()
        }
        invalidateCalendar()
        dataListener?.onDataSetted(Date())
    }

    fun setDate(date: Date) {
        calendar.time = date
        currentDay = -1
        if (isCurrentMonth()) {
            currentDay = getCurrentDayOfMonth()
        }
        if (isSettedMonthPassed()) {
            currentDay = MAX_DAYS
        }
        resetColor()
        invalidateCalendar()
    }

    private fun isSettedMonthPassed(): Boolean {
        val currenDateCalendar = Calendar.getInstance()
        currenDateCalendar.time = Date()
        return currenDateCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
                currenDateCalendar.get(Calendar.MONTH) > calendar.get(Calendar.MONTH)
    }

    private fun getCurrentDayOfMonth(): Int {
        val cal = Calendar.getInstance()
        cal.time = Date()
        return cal.get(Calendar.DAY_OF_MONTH)
    }

    private fun isCurrentMonth(): Boolean {
        val currenDateCalendar = Calendar.getInstance()
        currenDateCalendar.time = Date()
        return currenDateCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
                currenDateCalendar.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)
    }

    private fun setUpMonth() {
        for (position: Int in 0 until days.size) {
            val day = days[position]
            day?.setTextColor(ContextCompat.getColor(context, R.color.black))
            day?.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent))
            day?.isEnabled = false
            day?.tag = null
            when {
                position < (dayOfWeekFirs - 1) -> {
                    day?.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent))
                    day?.text = EMPTY
                }
                dayCount > (daysInCurrentMonth - 1) -> {
                    day?.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent))
                    day?.text = EMPTY
                }
                else -> {
                    day?.text = (++dayCount).toString()
                    if (dayCount < currentDay) {
                        day?.background =
                                ContextCompat.getDrawable(context, R.drawable.circle_calendar)
                        day?.setTextColor(ContextCompat.getColor(context, R.color.not_active))
                    } else {
                        day?.background =
                                ContextCompat.getDrawable(context, R.drawable.circle_calendar)
                        day?.isEnabled = true
                        day?.tag = dayCount
                    }
                }
            }
        }
    }

    private fun setUpHeader() {
        if (!shouldShowHeader) {
            findViewById<TextView>(R.id.monthLabel).visibility = View.GONE
            findViewById<ImageView>(R.id.previousMonth).visibility = View.GONE
            findViewById<ImageView>(R.id.nextMonth).visibility = View.GONE
            return
        }

        findViewById<TextView>(R.id.monthLabel).text = String.format(
            Locale.getDefault(), "%s %d",
            getMonthName(calendar.time),
            calendar.get(Calendar.YEAR)
        )
    }

    private fun invalidateCalendar() {
        dayCount = 0
        setUpCalendar()
        setUpHeader()
        setUpMonth()
    }

    private fun setUpCalendar() {
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1)
        daysInCurrentMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        dayOfWeekFirs = calendar.get(Calendar.DAY_OF_WEEK)
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), daysInCurrentMonth)
        dayOfweekLast = calendar.get(Calendar.DAY_OF_WEEK)
    }

    private fun getMonthName(date: Date): String {
        return android.text.format.DateFormat.format("MMM", date) as String
    }

    private fun getDaysLayoutParams(): GridLayout.LayoutParams {
        val buttonParams = GridLayout.LayoutParams()
        buttonParams.width = 0
        buttonParams.height = getHeightForButton()
        buttonParams.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
        buttonParams.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
        buttonParams.setMargins(15, 5, 15, 5)
        return buttonParams
    }

    private fun getHeightForButton(): Int {
        val metrics = context.resources.displayMetrics
        return ((metrics.widthPixels - 2 * resources.getDimensionPixelSize(R.dimen.left_right_main_screen) - 7 * 30) / 7)
    }

    companion object {
        const val MAX_DAYS: Int = 49
        var dayCount: Int = 0
        const val PADDING = 5
        var currentDay: Int = 0
    }
}