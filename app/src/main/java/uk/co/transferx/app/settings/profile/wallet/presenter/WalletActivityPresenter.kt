package uk.co.transferx.app.settings.profile.wallet.presenter

import android.content.SharedPreferences
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import uk.co.transferx.app.BasePresenter
import uk.co.transferx.app.UI
import uk.co.transferx.app.api.CardsApi
import uk.co.transferx.app.pojo.Card
import uk.co.transferx.app.repository.CardsRepository
import uk.co.transferx.app.tokenmanager.TokenManager
import java.net.HttpURLConnection
import javax.inject.Inject

class WalletActivityPresenter @Inject constructor(
    private val tokenManager: TokenManager,
    private val cardsApi: CardsApi,
    private val cardsRepository: CardsRepository,
    sharedPreferences: SharedPreferences
) : BasePresenter<WalletActivityPresenter.WalletActivityUI>(sharedPreferences) {

    private var compositeDisposable: CompositeDisposable? = null

    override fun attachUI(ui: WalletActivityUI?) {
        super.attachUI(ui)
        compositeDisposable = CompositeDisposable()
        setCards()
    }

    override fun detachUI() {
        super.detachUI()
        compositeDisposable?.dispose()
    }

    private fun setCards() {
        compositeDisposable?.add(
            cardsRepository.getCards()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ ui?.fillCardsOnUI(it) }, { ui?.error(it) })
        )

    }

    fun deleteCard(card: Card) {
        compositeDisposable?.add(
            tokenManager.token
                .flatMap { cardsApi.deleteCard(it.accessToken, card.id) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { resp -> if (resp.code() == HttpURLConnection.HTTP_OK)
                    {
                        ui?.deleteCard(card)
                        cardsRepository.clearCards()
                    } },
                    { ui?.error(it) })
        )
    }


    interface WalletActivityUI : UI {
        fun fillCardsOnUI(cards: List<Card>)
        fun error(throwable: Throwable)
        fun deleteCard(card: Card)
    }
}