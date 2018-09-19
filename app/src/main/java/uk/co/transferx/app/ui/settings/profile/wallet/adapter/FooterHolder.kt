package uk.co.transferx.app.ui.settings.profile.wallet.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.footer_wallet_layout.view.*

class FooterHolder(itemView: View, private val listener: () -> Unit) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    override fun onClick(v: View?) {
        listener.invoke()
    }

    init {
        itemView.addCard.setOnClickListener(this)
    }


}