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
constructor(private val recipientRepository: RecipientRepository, val schedulerProvider: BaseSchedulerProvider) :
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
                }, { throwable ->
                    globalErrorHandler(throwable)
                })

        compositeDisposable!!.add(dis)

//        // Get recipients
//        val list = ArrayList<Recipient>()
//        val recipient1 =   Recipient.Builder()
//                .firstname("Anna")
//                .lastname("Frank")
//                .country("XX")
//                .phone("39348")
//                .build()
//        val recipient2 =   Recipient.Builder()
//                .firstname("Britney")
//                .lastname("Nest")
//                .country("XX")
//                .phone("39348")
//                .build()
//
//        val recipient33 =   Recipient.Builder()
//                .firstname("Johny")
//                .lastname("WNend")
//                .country("XX")
//                .phone("39348")
//                .build()
//
//        val recipient3 =   Recipient.Builder()
//                .firstname("John")
//                .lastname("Wick")
//                .country("XX")
//                .phone("39348")
//                .build()
//        val recipient4 =   Recipient.Builder()
//                .firstname("Welney")
//                .lastname("Pardy")
//                .country("XX")
//                .phone("39348")
//                .build()
//
//        val recipient44 =   Recipient.Builder()
//                .firstname("Wxn")
//                .lastname("Pardy")
//                .country("XX")
//                .phone("39348")
//                .build()
//
//        val recipient5 =   Recipient.Builder()
//                .firstname("Xanxar")
//                .lastname("Lod")
//                .country("XX")
//                .phone("39348")
//                .build()
//
//        list.add(recipient1)
//        list.add(recipient2)
//        list.add(recipient3)
//        list.add(recipient33)
//        list.add(recipient4)
//        list.add(recipient44)
//        list.add(recipient5)



//
//        val sortedList = list.sortedWith(compareBy { it.firstname[0] })
//
//        this.ui.showRecipientList(list)
        this.ui.showRecipientList(recipientDtos.sortedBy { it.firstName })
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