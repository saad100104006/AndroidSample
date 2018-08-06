package uk.co.transferx.app.mainscreen.schedule

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.GridLayout
import android.view.Gravity
import android.view.View
import kotlinx.android.synthetic.main.activity_time_layout.*
import uk.co.transferx.app.BaseActivity
import uk.co.transferx.app.R
import uk.co.transferx.app.TransferXApplication
import uk.co.transferx.app.mainscreen.schedule.presenter.TimePresenter

class TimeActivity :BaseActivity(), View.OnClickListener, TimePresenter.TimeUI{

    private val listHours: ArrayList<AppCompatButton?> = ArrayList(12)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as TransferXApplication).appComponent.inject(this)
        setContentView(R.layout.activity_time_layout)
        setUpHours()
    }

    private fun getButton(position: Int): AppCompatButton {
        val hour = AppCompatButton(this)
        hour.setTextColor(ContextCompat.getColor(this, R.color.black))
        hour.isEnabled = true
        hour.gravity = Gravity.CENTER
        hour.setPadding(5,5,5,5)
        hour.layoutParams = getDaysLayoutParams(position)
        hour.setTextAppearance(this, R.style.BlackFontSmall)
        hour.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent))
        hour.background = ContextCompat.getDrawable(this, R.drawable.circle_calendar)
        hour.setOnClickListener(this)
        hour.maxLines = 1
        hour.id = View.generateViewId()
        return hour
    }

    override fun onClick(v: View?) {
        resetColor()
        val hour = v as AppCompatButton
        hour.background = ContextCompat.getDrawable(this, R.drawable.circle_calendar_green)
        hour.setTextColor(ContextCompat.getColor(this, R.color.white))
    }

    private fun resetColor(){
        for (hour in listHours){
            hour?.background = ContextCompat.getDrawable(this, R.drawable.circle_calendar)
            hour?.setTextColor(ContextCompat.getColor(this, R.color.black))
        }
    }

    private fun setUpHours(){
        for (i: Int in 1 until 13) {
            val hour = getButton(i)
            hour.text = i.toString()
            hoursContainer.addView(hour)
            listHours.add(hour)
        }
    }

    private fun getDaysLayoutParams(position: Int): GridLayout.LayoutParams {
        val buttonParams = GridLayout.LayoutParams()
        buttonParams.width = 0
        buttonParams.height = getHeightForButton()
        buttonParams.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
        buttonParams.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
        when (position) {
            1 -> {
                buttonParams.setMargins(0, 10, 25, 10)
            }
            6 -> {
                buttonParams.setMargins(25, 10, 0, 10)
            }
            7 -> {
                buttonParams.setMargins(0, 10, 25, 10)
            }
            12 -> {
                buttonParams.setMargins(25, 10, 0, 10)
            }
            else -> {
                buttonParams.setMargins(25, 10, 25, 10)
            }
        }
        return buttonParams
    }

    private fun getHeightForButton(): Int {
        val metrics = resources.displayMetrics
        return ((metrics.widthPixels - 2 * resources.getDimensionPixelSize(R.dimen.left_right_main_screen) - 6 * 50) / 6)
    }

    override fun goToWelcome() {
        //no op
    }
}