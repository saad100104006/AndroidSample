package uk.co.transferx.app.ui.settings.presenter

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.content.LocalBroadcastManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView

import uk.co.transferx.app.R

import android.app.Activity.RESULT_OK
import uk.co.transferx.app.util.Constants.DELETE
import uk.co.transferx.app.util.Constants.EMPTY

class ConfirmationDialogLogoutFragment : DialogFragment() {
    private var message: String? = null
    private var position: Int = 0
    private var id: String? = null

    interface CallBackInterfaceDialog {
        fun onSucces()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        if (bundle != null) {
            message = bundle.getString(MESSAGE, EMPTY)
            position = bundle.getInt(POSITION, -1)
            id = bundle.getString(ADDITIONAL_DATA, null)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.confirmation_logout_dialog_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (view.findViewById<View>(R.id.message) as TextView).text = resources.getString(R.string.confirmation)
        (view.findViewById<View>(R.id.details) as TextView).text = resources.getString(R.string.logout_text)
        view.findViewById<View>(R.id.button_no).setOnClickListener { v -> dismiss() }
        view.findViewById<View>(R.id.button_yes).setOnClickListener { v -> clickOkButton() }
    }

    private fun clickOkButton() {
        val fragment = targetFragment
        val intent: Intent
        if (fragment != null) {
            intent = Intent()
            intent.putExtra(ADDITIONAL_DATA, id)
            intent.putExtra(POSITION, position)
            fragment.onActivityResult(targetRequestCode, RESULT_OK, intent)
        } else {
            intent = Intent(DELETE)
            intent.putExtra(POSITION, position)
            LocalBroadcastManager.getInstance(activity!!).sendBroadcast(
                    intent)
        }

        dismiss()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    companion object {

        val MESSAGE = "message"
        val ADDITIONAL_DATA = "additional_data"
        val POSITION = "position"
    }
}
