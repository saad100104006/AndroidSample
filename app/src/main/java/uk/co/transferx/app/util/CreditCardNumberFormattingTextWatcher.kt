package uk.co.transferx.app.util

import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.EditText
import uk.co.transferx.app.R

class CreditCardNumberFormattingTextWatcher(private val editText: EditText?) : TextWatcher {

    private var onAction = { data: String -> }

    fun setLister(onAction: (String) -> Unit) {
        this.onAction = onAction
    }


    init {
        editText?.transformationMethod = object : PasswordTransformationMethod() {
            override fun getTransformation(source: CharSequence, view: View?): CharSequence {
                return object : CharSequence {
                    override fun subSequence(startIndex: Int, endIndex: Int): CharSequence {
                        return source
                    }

                    override fun get(index: Int): Char {
                        val asterisk = '*'
                        val space = ' '
                        return when (index) {
                            in 0..3 -> asterisk
                            4 -> space
                            in 4..8 -> asterisk
                            9 -> space
                            in 10..13 -> asterisk
                            else -> source[index]
                        }
                    }

                    override val length: Int
                        get() = source.length
                }
            }
        }
    }

    private var current = ""

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
    }

    override fun afterTextChanged(s: Editable) {


        if (s.toString() != current) {
            val userInput = s.toString().replace("[^\\d]".toRegex(), "")
            if (userInput.length <= 16) {
                val sb = StringBuilder()
                for (i in 0 until userInput.length) {
                    if (i % 4 == 0 && i > 0) {
                        sb.append(" ")
                    }
                    sb.append(userInput[i])
                }
                current = sb.toString()

                s.filters = arrayOfNulls<InputFilter>(0)
            }
            s.replace(0, s.length, current, 0, current.length)

        }
        val card = s.replace("\\s".toRegex(), "")
        val visa = "^4[0-9]{12}(?:[0-9]{3})?$".toRegex().matches(card)
        val masterCard = "^5[1-5][0-9]{14}\$".toRegex().matches(card)
        val cardType = when {
            visa -> R.drawable.ic_visa
            masterCard -> R.drawable.ic_master_card
            else -> R.drawable.payment_placeholder
        }
        onAction(card)
        editText?.setCompoundDrawablesWithIntrinsicBounds(0, 0, cardType, 0)
    }
}
