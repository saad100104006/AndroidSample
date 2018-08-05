package uk.co.transferx.app.mainscreen.schedule

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_calendar_layout.*
import uk.co.transferx.app.BaseActivity
import uk.co.transferx.app.R
import uk.co.transferx.app.TransferXApplication
import uk.co.transferx.app.mainscreen.schedule.presenter.CalendarPresenter
import java.util.*
import javax.inject.Inject

class CalendarActivity : BaseActivity(), CalendarPresenter.CalendarUI {

    @Inject
    lateinit var presenter: CalendarPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as TransferXApplication).appComponent.inject(this)
        setContentView(R.layout.activity_calendar_layout)
        buttonBackCalendar.setOnClickListener { onBackPressed() }
        customCl.setDataListener { presenter.setData(it) }
        saveDate.setOnClickListener { presenter.goBack() }
    }

    override fun onResume() {
        super.onResume()
        presenter.attachUI(this)
    }

    override fun onPause() {
        presenter.detachUI()
        super.onPause()
    }

    override fun saveData(isEnabled: Boolean) {
        setButtonStatus(saveDate, isEnabled)

    }

    override fun goBackWithData(date: Date) {
        // onActivityResult()
        finish()
    }

    override fun goToWelcome() {
        // no op
    }
}