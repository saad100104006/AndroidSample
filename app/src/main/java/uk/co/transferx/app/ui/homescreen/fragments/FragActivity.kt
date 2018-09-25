package uk.co.transferx.app.ui.homescreen.fragments

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.frag_activity.*

import uk.co.transferx.app.R
import uk.co.transferx.app.ui.base.BaseFragment

class FragActivity : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.frag_activity, container, false)

        tvAll.setOnClickListener {
            tvAll.setBackgroundColor(ContextCompat.getColor(context!!, R.color.amber))
            tvRecipient.setBackgroundColor(ContextCompat.getColor(context!!, R.color.transparent))

            tvMsgEmptyData.setText(R.string.msg_add_recipient)
            imgEmptyItem.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.cash_send))
        }

        tvRecipient.setOnClickListener {
            tvAll.setBackgroundColor(ContextCompat.getColor(context!!, R.color.transparent))
            tvRecipient.setBackgroundColor(ContextCompat.getColor(context!!, R.color.amber))

            tvMsgEmptyData.setText(R.string.msg_empty_recurrent)
            imgEmptyItem.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_empty_recurrent))
        }

        tvSendMoney.setOnClickListener { }
        return view
    }

    override fun tagName(): String {
        return FragActivity::class.java.simpleName
    }
}
