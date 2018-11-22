package uk.co.transferx.app.ui.transfer.presenters

import io.reactivex.disposables.CompositeDisposable
import uk.co.transferx.app.data.dto.RecipientDto
import uk.co.transferx.app.data.pojo.Recipient
import uk.co.transferx.app.data.repository.recipientsrepository.RecipientRepository
import uk.co.transferx.app.ui.base.BasePresenter
import uk.co.transferx.app.ui.base.UI
import uk.co.transferx.app.util.schedulers.BaseSchedulerProvider
import javax.inject.Inject

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
                    recipientDtos = recipients
                    this.ui.showRecipientList(recipientDtos.sortedBy { it.firstName })
                }, { throwable ->
                    globalErrorHandler(throwable)
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

    interface SelectRecipientView: UI {
        fun goToNextStep()

        fun showRecipientList(recipients: List<RecipientDto>)
    }
}