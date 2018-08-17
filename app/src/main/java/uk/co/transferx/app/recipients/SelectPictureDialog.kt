package uk.co.transferx.app.recipients

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog

class SelectPictureDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val selectPicOptions = arrayOf<CharSequence>("Take photo", "Choose photo from Gallery")

        val builder = AlertDialog.Builder(context!!)
            .setItems(selectPicOptions) { _, option -> handleClickOptions(option) }
        return builder.create()
    }

    private fun handleClickOptions(option: Int) {
        if (activity is PictureSettingsOptionsHandler) {
            val pictureSettingsOptionsHandler = activity as PictureSettingsOptionsHandler?
            when (option) {
                TAKE_PHOTO -> pictureSettingsOptionsHandler?.takePhoto()
                GALLERY -> pictureSettingsOptionsHandler?.chooseFromGallery()

            }
        } else {
            throw RuntimeException("Error $option")
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
