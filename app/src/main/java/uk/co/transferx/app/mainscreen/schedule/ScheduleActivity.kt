package uk.co.transferx.app.mainscreen.schedule

import android.os.Bundle
import kotlinx.android.synthetic.main.schedule_activity_layout.*
import uk.co.transferx.app.BaseActivity
import uk.co.transferx.app.R
import java.util.*


class ScheduleActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.schedule_activity_layout)
        val calendar = Calendar.getInstance()
        calendarMain.setUserCurrentMonthYear(7, 2018)

    }
}