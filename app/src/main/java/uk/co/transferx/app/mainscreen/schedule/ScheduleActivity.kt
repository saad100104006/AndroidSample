package uk.co.transferx.app.mainscreen.schedule

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.schedule_activity_layout.*
import uk.co.transferx.app.BaseActivity
import uk.co.transferx.app.R
import uk.co.transferx.app.TransferXApplication
import uk.co.transferx.app.welcom.WelcomeActivity
import java.util.*
import javax.inject.Inject


class ScheduleActivity : BaseActivity(), ScheduleActivityPresenter.ScheduleActivityUI {

    private lateinit var dayOfWeek: Array<TextView>
    private lateinit var markersOfcurrentDay: Array<TextView>

    @Inject
    lateinit var presenter: ScheduleActivityPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as TransferXApplication).appComponent.inject(this)
        setContentView(R.layout.schedule_activity_layout)
        dayOfWeek = arrayOf(One, Two, Tree, Four, Five, Six, Seven)
        markersOfcurrentDay = arrayOf(oneToday, twoToday, treeToday, fourToday, fiveToday, sixToday, sevenToday)
        arrow.setOnClickListener { Toast.makeText(this, "Calendar clicked", Toast.LENGTH_SHORT).show() }
        buttonBackSchedule.setOnClickListener { onBackPressed() }
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

    private fun setInitialDate(date: Date) {
        val calendar: Calendar = Calendar.getInstance()
        calendar.time = date
        val currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        calendar.add(Calendar.DATE, 0 - currentDayOfWeek)

        for (i: Int in 0 until dayOfWeek.size) {
            if (i < currentDayOfWeek - 1) {
                dayOfWeek[i].setTextColor(ContextCompat.getColor(this, R.color.not_active))
            }
            calendar.add(Calendar.DATE, 1)
            dayOfWeek[i].text = calendar.get(Calendar.DAY_OF_MONTH).toString()
            markersOfcurrentDay[i].visibility = View.INVISIBLE
        }
        (markersOfcurrentDay[currentDayOfWeek.dec()]).visibility = View.VISIBLE

    }

    override fun goToWelcome() {
        WelcomeActivity.startWelcomeActivity(this@ScheduleActivity)
    }

    private fun setChoosenDate() {

    }
}