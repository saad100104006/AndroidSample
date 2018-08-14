package uk.co.transferx.app.mainscreen.schedule.presenter

import uk.co.transferx.app.BasePresenter
import uk.co.transferx.app.UI
import uk.co.transferx.app.pojo.TransactionCreate
import javax.inject.Inject

class ReviewPresenter @Inject constructor() : BasePresenter<ReviewPresenter.ReviewUI>() {


    private lateinit var transaction: TransactionCreate

    fun setTransaction(transaction: TransactionCreate) {
        this.transaction = transaction
    }


    override fun attachUI(ui: ReviewUI?) {
        super.attachUI(ui)
        this.ui?.setDataToSummary(transaction)

    }


    interface ReviewUI : UI {

        fun goToTransferSummary()

        fun setDataToSummary(transaction: TransactionCreate)
    }
}