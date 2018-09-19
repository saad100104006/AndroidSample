package uk.co.transferx.app.ui.settings.support

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_support_layout.*
import org.jetbrains.anko.toast
import uk.co.transferx.app.ui.base.BaseActivity
import uk.co.transferx.app.R
import uk.co.transferx.app.TransferXApplication

class SupportActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as TransferXApplication).appComponent.inject(this)
        setContentView(R.layout.activity_support_layout)
        buttonBackSupport.setOnClickListener { onBackPressed() }
        chatTitle.setOnClickListener {
            // Smooch.init(application, Settings("")) {
            //   if (it.error == null) {
            //  ConversationActivity.show()
            toast("We have conflict in libraries with smooch, should fixed later")
            // }
        }


    }
}