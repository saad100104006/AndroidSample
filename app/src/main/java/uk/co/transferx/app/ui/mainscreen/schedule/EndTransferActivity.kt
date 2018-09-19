package uk.co.transferx.app.ui.mainscreen.schedule

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_end_transfer_layout.*
import uk.co.transferx.app.ui.base.BaseActivity
import uk.co.transferx.app.R
import uk.co.transferx.app.TransferXApplication
import uk.co.transferx.app.ui.mainscreen.schedule.RepeatTransferActivity.Companion.END_DATE
import uk.co.transferx.app.ui.mainscreen.schedule.presenter.EndTransferPresenter
import java.text.DateFormatSymbols
import java.util.*
import javax.inject.Inject

class EndTransferActivity : BaseActivity(), EndTransferPresenter.EndTransferUI {

    @Inject
    lateinit var presenter: EndTransferPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as TransferXApplication).appComponent.inject(this)
        setContentView(R.layout.activity_end_transfer_layout)
        buttonBackEnd.setOnClickListener { onBackPressed() }
        yearSpinner.setDataWithHintItem(getYears(), getString(R.string.yyyy))
        monthSpinner.setDataWithHintItem(
            DateFormatSymbols(Locale.getDefault()).months,
            getString(R.string.month)
        )
        customCl.setDataListener { presenter.setEndDate(it) }
        setNever.setOnCheckedChangeListener { buttonView, isChecked ->
            setTime.isChecked = !isChecked
            presenter.setNever(isChecked)
        }
        setTime.setOnCheckedChangeListener { buttonView, isChecked ->
            setNever.isChecked = !isChecked
        }
        yearSpinner.setOnItemSelectedListener { position, obj -> presenter.setYear(obj.toString()) }
        monthSpinner.setOnItemSelectedListener { position, obj -> presenter.setMonth(position) }
        saveTime.setOnClickListener { presenter.saveTime() }
    }

    override fun onResume() {
        super.onResume()
        presenter.attachUI(this)
    }

    override fun onPause() {
        presenter.detachUI()
        super.onPause()
    }

    private fun getYears(): Array<String> {
        val calendar: Calendar = Calendar.getInstance()
        calendar.time = Date()
        val currentYear = calendar.get(Calendar.YEAR)
        return Array(MAX_YEARS) { "${(currentYear + it)}" }
    }

    override fun setCalendarDate(date: Date) {
        customCl.setDate(date)
    }

    override fun setButtonEnabled(isEnabled: Boolean) {
        setButtonStatus(saveTime, isEnabled)
    }

    override fun goBackWithTime(date: Date) {
        setResult(Activity.RESULT_OK, Intent().putExtra(END_DATE, date.time))
        finish()
    }

    override fun goBackWithNever() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun goToWelcome() {
        // no op
    }

    companion object {
        const val MAX_YEARS = 10
    }

}