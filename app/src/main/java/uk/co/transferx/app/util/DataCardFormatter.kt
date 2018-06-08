package uk.co.transferx.app.util

import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher

class DataCardFormatter : TextWatcher {

    private var current = ""

    private var onAction = { data: String -> }

    fun setLister(onAction: (String) -> Unit) {
        this.onAction = onAction
    }


    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun afterTextChanged(s: Editable) {
        if (s.toString() != current) {
            val userInput = s.toString().replace("[^\\d]".toRegex(), "")

            if (userInput.length <= 4) {
                val sb = StringBuilder()
                for (i in 0 until userInput.length) {
                    if (i % 2 == 0 && i > 0) {
                        sb.append("/")
                    }
                    sb.append(userInput[i])
                }
                current = sb.toString()

                s.filters = arrayOfNulls<InputFilter>(0)
            }
            s.replace(0, s.length, current, 0, current.length)
            onAction(s.toString())

        }
    }
}