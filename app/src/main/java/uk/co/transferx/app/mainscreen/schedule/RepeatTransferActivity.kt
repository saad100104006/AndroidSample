package uk.co.transferx.app.mainscreen.schedule

import android.app.Activity
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
import uk.co.transferx.app.pojo.TransactionCreate
import uk.co.transferx.app.util.Constants.EMPTY
import uk.co.transferx.app.util.Constants.TRANSACTION
import java.util.*
import javax.inject.Inject


class RepeatTransferActivity : BaseActivity(), RepeatTransferPresenter.RepeatTransferUI {

    @Inject
    lateinit var presenter: RepeatTransferPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as TransferXApplication).appComponent.inject(this)
        presenter.setNeverString(getString(R.string.never))
        presenter.setTransaction(intent.getParcelableExtra(TRANSACTION))
        setContentView(R.layout.activity_repeate_transfer_layout)
        buttonFinish.setOnClickListener { presenter.goToSummaryScreen() }
        endInput.setOnClickListener { startActivityForResult<EndTransferActivity>(END_TRANSFER) }
        buttonBackRepeat.setOnClickListener { onBackPressed() }
        buttonsRepeat.setOnCheckedChangeListener { _, checkedId -> setRepeatState(checkedId) }
        frequencyLabel.setOnItemSelectedListener { position, obj -> presenter.setFrequency(obj.toString()) }
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
        if (requestCode == END_TRANSFER && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                presenter.setDate(date = Date(data.getLongExtra(END_DATE, -1)))
                presenter.setNever(false)
                return
            }
            presenter.setNever(true)
            endInput.text = getString(R.string.never)

        }
    }

    private fun setRepeatState(buttonId: Int) {
        when (buttonId) {
            R.id.noRepeat -> setNotActiveState()
            R.id.yesRepeat -> setActiveState()
        }
    }

    private fun setActiveState() {
        setTextViewDrawableColor(endInput, R.color.black)
        setColorForState(R.color.black)
        setButtonStatus(buttonFinish, false)
        notActive.visibility = GONE
        frequencyLabel.visibility = VISIBLE
        endInput.isEnabled = true
        endInput.text = presenter.getDate()
        presenter.setRepeat(true)
    }

    private fun setNotActiveState() {
        setTextViewDrawableColor(endInput, R.color.not_active)
        setColorForState(R.color.not_active)
        frequencyLabel.visibility = INVISIBLE
        notActive.visibility = VISIBLE
        endInput.isEnabled = false
        endInput.text = EMPTY
        presenter.setRepeat(false)
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

    override fun setDateToView(date: String) {
        endInput.text = date
    }

    override fun goToNextScreen(transaction: TransactionCreate) {

    }

    override fun setButtonEnabled(enabled: Boolean) {
        setButtonStatus(buttonFinish, enabled)
    }

    override fun goToWelcome() {
        //no op
    }

    companion object {
        const val END_TRANSFER = 555
        const val END_DATE = "end_date"
    }
}