package uk.co.transferx.app.mainscreen.schedule.presenter

import uk.co.transferx.app.BasePresenter
import uk.co.transferx.app.UI
import uk.co.transferx.app.util.Constants.EMPTY
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class RepeatTransferPresenter @Inject constructor() :
    BasePresenter<RepeatTransferPresenter.RepeatTransferUI>() {

    private var date: Date? = null
    private var never = false
    private val formattedDate = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())


    override fun attachUI(ui: RepeatTransferUI?) {
        super.attachUI(ui)
        if (date != null) {
            this.ui?.setDateToView(formattedDate.format(date))
        }
    }


    fun setDate(date: Date) {
        this.date = date
        ui?.setDateToView(formattedDate.format(date))
    }

    fun setNever(never: Boolean) {
        this.never = never
        if (never) {
            date = null
        }
    }

    fun getDate(): String = if (date == null) EMPTY else formattedDate.format(date)

    interface RepeatTransferUI : UI {
        fun setDateToView(date: String)
    }
}