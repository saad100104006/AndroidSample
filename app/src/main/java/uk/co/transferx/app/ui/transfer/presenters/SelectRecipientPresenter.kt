package uk.co.transferx.app.ui.transfer.presenters

import io.reactivex.disposables.CompositeDisposable
import uk.co.transferx.app.data.dto.RecipientDto
import uk.co.transferx.app.data.pojo.Recipient
import uk.co.transferx.app.data.repository.recipientsrepository.RecipientRepository
import uk.co.transferx.app.ui.base.BasePresenter
import uk.co.transferx.app.ui.base.UI
import uk.co.transferx.app.util.schedulers.BaseSchedulerProvider
import javax.inject.Inject

/**
 * Created by Catalin Ghita on 15.11.2018.
 */
class SelectRecipientPresenter @Inject
constructor(private val recipientRepository: RecipientRepository, private val schedulerProvider: BaseSchedulerProvider) :
        BasePresenter<SelectRecipientPresenter.SelectRecipientView>() {
    private var compositeDisposable: CompositeDisposable? = null
    private var recipientDtos: List<RecipientDto> = ArrayList<RecipientDto>()

    override fun attachUI(ui: SelectRecipientView?) {
        super.attachUI(ui)

        if (compositeDisposable == null) compositeDisposable = CompositeDisposable()

        val dis = recipientRepository
                .recipients
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ recipients ->
                    recipientDtos = recipients.sortedBy { it.firstName }
                    this.ui.showRecipientList(recipientDtos)
                }, { this.ui.showError()
                })

        compositeDisposable!!.add(dis)

    }

    override fun detachUI() {
        super.detachUI()
        compositeDisposable?.let {
            compositeDisposable!!.dispose()
            compositeDisposable = null
        }
    }

    fun goToNextTransferStep(data: RecipientDto){
        this.ui.goToNextStep(data)
    }

    fun goToAddRecipient(){
        this.ui.goToAddRecipient()
    }

    fun cancelTransfer(){
        this.ui.goBack()
    }

    fun displayFilterRecipientDtos(query: String){
        this.ui.showRecipientList(filter(recipientDtos, query))
    }

    private fun filter(list: List<RecipientDto>, query: String): List<RecipientDto> {
        val filteredRecipientDtos = ArrayList<RecipientDto>()
        for(recipient in list){
            val testedString = recipient.firstName.toLowerCase() + recipient.lastName.toLowerCase()
            if(testedString.contains(query.toLowerCase())) filteredRecipientDtos.add(recipient)
        }
        return filteredRecipientDtos.sortedBy { it.firstName }
    }

    interface SelectRecipientView: UI {
        fun goToNextStep(data: RecipientDto)

        fun goBack()

        fun goToAddRecipient()

        fun showRecipientList(recipients: List<RecipientDto>)

        fun showError()
    }
}