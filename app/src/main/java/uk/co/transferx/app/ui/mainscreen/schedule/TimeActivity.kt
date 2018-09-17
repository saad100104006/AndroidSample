package uk.co.transferx.app.ui.mainscreen.schedule

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.GridLayout
import android.view.Gravity
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_time_layout.*
import uk.co.transferx.app.ui.base.BaseActivity
import uk.co.transferx.app.R
import uk.co.transferx.app.TransferXApplication
import uk.co.transferx.app.ui.mainscreen.schedule.ScheduleActivity.Companion.SETTLED_TIME
import uk.co.transferx.app.ui.mainscreen.schedule.presenter.TimePresenter
import javax.inject.Inject

class TimeActivity : BaseActivity(), View.OnClickListener, TimePresenter.TimeUI {

    private val listHours: ArrayList<AppCompatButton?> = ArrayList(12)
    private val listMinutes: ArrayList<Button> = ArrayList(4)

    @Inject
    lateinit var presenter: TimePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as TransferXApplication).appComponent.inject(this)
        setContentView(R.layout.activity_time_layout)
        buttonBackClock.setOnClickListener { onBackPressed() }
        listMinutes.add(zMinutes)
        listMinutes.add(fMinutes)
        listMinutes.add(tMinutes)
        listMinutes.add(ffMinutes)
        zMinutes.setOnClickListener { setMinutes(it as Button) }
        fMinutes.setOnClickListener { setMinutes(it as Button) }
        tMinutes.setOnClickListener { setMinutes(it as Button) }
        ffMinutes.setOnClickListener { setMinutes(it as Button) }
        buttonsRepeat.setOnCheckedChangeListener { _, checkedId -> setAmPm(checkedId) }
        saveTime.setOnClickListener { presenter.saveTime() }
        setUpHours()
    }

    private fun setAmPm(icheckedId: Int) {
        if (icheckedId == R.id.am) presenter.setAMPM(getString(R.string.am)) else presenter.setAMPM(
            getString(R.string.pm)
        )
    }

    override fun onResume() {
        presenter.attachUI(this)
        super.onResume()
    }

    override fun onPause() {
        presenter.detachUI()
        super.onPause()
    }

    private fun getButton(position: Int): AppCompatButton {
        val hour = AppCompatButton(this)
        hour.setTextColor(ContextCompat.getColor(this, R.color.black))
        hour.isEnabled = true
        hour.gravity = Gravity.CENTER
        hour.setPadding(5, 5, 5, 5)
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
        val button = v as Button
        resetColorHours()
        setMarked(button)
        presenter.setHour(button.text.toString())
    }

    private fun setMarked(button: Button) {
        button.background = ContextCompat.getDrawable(this, R.drawable.circle_calendar_green)
        button.setTextColor(ContextCompat.getColor(this, R.color.white))
    }

    private fun resetColorMinutes() {
        for (minutes in listMinutes) {
            minutes.background = ContextCompat.getDrawable(this, R.drawable.circle_calendar)
            minutes.setTextColor(ContextCompat.getColor(this, R.color.black))
        }
    }

    private fun setMinutes(button: Button) {
        resetColorMinutes()
        setMarked(button)
        presenter.setMinutes(button.text.toString())
    }

    private fun resetColorHours() {
        for (hour in listHours) {
            hour?.background = ContextCompat.getDrawable(this, R.drawable.circle_calendar)
            hour?.setTextColor(ContextCompat.getColor(this, R.color.black))
        }
    }

    private fun setUpHours() {
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
        return ((metrics.widthPixels - 2 * resources.getDimensionPixelSize(R.dimen.left_right_main_screen) - 6 * 40) / 6)
    }

    override fun setButtonEnabled(isEnabled: Boolean) {
        setButtonStatus(saveTime, isEnabled)
    }

    override fun saveTime(time: String) {
        setResult(Activity.RESULT_OK, Intent().putExtra(SETTLED_TIME, time))
        finish()
    }

    override fun goToWelcome() {
        //no op
    }
}