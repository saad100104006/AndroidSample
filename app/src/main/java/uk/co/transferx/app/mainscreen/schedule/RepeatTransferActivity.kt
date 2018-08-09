package uk.co.transferx.app.mainscreen.schedule

import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_repeate_transfer_layout.*
import org.jetbrains.anko.startActivityForResult
import uk.co.transferx.app.BaseActivity
import uk.co.transferx.app.R
import uk.co.transferx.app.TransferXApplication
import uk.co.transferx.app.mainscreen.schedule.presenter.RepeatTransferPresenter
import javax.inject.Inject


class RepeatTransferActivity : BaseActivity(), RepeatTransferPresenter.RepeatTransferUI {

    @Inject
    lateinit var presenter: RepeatTransferPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as TransferXApplication).appComponent.inject(this)
        setContentView(R.layout.activity_repeate_transfer_layout)
        setButtonStatus(buttonFinish, true)
        endInput.setOnClickListener { startActivityForResult<EndTransferActivity>(END_TRANSFER) }
        buttonBackRepeat.setOnClickListener { onBackPressed() }
        buttonsRepeat.setOnCheckedChangeListener { _, checkedId -> setRepeatState(checkedId) }
        frequencyLabel.setOnItemSelectedListener { position, obj -> }
        frequencyLabel.setDataWithHintItem(
            resources.getStringArray(R.array.frequency_transfer),
            getString(R.string.none)
        )
    }

    override fun onResume() {
        super.onResume()
        presenter.attachUI(this)
    }

    override fun onPause() {
        presenter.detachUI()
        super.onPause()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun setRepeatState(buttonId: Int) {
        when (buttonId) {
            R.id.noRepeat -> {
                setTextViewDrawableColor(endInput, R.color.not_active)
                setColorForState(R.color.not_active)
                setButtonStatus(buttonFinish, true)
                frequencyLabel.visibility = INVISIBLE
                notActive.visibility = VISIBLE
                endInput.isEnabled = false
            }
            R.id.yesRepeat -> {
                setTextViewDrawableColor(endInput, R.color.black)
                setColorForState(R.color.black)
                setButtonStatus(buttonFinish, false)
                notActive.visibility = GONE
                frequencyLabel.visibility = VISIBLE
                endInput.isEnabled = true

            }
        }
    }

    private fun setColorForState(color: Int) {
        val resolvedColor = ContextCompat.getColor(this, color)
        frequencyTitle.setTextColor(resolvedColor)
        underLineFrequency.setBackgroundColor(resolvedColor)
        endTitle.setTextColor(resolvedColor)
        endInput.setHintTextColor(resolveHintColor(color))
        endInput.setBackgroundResource(resolveBackground(color))
    }

    private fun resolveHintColor(color: Int): Int {
        if (color == R.color.not_active) {
            return ContextCompat.getColor(this, color)
        }
        return ContextCompat.getColor(this, R.color.hint)
    }

    private fun resolveBackground(color: Int): Int {
        var backgroundDrawable = R.drawable.underline_normal
        if (color == R.color.not_active) {
            backgroundDrawable = R.drawable.underline_not_active
        }
        return backgroundDrawable
    }

    private fun setTextViewDrawableColor(textView: TextView, color: Int) {
        for (drawable in textView.compoundDrawables) {
            if (drawable != null) {
                drawable.colorFilter =
                        PorterDuffColorFilter(
                            ContextCompat.getColor(this, color),
                            PorterDuff.Mode.SRC_IN
                        )
            }
        }
    }

    override fun goToWelcome() {
        //no op
    }


    companion object {
        const val END_TRANSFER = 555
    }
}