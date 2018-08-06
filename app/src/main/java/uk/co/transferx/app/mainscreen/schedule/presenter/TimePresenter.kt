package uk.co.transferx.app.mainscreen.schedule.presenter

import uk.co.transferx.app.BasePresenter
import uk.co.transferx.app.UI
import javax.inject.Inject

class TimePresenter @Inject constructor() : BasePresenter<TimePresenter.TimeUI>() {

    private var hour: Int = -1
    private var minutes :Int = -1

    fun setHour(hour: Int) {
        this.hour = hour
    }

    fun setMinutes(minutes: Int){
        this.minutes = minutes
    }

    interface TimeUI : UI {

    }
}