package uk.co.transferx.app.mainscreen.schedule.presenter

import uk.co.transferx.app.BasePresenter
import uk.co.transferx.app.UI
import java.util.*
import javax.inject.Inject

class CalendarPresenter @Inject constructor() : BasePresenter<CalendarPresenter.CalendarUI>() {

    private var date: Date = Date()

    fun setData(date: Date) {
        this.date = date
        ui?.saveData(!isCurrentData(date))
    }

    private fun isCurrentData(date: Date): Boolean {
        val calOne = Calendar.getInstance()
        val calTwo = Calendar.getInstance()
        calOne.time = date
        calTwo.time = Date()
        return calOne.get(Calendar.YEAR) == calTwo.get(Calendar.YEAR)
                && calOne.get(Calendar.DAY_OF_YEAR) == calTwo.get(Calendar.DAY_OF_YEAR)
    }

    fun goBack() {
        ui?.goBackWithData(date)
    }


    interface CalendarUI : UI {
        fun saveData(isEnabled: Boolean)
        fun goBackWithData(date: Date)
    }
}