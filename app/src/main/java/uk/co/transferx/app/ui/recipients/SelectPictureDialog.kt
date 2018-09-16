package uk.co.transferx.app.ui.recipients

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.widget.Toast

class SelectPictureDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val selectPicOptions = arrayOf<CharSequence>("Take photo", "Choose photo from Gallery")

        val builder = AlertDialog.Builder(context!!)
            .setItems(selectPicOptions) { _, option -> handleClickOptions(option) }
        return builder.create()
    }

    private fun handleClickOptions(option: Int) {

            when (option) {
                TAKE_PHOTO -> Toast.makeText(context, "We need S3 for that", Toast.LENGTH_SHORT).show()
                GALLERY -> Toast.makeText(context, "We need S3 for that",Toast.LENGTH_SHORT).show()
            }
    }

    interface PictureSettingsOptionsHandler {
        fun takePhoto()

        fun chooseFromGallery()
    }

    companion object {
        private const val TAKE_PHOTO = 0
        private const val GALLERY = 1

        fun newInstance(): SelectPictureDialog {
            return SelectPictureDialog()
        }
    }
}
