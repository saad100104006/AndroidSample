package uk.co.transferx.app.ui.homescreen

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.TextView
import uk.co.transferx.app.R
import uk.co.transferx.app.ui.base.BaseActivity
import uk.co.transferx.app.ui.homescreen.fragments.FragActivity
import uk.co.transferx.app.ui.homescreen.fragments.FragRecipients
import uk.co.transferx.app.ui.homescreen.fragments.FragSettings
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        tvActivity.setOnClickListener {
            tvActivity.setTextColor(ContextCompat.getColor(this, R.color.amber))
            setTextViewDrawableColor(tvActivity, R.color.amber)
            viewActivityBar.setBackgroundColor(ContextCompat.getColor(this, R.color.amber))

            tvRecipients.setTextColor(ContextCompat.getColor(this, R.color.grey_text))
            setTextViewDrawableColor(tvRecipients, R.color.grey_text)
            viewRecipientsBar.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent))

            tvSettings.setTextColor(ContextCompat.getColor(this, R.color.grey_text))
            setTextViewDrawableColor(tvSettings, R.color.grey_text)
            viewSettingsBar.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent))

            callFragmentActivity()
        }

        tvRecipients.setOnClickListener {
            tvActivity.setTextColor(ContextCompat.getColor(this, R.color.grey_text))
            setTextViewDrawableColor(tvActivity, R.color.grey_text)
            viewActivityBar.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent))

            tvRecipients.setTextColor(ContextCompat.getColor(this, R.color.amber))
            setTextViewDrawableColor(tvRecipients, R.color.amber)
            viewRecipientsBar.setBackgroundColor(ContextCompat.getColor(this, R.color.amber))

            tvSettings.setTextColor(ContextCompat.getColor(this, R.color.grey_text))
            setTextViewDrawableColor(tvSettings, R.color.grey_text)
            viewSettingsBar.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent))

            callFragmentRecipients()
        }

        tvSettings.setOnClickListener {
            tvActivity.setTextColor(ContextCompat.getColor(this, R.color.grey_text))
            setTextViewDrawableColor(tvActivity, R.color.grey_text)
            viewActivityBar.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent))

            tvRecipients.setTextColor(ContextCompat.getColor(this, R.color.grey_text))
            setTextViewDrawableColor(tvRecipients, R.color.grey_text)
            viewRecipientsBar.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent))

            tvSettings.setTextColor(ContextCompat.getColor(this, R.color.amber))
            setTextViewDrawableColor(tvSettings, R.color.amber)
            viewSettingsBar.setBackgroundColor(ContextCompat.getColor(this, R.color.amber))

            callFragmentSettings()
        }

        callFragmentActivity()
    }

    private fun setTextViewDrawableColor(textView: TextView, color: Int) {
        for (drawable in textView.compoundDrawables) {
            if (drawable != null) {
                drawable.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(this, color), PorterDuff.Mode.SRC_IN)
            }
        }
    }

    internal fun callFragmentActivity() {
        replaceFragment(FragActivity(), 0, R.id.containerHome)
    }

    internal fun callFragmentRecipients() {
        replaceFragment(FragRecipients(), 0, R.id.containerHome)
    }

    internal fun callFragmentSettings() {
        replaceFragment(FragSettings(), 0, R.id.containerHome)
    }
}
