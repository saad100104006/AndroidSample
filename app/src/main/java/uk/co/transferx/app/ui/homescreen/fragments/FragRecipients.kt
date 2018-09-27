package uk.co.transferx.app.ui.homescreen.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.frag_recipients.*

import uk.co.transferx.app.R
import uk.co.transferx.app.ui.base.BaseFragment

class FragRecipients : BaseFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frag_recipients, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imgSearch.setOnClickListener { }
        imgAddRecipient.setOnClickListener { callAddRecipient() }
        tvAddRecipient.setOnClickListener { callAddRecipient() }
    }

    internal fun callAddRecipient() {
    }

    override fun tagName(): String {
        return FragRecipients::class.java.simpleName
    }
}