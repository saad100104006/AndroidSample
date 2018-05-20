package uk.co.transferx.app.view

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayout
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.custom_calendar.view.*
import uk.co.transferx.app.R

class CustomCalendar @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    var view: View? = null

    init {
        view = LayoutInflater.from(context).inflate(R.layout.custom_calendar, this, true)
        initilizeCalendar()
    }


    private fun initilizeCalendar() {
        for (i: Int in 1..6 * 7) {
            val day = Button(context)
            day.setTextColor(ContextCompat.getColor(context, R.color.black))
            day.isEnabled = false
            day.layoutParams = getdaysLayoutParams()
            day.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
            day.maxLines = 1
            day.text = i.toString()
            day.background = ContextCompat.getDrawable(context, R.drawable.circle_calendar)
            calendarContainer.addView(day)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        Log.d("Serge", "with " + w + " height " + w + " oldWith " + oldw + " oldHeight " + oldh)
        Log.d("Serge", "viewWith " + view?.width + " viewHeight " + view?.height)
        val param :ViewGroup.LayoutParams? = view?.layoutParams
        val height = param?.height
        Log.d("Serge", "viewWith " + view?.width + " viewHeight " + view?.height)

    }


    private fun getdaysLayoutParams(): GridLayout.LayoutParams {
        val buttonParams = GridLayout.LayoutParams()
        buttonParams.width = 0
        buttonParams.height = 0
        buttonParams.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
        buttonParams.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
        buttonParams.setMargins(8,8,8,8)
        return buttonParams
    }
}