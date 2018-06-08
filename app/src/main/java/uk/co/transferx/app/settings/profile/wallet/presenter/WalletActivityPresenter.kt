package uk.co.transferx.app.settings.profile.wallet.presenter

import android.content.SharedPreferences
import uk.co.transferx.app.BasePresenter
import uk.co.transferx.app.UI
import uk.co.transferx.app.util.Constants.CARDS
import javax.inject.Inject

class WalletActivityPresenter @Inject constructor(private val sharedPreferences: SharedPreferences): BasePresenter<WalletActivityPresenter.WalletActivityUI>() {

    override fun attachUI(ui: WalletActivityUI?) {
        super.attachUI(ui)
        setCards()
    }

    private fun setCards(){
        val cards = sharedPreferences.getStringSet(CARDS, emptySet())
        ui?.fillCardsOnUI(cards)
    }

    interface WalletActivityUI : UI{
        fun fillCardsOnUI (cards: Set<String>)
    }
}