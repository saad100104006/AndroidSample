package uk.co.transferx.app.ui.homescreen.fragments

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import uk.co.transferx.app.R
import uk.co.transferx.app.ui.base.BaseFragment

class FragActivity : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.frag_activity, container, false)

        val tvAll = view.findViewById<TextView>(R.id.tv_all)
        val tvRecipients = view.findViewById<TextView>(R.id.tv_recipient)

        val emptyDataMsg = view.findViewById<TextView>(R.id.tv_msg_empty_data)
        val emptyDataImg = view.findViewById<ImageView>(R.id.img_empty_item)

        tvAll.setOnClickListener(View.OnClickListener {
            tvAll.setBackgroundColor(ContextCompat.getColor(context!!, R.color.amber))
            tvRecipients.setBackgroundColor(ContextCompat.getColor(context!!, R.color.transparent))

            emptyDataMsg.setText(R.string.msg_add_recipient)
            emptyDataImg.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.cash_send))
        })


        tvRecipients.setOnClickListener(View.OnClickListener {
            tvAll.setBackgroundColor(ContextCompat.getColor(context!!, R.color.transparent))
            tvRecipients.setBackgroundColor(ContextCompat.getColor(context!!, R.color.amber))

            emptyDataMsg.setText(R.string.msg_empty_recurrent)
            emptyDataImg.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_empty_recurrent))
        })


        val tvSendMoney = view.findViewById<TextView>(R.id.tv_send_money)
        tvSendMoney.setOnClickListener { }

        return view
    }

    override fun tagName(): String {
        return FragActivity::class.java.simpleName
    }
}
