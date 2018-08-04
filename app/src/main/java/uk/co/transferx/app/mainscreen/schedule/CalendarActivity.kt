package uk.co.transferx.app.mainscreen.schedule

import android.os.Bundle
import kotlinx.android.synthetic.main.schedule_activity_layout.*
import uk.co.transferx.app.BaseActivity
import uk.co.transferx.app.R
import uk.co.transferx.app.TransferXApplication
import uk.co.transferx.app.mainscreen.schedule.presenter.CalendarPresenter
import javax.inject.Inject

class CalendarActivity : BaseActivity(), CalendarPresenter.CalendarUI {

    @Inject
    lateinit var presenter: CalendarPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as TransferXApplication).appComponent.inject(this)
        setContentView(R.layout.activity_calendar_layout)
        buttonBackSchedule.setOnClickListener { onBackPressed() }
    }

    override fun goToWelcome() {
        // no op
    }
}