package uk.co.transferx.app.ui.homescreen.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import org.jetbrains.anko.intentFor

import uk.co.transferx.app.R
import uk.co.transferx.app.ui.base.BaseFragment
import uk.co.transferx.app.ui.recipients.addrecipients.AddRecipientsActivity
import uk.co.transferx.app.ui.recipients.addrecipients.Mode
import uk.co.transferx.app.util.Constants

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
        context?.startActivity(context?.intentFor<AddRecipientsActivity>(Constants.MODE to Mode.ADD.ordinal))
    }

    override fun tagName(): String {
        return FragRecipients::class.java.simpleName
    }
}
