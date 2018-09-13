package uk.co.transferx.app.ui.settings.profile.wallet.presenter

import android.content.SharedPreferences
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import uk.co.transferx.app.ui.base.BasePresenter
import uk.co.transferx.app.ui.base.UI
import uk.co.transferx.app.data.remote.CardsApi
import uk.co.transferx.app.data.pojo.Card
import uk.co.transferx.app.data.repository.CardsRepository
import uk.co.transferx.app.ui.settings.profile.wallet.CardMode
import uk.co.transferx.app.ui.settings.profile.wallet.CardType
import uk.co.transferx.app.data.repository.tokenmanager.TokenManager
import uk.co.transferx.app.util.Constants.EMPTY
import javax.inject.Inject

class AddCardPresenter @Inject constructor(
        private val cardsApi: CardsApi,
        private val tokenManager: TokenManager,
        private val cardsRepository: CardsRepository,
        sharedPreferences: SharedPreferences
) :
    BasePresenter<AddCardPresenter.AddCardUI>(sharedPreferences) {

    private var cardNumber: String = EMPTY
    private var expDate: String = EMPTY
    private var cvc: String = EMPTY
    private var nameOfCard: String = EMPTY
    private var cardType: CardType = CardType.UNKNOWN
    private var isVisa: Boolean = false
    private var isMasterCard = false
    private var disposable: Disposable? = null
    private var cardMode = CardMode.ADD
    private var card: Card? = null
    private val visaRegEx = "^4[0-9]{12}(?:[0-9]{3})?$".toRegex()
    private val masterCardRegEx = "^5[1-5][0-9]{14}\$".toRegex()
    private val expDateRegEx = "^(0[1-9]|1[0-2])\\/?([0-9]{4}|[0-9]{2})$".toRegex()


    override fun attachUI(ui: AddCardUI?) {
        super.attachUI(ui)
        if (cardMode == CardMode.EDIT) {
            ui?.setCard(card)
        }
    }

    override fun detachUI() {
        disposable?.dispose()
        super.detachUI()
    }

    fun setCardNumber(number: String) {
        cardNumber = number
        isVisa = visaRegEx.matches(cardNumber)
        isMasterCard = masterCardRegEx.matches(cardNumber)
        validateData()
    }

    fun setMode(cardMode: CardMode) {
        this.cardMode = cardMode
    }

    fun setCard(card: Card) {
        this.card = card
        nameOfCard = card.name
        expDate = card.expDate
        cardNumber = card.number
        isVisa = visaRegEx.matches(card.number)
        isMasterCard = masterCardRegEx.matches(card.number)
        cvc = card.cvc ?: EMPTY
    }

    fun setNameOfCard(nameOfCard: String) {
        this.nameOfCard = nameOfCard
        validateData()
    }

    fun setExpirationDate(expDate: String) {
        this.expDate = expDate
        validateData()
    }

    fun setCVC(cvc: String) {
        this.cvc = cvc
        validateData()
    }

    private fun validateData() {
        cardType = when {
            isVisa -> {
                CardType.VISA
            }
            isMasterCard -> {
                CardType.MASTERCARD
            }
            else -> {
                CardType.UNKNOWN
            }
        }
        ui?.setButtonEnabled(isValid())
    }

    fun saveCard() {
        disposable = tokenManager.token
            .flatMap {
                cardsApi.addCards(
                    it.accessToken,
                    Card(null, nameOfCard, cardNumber, cardType.name, expDate, cvc)
                )
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                cardsRepository.clearCards()
                ui?.goBack()
            }, { globalErrorHandler(it) })
    }

    fun saveEditedCard() {
        val editedCard = Card(null, nameOfCard, cardNumber, cardType.name, expDate, cvc)
        if (isCardsTheSame(editedCard)) {
            ui?.goBack()
            return
        }
        disposable = tokenManager.token
            .flatMap { cardsApi.saveEditedCard(it.accessToken, card?.id, editedCard) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ ui?.goBack() }, { ui?.error() })

    }

    override fun globalErrorHandler(throwable: Throwable) {
        super.globalErrorHandler(throwable)
        ui?.error()
    }

    private fun isCardsTheSame(newCard: Card): Boolean {
        return newCard.name == card?.name && newCard.expDate == card?.expDate
                && newCard.number == card?.number && newCard.cvc == card?.cvc
    }

    private fun isValid(): Boolean {
        return cardNumber.length == 16 && (isVisa || isMasterCard) && cvc.length == 3 && expDateRegEx.matches(
            expDate
        )
    }

    interface AddCardUI : UI {
        fun setButtonEnabled(isEnabled: Boolean)
        fun goBack()
        fun error()
        fun setCard(card: Card?)
    }
}