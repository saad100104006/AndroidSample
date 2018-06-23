package uk.co.transferx.app.settings.profile.wallet.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.card_wallet_item.view.*
import uk.co.transferx.app.R
import uk.co.transferx.app.pojo.Card
import uk.co.transferx.app.settings.profile.wallet.CardType

class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val cardText: TextView = itemView.cardNumber
    var cardId: Card? = null

    fun bindata(card: Card) {
        cardId = card
        cardText.text = cardId?.toString()
        val cardType =
            if (CardType.VISA == CardType.valueOf(cardId?.type!!.capitalize())) R.drawable.ic_visa else R.drawable.ic_master_card
        cardText.setCompoundDrawablesWithIntrinsicBounds(cardType, 0, 0, 0)
    }

}