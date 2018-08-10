package uk.co.transferx.app.mainscreen.schedule.presenter

import uk.co.transferx.app.BasePresenter
import uk.co.transferx.app.UI
import uk.co.transferx.app.pojo.TransactionCreate
import uk.co.transferx.app.util.Constants.EMPTY
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class RepeatTransferPresenter @Inject constructor() :
    BasePresenter<RepeatTransferPresenter.RepeatTransferUI>() {

    private var date: Date? = null
    private var never = false
    private val formattedDate = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    private val formatDateToSend =
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.'000Z'", Locale.getDefault())

    private lateinit var transaction: TransactionCreate
    private var shouldRepeat = false
    private var frequency: String? = null
    private var startDate: Date = Date()
    private var neverString: String = EMPTY


    override fun attachUI(ui: RepeatTransferUI?) {
        super.attachUI(ui)
        if (date != null) {
            this.ui?.setDateToView(formattedDate.format(date))
        }
        validateInput()
    }

    fun setTransaction(transaction: TransactionCreate) {
        this.transaction = transaction
    }

    fun setRepeat(shouldRepeat: Boolean) {
        this.shouldRepeat = shouldRepeat
        validateInput()
    }

    fun setDate(date: Date) {
        this.date = date
        ui?.setDateToView(formattedDate.format(date))
        validateInput()
    }

    fun setNeverString(never: String) {
        neverString = never
    }

    fun setFrequency(frequency: String) {
        this.frequency = frequency
        validateInput()
    }

    fun setNever(never: Boolean) {
        this.never = never
        if (never) {
            date = null
        }
        validateInput()
    }

    private fun validateInput() {
        ui?.setButtonEnabled(shouldEnableButton())
    }

    fun goToSummaryScreen() {
        if (!shouldRepeat) {
            ui?.goToNextScreen(transaction.copy(repeat = shouldRepeat))
            return
        }
        val endDate = if (date == null) EMPTY else formatDateToSend.format(date)
        ui?.goToNextScreen(
            transaction.copy(
                frequency = frequency?.toLowerCase(),
                startTime = formatDateToSend.format(startDate), endTime = endDate
            )
        )
    }

    private fun shouldEnableButton(): Boolean {
        return !shouldRepeat || ((never && frequency != null) || (date != null && frequency != null))
    }

    fun getDate(): String = when {
        (date == null && never) -> neverString
        (date != null) -> formattedDate.format(date)
        else -> EMPTY
    }

    interface RepeatTransferUI : UI {
        fun setDateToView(date: String)
        fun goToNextScreen(transaction: TransactionCreate)
        fun setButtonEnabled(enabled: Boolean)
    }
}