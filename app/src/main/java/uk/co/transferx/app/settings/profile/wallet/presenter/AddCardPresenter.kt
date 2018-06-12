package uk.co.transferx.app.settings.profile.wallet.presenter

import android.content.SharedPreferences
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import uk.co.transferx.app.BasePresenter
import uk.co.transferx.app.UI
import uk.co.transferx.app.api.CardsApi
import uk.co.transferx.app.pojo.Card
import uk.co.transferx.app.settings.profile.wallet.CardType
import uk.co.transferx.app.tokenmanager.TokenManager
import uk.co.transferx.app.util.Constants.CARDS
import uk.co.transferx.app.util.Constants.EMPTY
import uk.co.transferx.app.util.Util
import javax.inject.Inject

class AddCardPresenter @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val cardsApi: CardsApi,
    private val tokenManager: TokenManager
) :
    BasePresenter<AddCardPresenter.AddCardUI>() {

    private var cardNumber: String = EMPTY
    private var expDate: String = EMPTY
    private var cvc: String = EMPTY
    private var nameOfCard: String = EMPTY
    private var cardType: CardType = CardType.UNKNOWN
    private var isVisa: Boolean = false
    private var isMasterCard = false
    private var disposable: Disposable? = null

    override fun detachUI() {
        disposable?.dispose()
        super.detachUI()
    }

    fun setCardNumber(number: String) {
        cardNumber = number
        isVisa = "^4[0-9]{12}(?:[0-9]{3})?$".toRegex().matches(cardNumber)
        isMasterCard = "^5[1-5][0-9]{14}\$".toRegex().matches(cardNumber)
        validateData()

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
        disposable = cardsApi.addCards(
            tokenManager.token,
            Card(null, nameOfCard, cardNumber, cardType.name, expDate, cvc)
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ resp ->
                val cards = sharedPreferences.getStringSet(CARDS, HashSet<String>())
                cards.add(resp.id)
                sharedPreferences.edit().putStringSet(CARDS, cards).apply()
                ui?.goBack()
            }, { ui?.error() })
    }

    private fun isValid(): Boolean {
        return cardNumber.length == 16 &&
                (isVisa || isMasterCard)
                && cvc.length == 3
    }


    interface AddCardUI : UI {
        fun setButtonEnabled(isEnabled: Boolean)
        fun goBack()
        fun error()
    }
}