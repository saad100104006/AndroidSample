package uk.co.transferx.app.ui.homescreen.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import uk.co.transferx.app.R
import uk.co.transferx.app.ui.base.BaseFragment

class FragActivity : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frag_activity, container, false)
    }

    override fun tagName(): String {
        return FragActivity::class.java.simpleName
    }
}
