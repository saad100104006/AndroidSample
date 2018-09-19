package uk.co.transferx.app.ui.homescreen.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import uk.co.transferx.app.R
import uk.co.transferx.app.ui.base.BaseFragment

class FragRecipients : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.frag_recipients, container, false)

        val imgSearch = view.findViewById<ImageView>(R.id.img_search)
        imgSearch.setOnClickListener {  }

        val imgAddRecipient = view.findViewById<ImageView>(R.id.img_add_recipient)
        imgAddRecipient.setOnClickListener { callAddRecipient() }

        val tvAddRecipient = view.findViewById<TextView>(R.id.tv_add_recipient)
        tvAddRecipient.setOnClickListener { callAddRecipient() }

        return view
    }

    internal fun callAddRecipient() {

    }

    override fun tagName(): String {
        return FragRecipients::class.java.simpleName
    }
}
