package uk.co.transferx.app.mainscreen.schedule.presenter

import uk.co.transferx.app.BasePresenter
import uk.co.transferx.app.UI
import uk.co.transferx.app.pojo.TransactionCreate
import uk.co.transferx.app.util.Constants.EMPTY
import uk.co.transferx.app.util.Util.formattedDateForSend
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ScheduleActivityPresenter @Inject constructor() :
    BasePresenter<ScheduleActivityPresenter.ScheduleActivityUI>() {

    private var time: String = EMPTY
    private var date: Date? = null
    private var calendar: Calendar = Calendar.getInstance()
    private lateinit var transaction: TransactionCreate
    private val simpleDateFomat = SimpleDateFormat("hh:mm aa", Locale.getDefault())

    override fun attachUI(ui: ScheduleActivityUI?) {
        super.attachUI(ui)
        this.ui?.setButton(isEnabled())
    }

    fun setTime(time: String) {
        this.time = time
        val cal = Calendar.getInstance()
        cal.time = simpleDateFomat.parse(time)
        calendar.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY))
        calendar.set(Calendar.MINUTE, cal.get(Calendar.MINUTE))
        ui?.setButton(isEnabled())
    }

    fun setTransaction(transaction: TransactionCreate) {
        this.transaction = transaction
    }

    fun setDate(date: Date) {
        this.date = date
        calendar.time = date
        ui?.setButton(isEnabled())
    }

    fun goToNextScreen() {
        ui?.goToNext(transaction.copy(startTime = formattedDateForSend(calendar.time)))
    }



    private fun isEnabled() = !time.isEmpty() && date != null

    interface ScheduleActivityUI : UI {
        fun setButton(isEnabled: Boolean)
        fun goToNext(transaction: TransactionCreate)
    }
}