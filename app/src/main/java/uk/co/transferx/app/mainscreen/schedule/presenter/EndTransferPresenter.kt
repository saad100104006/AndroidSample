package uk.co.transferx.app.mainscreen.schedule.presenter

import uk.co.transferx.app.BasePresenter
import uk.co.transferx.app.UI
import java.util.*
import javax.inject.Inject

class EndTransferPresenter @Inject constructor() :
    BasePresenter<EndTransferPresenter.EndTransferUI>() {

    private var year: String? = null
    private var month: Int? = null


    public fun setYear(year: String) {
        this.year = year
        setDate()
    }

    public fun setMonth(month: Int) {
        this.month = month
        setDate()
    }


    private fun setDate(){
        if(year != null && month != null) {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, year!!.toInt())
            calendar.set(Calendar.MONTH, month!!)
            ui?.setCalendarDate(calendar.time)
        }
    }

    interface EndTransferUI : UI {
        fun setCalendarDate(date: Date)

    }
}