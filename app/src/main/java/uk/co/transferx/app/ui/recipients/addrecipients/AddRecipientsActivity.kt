package uk.co.transferx.app.ui.recipients.addrecipients

import android.app.Activity
import android.os.Bundle
import android.os.Parcelable

import uk.co.transferx.app.ui.base.BaseActivity
import uk.co.transferx.app.R
import uk.co.transferx.app.ui.recipients.addrecipients.fragments.AddRecipientSuccessFragment
import uk.co.transferx.app.ui.recipients.addrecipients.fragments.AddRecipientsFragment

import uk.co.transferx.app.util.Constants.MODE
import uk.co.transferx.app.util.Constants.RECIPIENT

/**
 * Created by sergey on 03/01/2018.
 */

class AddRecipientsActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_recipients_activity_layout)

        launchAddEditRecipientsFragment()
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED)
        finish()
        super.onBackPressed()
    }

     private fun launchAddEditRecipientsFragment(){
        val fragment = AddRecipientsFragment()
        val bundle = Bundle()
        val mode = Mode.values()[intent.getIntExtra(MODE, 3)]
        if (mode == Mode.EDIT) bundle.putParcelable(RECIPIENT, intent.getParcelableExtra<Parcelable>(RECIPIENT))
        bundle.putInt(MODE, intent.getIntExtra(MODE, -1))
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment, fragment.tag).commit()
    }

     fun launchAddRecipientConfirmationFragment(mode: Int){
        val fragment = AddRecipientSuccessFragment()
        val bundle = Bundle()
        bundle.putInt(MODE, mode)
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment, fragment.tag).commit()
    }
}
