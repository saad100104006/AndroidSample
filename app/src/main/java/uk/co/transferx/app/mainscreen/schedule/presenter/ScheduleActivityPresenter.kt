package uk.co.transferx.app.mainscreen.schedule.presenter

import android.util.Log
import uk.co.transferx.app.BasePresenter
import uk.co.transferx.app.UI
import uk.co.transferx.app.util.Constants.EMPTY
import java.util.*
import javax.inject.Inject

class ScheduleActivityPresenter @Inject constructor() :
    BasePresenter<ScheduleActivityPresenter.ScheduleActivityUI>() {

    private var time: String = EMPTY
    private var date: Date? = null
    private var calendar: Calendar = Calendar.getInstance()


    override fun attachUI(ui: ScheduleActivityUI?) {
        super.attachUI(ui)
        this.ui?.setButton(isEnabled())
    }

    fun setTime(time: String) {
        this.time = time
        ui?.setButton(isEnabled())
        Log.d("Serge", time)
    }

    fun setDate(date: Date) {
        this.date = date
        calendar.time = date
        ui?.setButton(isEnabled())
    }

    fun goToNextScreen() {
      ui?.goToNext(calendar.time.time)
    }

    private fun isEnabled() = !time.isEmpty() && date != null

    interface ScheduleActivityUI : UI {
        fun setButton(isEnabled: Boolean)
        fun goToNext(time: Long)
    }
}