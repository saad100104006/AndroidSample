package uk.co.transferx.app.ui.homescreen.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.frag_recipients.*

import uk.co.transferx.app.R
import uk.co.transferx.app.ui.base.BaseFragment

class FragRecipients : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.frag_recipients, container, false)

        imgSearch.setOnClickListener {  }

        imgAddRecipient.setOnClickListener { callAddRecipient() }

        tvAddRecipient.setOnClickListener { callAddRecipient() }

        return view
    }

    internal fun callAddRecipient() {

    }

    override fun tagName(): String {
        return FragRecipients::class.java.simpleName
    }
}
