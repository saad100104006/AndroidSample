package uk.co.transferx.app.data.repository

import io.reactivex.Single
import uk.co.transferx.app.data.remote.CardsApi
import uk.co.transferx.app.data.pojo.Card
import uk.co.transferx.app.data.pojo.Cards
import uk.co.transferx.app.data.repository.tokenmanager.TokenManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CardsRepository @Inject constructor(
        private val cardsApi: CardsApi,
        private val tokenManager: TokenManager
) {
    private val cards: MutableList<Card> = ArrayList()
    private var shouldRefresh: Boolean = true

    fun getCards(): Single<List<Card>> {
        if (!cards.isEmpty()) {
            return Single.just(cards)
        }
        return tokenManager.token
            .flatMap<Cards> { (accessToken) -> cardsApi.getCards(accessToken) }
            .map { it.cards }
            .doOnSuccess { cards.addAll(it) }
    }

    fun clearCards() {
        cards.clear()
        shouldRefresh = true
    }

    fun shouldRefresh(): Boolean{
        val temp = shouldRefresh
        shouldRefresh = false
        return temp
    }

}