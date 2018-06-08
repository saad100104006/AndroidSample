package uk.co.transferx.app.settings.profile.wallet.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.card_wallet_item.view.*

class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val card: TextView = itemView.cardNumber

    fun bindata(textNumber: String) {
        card.setText(textNumber)
    }

}