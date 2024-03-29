package uk.co.transferx.app.ui.settings.profile.wallet.adapter

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import uk.co.transferx.app.ui.base.BaseActivity
import uk.co.transferx.app.R
import uk.co.transferx.app.data.pojo.Card
import uk.co.transferx.app.ui.settings.profile.wallet.AddCardActivity
import uk.co.transferx.app.ui.settings.profile.wallet.CardMode
import uk.co.transferx.app.util.Constants.MODE


class WalletAdapter(private val activity: BaseActivity) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private var paymentCards: MutableList<Card> = mutableListOf()

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
            {
                val intent = Intent(activity, AddCardActivity::class.java)
                intent.putExtra(MODE, CardMode.ADD.ordinal)
                activity.startActivity(intent)
            }
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
            holder.bindata(paymentCards[position])

        }

        is EmptyHeaderHolder -> {
        }
        else -> throw IllegalArgumentException("error argument $holder")
    }

    fun setCards(cards: List<Card>) {
        paymentCards = cards.toMutableList()
        notifyDataSetChanged()
    }

    fun getCard(position: Int): Card {
        return paymentCards[position]

    }

    fun deleteCard(card: Card) {
        val itemPosition = paymentCards.indexOf(card)
        paymentCards.remove(card)
        notifyItemRemoved(itemPosition)
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

    companion object {
        const val EMPTY_HEADER: Int = 10
        const val FOOTER: Int = 20
        const val ITEM: Int = 30
    }
}