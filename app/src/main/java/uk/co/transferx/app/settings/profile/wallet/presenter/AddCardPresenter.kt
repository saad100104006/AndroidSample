package uk.co.transferx.app.settings.profile.wallet.presenter

import android.content.SharedPreferences
import timber.log.Timber
import uk.co.transferx.app.BasePresenter
import uk.co.transferx.app.UI
import uk.co.transferx.app.util.Constants.CARDS
import uk.co.transferx.app.util.Constants.EMPTY
import uk.co.transferx.app.util.Util
import javax.inject.Inject

class AddCardPresenter @Inject constructor(private val sharedPreferences: SharedPreferences) :
    BasePresenter<AddCardPresenter.AddCardUI>() {

    private var cardNumber: String = EMPTY
    private var expDate: String = EMPTY
    private var cvc: String = EMPTY
    private var nameOfCard: String = EMPTY

    fun setCardNumber(number: String) {
        cardNumber = number
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
        ui?.setButtonEnabled(isValid())
    }

    fun saveCard() {
        val cards = sharedPreferences.getStringSet(CARDS, HashSet<String>())
        cards.add(cardNumber)
        sharedPreferences.edit().putStringSet(CARDS, cards).apply()
        ui?.goBack()
    }

    private fun isValid(): Boolean {
        return cardNumber.length == 16 &&
                ("^4[0-9]{12}(?:[0-9]{3})?$".toRegex().matches(cardNumber) || "^5[1-5][0-9]{14}\$".toRegex().matches(
                    cardNumber
                )) &&
                expDate.matches("(?:0[1-9]|1[0-2])/[0-9]{2}".toRegex())
                && cvc.length == 3
    }


    interface AddCardUI : UI {
        fun setButtonEnabled(isEnabled: Boolean)
        fun goBack()
    }
}