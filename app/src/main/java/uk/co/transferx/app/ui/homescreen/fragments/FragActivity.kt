package uk.co.transferx.app.ui.homescreen.fragments

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import uk.co.transferx.app.R
import uk.co.transferx.app.ui.base.BaseFragment

class FragActivity : BaseFragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.frag_activity, container, false)

        val tvAll = view.findViewById<TextView>(R.id.tv_all)
        val tvRecipients = view.findViewById<TextView>(R.id.tv_recipient)

        tvAll.setOnClickListener(View.OnClickListener {
            tvAll.setBackgroundColor(ContextCompat.getColor(context!!, R.color.amber))
            tvRecipients.setBackgroundColor(ContextCompat.getColor(context!!, R.color.transparent))
        })


        tvRecipients.setOnClickListener {
            tvRecipients.setOnClickListener(View.OnClickListener {
                tvAll.setBackgroundColor(ContextCompat.getColor(context!!, R.color.transparent))
                tvRecipients.setBackgroundColor(ContextCompat.getColor(context!!, R.color.amber)) })
        }

        val tvSendMoney = view.findViewById<TextView>(R.id.tv_send_money)
        tvSendMoney.setOnClickListener {  }

        return view
    }

    override fun tagName(): String {
        return FragActivity::class.java.simpleName
    }
}
