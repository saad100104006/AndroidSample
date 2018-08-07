package uk.co.transferx.app.mainscreen.schedule

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_repeate_transfer_layout.*
import org.jetbrains.anko.toast
import uk.co.transferx.app.BaseActivity
import uk.co.transferx.app.R
import uk.co.transferx.app.TransferXApplication
import uk.co.transferx.app.mainscreen.schedule.presenter.RepeatTransferPresenter
import javax.inject.Inject
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.widget.TextView


class RepeatTransferActivity : BaseActivity(), RepeatTransferPresenter.RepeatTransferUI {

    @Inject
    lateinit var presenter: RepeatTransferPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as TransferXApplication).appComponent.inject(this)
        setContentView(R.layout.activity_repeate_transfer_layout)
        setButtonStatus(buttonFinish, true)
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
        setRepeatState(buttonsRepeat.checkedRadioButtonId)

    }

    override fun onPause() {
        presenter.detachUI()
        super.onPause()
    }

    private fun setRepeatState(buttonId: Int) {
        when (buttonId) {
            R.id.noRepeat -> {
                setTextViewDrawableColor(endInput, R.color.not_active)
            }
            R.id.yesRepeat -> {
                setTextViewDrawableColor(endInput, R.color.black)
            }
        }
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
}