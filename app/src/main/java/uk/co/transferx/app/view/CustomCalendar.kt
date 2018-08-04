package uk.co.transferx.app.view

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayout
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.Gravity.CENTER
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.custom_calendar.view.*
import uk.co.transferx.app.R
import java.util.*
import kotlin.collections.ArrayList

class CustomCalendar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {


    private val days: ArrayList<Button?> = ArrayList(MAX_DAYS)
    private var data: Date
    private val calendar: Calendar
    private var daysInCurrentMonth: Int = 0
    private var dayOfWeekFirs: Int = 0
    private var dayOfweekLast: Int = 0

    init {
        inflate(context, R.layout.custom_calendar, this)
        data = Date()
        calendar = Calendar.getInstance()
        setUpCalendar()
        setUpHeader()
        initializeCalendar()
    }

    private fun initializeCalendar() {


        for (i: Int in 0 until MAX_DAYS) {
            val day = getButtonWithDay(i)
            calendarContainer.addView(day)
            // days[i] = day
            days.add(day)
        }
        Log.d("Serge", "container " + calendarContainer)
    }

    private fun getButton(): Button {
        val day = Button(context)
        day.setTextColor(ContextCompat.getColor(context, R.color.black))
        day.isEnabled = false
        day.gravity = CENTER
        day.layoutParams = getdaysLayoutParams()
        day.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
        day.maxLines = 1
        day.id = View.generateViewId()
        return day
    }

    private fun getButtonWithDay(position: Int): Button {
        val day = getButton()
        if (position < dayOfWeekFirs - 1) {
            day.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent))
            day.text = EMPTY
            return day
        }
        if (dayCount > daysInCurrentMonth - 1) {
            day.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent))
            day.text = EMPTY
            return day
        }
        day.background = ContextCompat.getDrawable(context, R.drawable.circle_calendar)
        day.text = (++dayCount).toString()
        return day
    }

    private fun setUpHeader() {
        findViewById<TextView>(R.id.monthLabel).text = String.format(
            Locale.getDefault(), "%s %d",
            getMonthName(data),
            calendar.get(Calendar.YEAR)
        )
    }

    private fun invalidateCalendar() {
        dayCount = 0
        for (i: Int in 0 until MAX_DAYS) {
            //  getButtonWithDay(days[i], i)
        }
    }

    private fun fillDays(){
        for (i: Int in 1 until 8){
            val dayOfW = getButton()
            val dayOfWeeName = calendar.getDisplayName( Calendar.DAY_OF_WEEK ,Calendar.LONG, Locale.getDefault())

        }
    }

    private fun setUpCalendar() {
        calendar.time = data
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1)
        daysInCurrentMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        dayOfWeekFirs = calendar.get(Calendar.DAY_OF_WEEK)
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), daysInCurrentMonth)
        dayOfweekLast = calendar.get(Calendar.DAY_OF_WEEK)
    }

    fun setDate(data: Date) {
        this.data = data
        setUpCalendar()
        setUpHeader()
        invalidateCalendar()
    }


    private fun getMonthName(date: Date): String {
        return android.text.format.DateFormat.format("MMM", date) as String
    }


    private fun getdaysLayoutParams(): GridLayout.LayoutParams {

        val buttonParams = GridLayout.LayoutParams()
        buttonParams.width = 0
        buttonParams.height = getHeightforButton()
        buttonParams.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
        buttonParams.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
        buttonParams.setMargins(15, 5, 15, 5)
        return buttonParams
    }

    private fun getHeightforButton(): Int {
        val metrics = context.resources.displayMetrics
        return ((metrics.widthPixels - 2 * resources.getDimensionPixelSize(R.dimen.left_right_main_screen) - 7 * 30) / 7)
    }

    companion object {
        const val MAX_DAYS: Int = 42
        var dayCount: Int = 0
        const val EMPTY = ""
    }
}