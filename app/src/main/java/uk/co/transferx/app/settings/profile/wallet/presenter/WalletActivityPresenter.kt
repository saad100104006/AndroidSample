package uk.co.transferx.app.settings.profile.wallet.presenter

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import uk.co.transferx.app.BasePresenter
import uk.co.transferx.app.UI
import uk.co.transferx.app.api.CardsApi
import uk.co.transferx.app.pojo.Card
import uk.co.transferx.app.tokenmanager.TokenManager
import javax.inject.Inject

class WalletActivityPresenter @Inject constructor(
    private val tokenManager: TokenManager,
    private val cardsApi: CardsApi
) : BasePresenter<WalletActivityPresenter.WalletActivityUI>() {

    override fun attachUI(ui: WalletActivityUI?) {
        super.attachUI(ui)
        setCards()
    }

    private fun setCards() {
        cardsApi.getCards(tokenManager.token)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ resp -> ui?.fillCardsOnUI(resp.cards) }, { ui?.error(it) })


    }

    interface WalletActivityUI : UI {
        fun fillCardsOnUI(cards: List<Card>)
        fun error(throwable: Throwable)
    }
}