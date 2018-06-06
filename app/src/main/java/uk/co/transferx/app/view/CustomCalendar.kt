package uk.co.transferx.app.view

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayout
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.custom_calendar.view.*
import uk.co.transferx.app.R
import java.util.*

class CustomCalendar @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    companion object {
        const val MAX_DAYS: Int = 42
        var dayCount: Int = 0
        const val EMPTY = ""
    }

    private var view: View? = null
    private val days: Array<Button?> = arrayOfNulls(MAX_DAYS)
    private var data: Date
    private val calendar: Calendar
    private var daysInCurrentMonth: Int = 0
    private var dayOfWeekFirs: Int = 0
    private var dayOfweekLast: Int = 0

    init {
        view = LayoutInflater.from(context).inflate(R.layout.custom_calendar, this, true)
        data = Date(System.currentTimeMillis())
        calendar = Calendar.getInstance()
        setUpCalendar()
        setUpHeader()
        initializeCalendar()
    }

    private fun initializeCalendar() {
        for (i: Int in 0 until MAX_DAYS) {
            val day = Button(context)
            day.setTextColor(ContextCompat.getColor(context, R.color.black))
            day.isEnabled = false
            day.layoutParams = getdaysLayoutParams()
            day.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
            day.maxLines = 1
            setDayNumber(day, i)
            calendarContainer.addView(day)
            days[i] = day
        }
    }

    private fun setUpHeader() {
        monthLabel.text = String.format(
                Locale.getDefault(), "%s %d",
                getMonthName(data),
                calendar.get(Calendar.YEAR))
    }

    private fun invalidateCalendar() {
        dayCount = 0
        for (i: Int in 0 until MAX_DAYS) {
            setDayNumber(days[i], i)
        }
    }

    private fun setDayNumber(day: Button?, position: Int) {
        if (position < dayOfWeekFirs - 1) {
            day?.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent))
            day?.text = EMPTY
            return
        }
        if (dayCount > daysInCurrentMonth - 1) {
            day?.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent))
            day?.text = EMPTY
            return
        }
        day?.background = ContextCompat.getDrawable(context, R.drawable.circle_calendar)
        day?.text = (++dayCount).toString()
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
        buttonParams.height = 0
        buttonParams.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
        buttonParams.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
        buttonParams.setMargins(15, 5, 15, 5)
        return buttonParams
    }
}