package uk.co.transferx.app.mainscreen.schedule

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.schedule_activity_layout.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult
import uk.co.transferx.app.BaseActivity
import uk.co.transferx.app.R
import uk.co.transferx.app.TransferXApplication
import uk.co.transferx.app.mainscreen.schedule.presenter.ScheduleActivityPresenter
import uk.co.transferx.app.pojo.TransactionCreate
import uk.co.transferx.app.util.Constants.EMPTY
import uk.co.transferx.app.util.Constants.TRANSACTION
import uk.co.transferx.app.welcom.WelcomeActivity
import java.util.*
import javax.inject.Inject


class ScheduleActivity : BaseActivity(), ScheduleActivityPresenter.ScheduleActivityUI {

    private lateinit var dayOfWeek: Array<TextView>
    private lateinit var markersOfCurrentDay: Array<TextView>

    @Inject
    lateinit var presenter: ScheduleActivityPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as TransferXApplication).appComponent.inject(this)
        setContentView(R.layout.schedule_activity_layout)
        presenter.setTransaction(intent.getParcelableExtra(TRANSACTION))
        dayOfWeek = arrayOf(One, Two, Tree, Four, Five, Six, Seven)
        markersOfCurrentDay =
                arrayOf(oneToday, twoToday, treeToday, fourToday, fiveToday, sixToday, sevenToday)
        arrow.setOnClickListener { startActivityForResult<CalendarActivity>(CALENDAR) }
        timeArrow.setOnClickListener { startActivityForResult<TimeActivity>(TIME) }
        buttonBackRepeat.setOnClickListener { onBackPressed() }
        buttonSubmite.setOnClickListener { presenter.goToNextScreen() }
        setInitialDate(Date())
    }

    override fun onResume() {
        super.onResume()
        presenter.attachUI(this)
    }

    override fun onPause() {
        super.onPause()
        presenter.detachUI()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CALENDAR && resultCode == Activity.RESULT_OK) {
            val date = Date(data!!.getLongExtra(SETTLED_DATA, -1))
            setChosenDate(date)
            presenter.setDate(date = date)
        }
        if (requestCode == TIME && resultCode == Activity.RESULT_OK) {
            val time = data?.getStringExtra(SETTLED_TIME)
            timeInput.text = time
            presenter.setTime(time ?: EMPTY)
        }
    }

    private fun setInitialDate(dat: Date) {
        val calendar: Calendar = Calendar.getInstance()
        calendar.time = dat
        val currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        calendar.add(Calendar.DATE, 0 - currentDayOfWeek)

        for (i: Int in 0 until dayOfWeek.size) {
            if (i < currentDayOfWeek - 1) {
                dayOfWeek[i].setTextColor(ContextCompat.getColor(this, R.color.not_active))
            }
            calendar.add(Calendar.DATE, 1)
            dayOfWeek[i].text = calendar.get(Calendar.DAY_OF_MONTH).toString()
            markersOfCurrentDay[i].visibility = View.INVISIBLE
        }
        (markersOfCurrentDay[currentDayOfWeek.dec()]).visibility = View.VISIBLE
        date.text = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())

    }

    override fun goToWelcome() {
        WelcomeActivity.startWelcomeActivity(this@ScheduleActivity)
    }

    private fun setChosenDate(settledDate: Date) {
        val calendar: Calendar = Calendar.getInstance()
        calendar.time = settledDate
        date.text = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
        val settledDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        calendar.add(Calendar.DATE, 0 - settledDayOfWeek)
        for (i: Int in 0 until dayOfWeek.size) {
            val day = dayOfWeek[i]
            day.setTextColor(ContextCompat.getColor(this, R.color.black))
            day.background = ContextCompat.getDrawable(this, R.drawable.circle_calendar)
            if (i == settledDayOfWeek - 1) {
                day.setTextColor(ContextCompat.getColor(this, R.color.white))
                day.background = ContextCompat.getDrawable(this, R.drawable.circle_calendar_green)
            }
            calendar.add(Calendar.DATE, 1)
            day.text = calendar.get(Calendar.DAY_OF_MONTH).toString()
            markersOfCurrentDay[i].visibility = View.INVISIBLE
        }
    }

    override fun setButton(isEnabled: Boolean) {
        setButtonStatus(buttonSubmite, isEnabled)
    }

    override fun goToNext(transaction: TransactionCreate) {
        startActivity<RepeatTransferActivity>(TRANSACTION to transaction)
    }

    companion object {
        const val CALENDAR: Int = 888
        const val TIME: Int = 777
        const val SETTLED_DATA = "settled_data"
        const val SETTLED_TIME = "settled_time"
    }
}