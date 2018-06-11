package uk.co.transferx.app.settings.profile.wallet.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import org.jetbrains.anko.startActivity
import uk.co.transferx.app.BaseActivity
import uk.co.transferx.app.R
import uk.co.transferx.app.pojo.Card
import uk.co.transferx.app.settings.profile.wallet.AddCardActivity
import uk.co.transferx.app.settings.profile.wallet.CardType


class WalletAdapter(private val activity: BaseActivity) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val EMPTY_HEADER: Int = 10
        const val FOOTER: Int = 20
        const val ITEM: Int = 30
    }

    private var paymentCards: List<Card> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            RecyclerView.ViewHolder = when (viewType) {
        EMPTY_HEADER -> EmptyHeaderHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.empty_wallet_layout, parent, false)
        )
        FOOTER -> FooterHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.footer_wallet_layout,
                parent,
                false
            ),
            { activity.startActivity<AddCardActivity>() }
        )

        ITEM -> ItemHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.card_wallet_item, parent, false)
        )
        else -> throw IllegalStateException("Error state $viewType")
    }

    override fun getItemCount(): Int {
        if (paymentCards.isEmpty()) {
            return 2
        }
        return paymentCards.size + 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = when (holder) {
        is FooterHolder -> {

        }
        is ItemHolder -> {
            val numberCard = paymentCards[position]

            holder.bindata(numberCard.toString())
            val cardType = if(CardType.VISA == CardType.valueOf(numberCard.type.capitalize())) R.drawable.ic_visa else R.drawable.ic_master_card
            holder.card.setCompoundDrawablesWithIntrinsicBounds(cardType, 0, 0, 0)
        }

        is EmptyHeaderHolder -> {
        }
        else -> throw IllegalArgumentException("error argument $holder")
    }

    fun setCards(cards: List<Card>) {
        paymentCards = cards
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        if (paymentCards.isEmpty() && position == 0) {
            return EMPTY_HEADER
        }
        if (paymentCards.isEmpty() && position == 1) {
            return FOOTER
        }
        if (!paymentCards.isEmpty() && position == paymentCards.size) {
            return FOOTER
        }
        return ITEM
    }
}