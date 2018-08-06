package uk.co.transferx.app.mainscreen.schedule.presenter

import android.util.Log
import uk.co.transferx.app.BasePresenter
import uk.co.transferx.app.UI
import uk.co.transferx.app.util.Constants.EMPTY
import javax.inject.Inject

class TimePresenter @Inject constructor() : BasePresenter<TimePresenter.TimeUI>() {

    private var hour: String = EMPTY
    private var minutes: String = EMPTY
    private var amPm: String = EMPTY

    fun setHour(hour: String) {
        this.hour = hour
        ui?.setButtonEnabled(shouldSave())
    }

    fun setMinutes(minutes: String) {
        this.minutes = minutes
        ui?.setButtonEnabled(shouldSave())
    }

    fun setAMPM(amPm: String) {
        this.amPm = amPm
        ui?.setButtonEnabled(shouldSave())
    }

    fun saveTime(){
        ui?.saveTime("$hour$minutes $amPm")
        Log.d("Serge", "$hour$minutes $amPm")
    }

    private fun shouldSave() = !hour.isEmpty() && !minutes.isEmpty() && !amPm.isEmpty()

    interface TimeUI : UI {
        fun setButtonEnabled(isEnabled: Boolean)
        fun saveTime(time: String)
    }
}