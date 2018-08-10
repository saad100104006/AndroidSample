package uk.co.transferx.app.mainscreen.schedule.presenter

import uk.co.transferx.app.BasePresenter
import uk.co.transferx.app.UI
import java.util.*
import javax.inject.Inject

class EndTransferPresenter @Inject constructor() :
    BasePresenter<EndTransferPresenter.EndTransferUI>() {

    private var year: String? = null
    private var month: Int? = null
    private var date: Date? = null
    private var never = false


    fun setYear(year: String) {
        this.year = year
        setDate()
        date = null
        validateDate()
    }

    fun setMonth(month: Int) {
        this.month = month
        setDate()
        date = null
        validateDate()
    }

    fun setEndDate(date: Date) {
        this.date = date
        validateDate()
    }

    fun setNever(never: Boolean) {
        this.never = never
        validateDate()
    }

    fun saveTime() {
        if (never) {
            ui?.goBackWithNever()
            return
        }

        ui?.goBackWithTime(date!!)
    }

    private fun validateDate() {
        ui?.setButtonEnabled((date != null && year != null && month != null) || never)
    }

    private fun setDate() {
        if (year != null && month != null) {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, year!!.toInt())
            calendar.set(Calendar.MONTH, month!!)
            ui?.setCalendarDate(calendar.time)
        }
    }

    interface EndTransferUI : UI {
        fun setCalendarDate(date: Date)
        fun setButtonEnabled(isEnabled: Boolean)
        fun goBackWithTime(date: Date)
        fun goBackWithNever()

    }
}