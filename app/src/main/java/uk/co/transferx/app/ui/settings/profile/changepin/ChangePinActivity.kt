package uk.co.transferx.app.ui.settings.profile.changepin

import android.graphics.Color
import android.os.Bundle
import android.support.annotation.ColorRes
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import com.chaos.view.PinView
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.change_password_activity_layout.*
import kotlinx.android.synthetic.main.change_pin_layout.*
import kotlinx.android.synthetic.main.personal_details_activity_layout.*
import kotlinx.android.synthetic.main.signin_pin_fragment.*
import uk.co.transferx.app.R
import uk.co.transferx.app.TransferXApplication
import uk.co.transferx.app.ui.base.BaseActivity
import uk.co.transferx.app.util.Constants
import javax.inject.Inject

/**
 * Created by HP on 11/1/2018.
 */

class ChangePinActivity : BaseActivity(), ChangePinPresenter.ChangePinUI  {

    @Inject
    lateinit var presenter: ChangePinPresenter
    var compositeDisposable: CompositeDisposable? = null

    private var pinView1: PinView? = null

    private var pinView2: PinView? = null


    private var pinView3: PinView? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as TransferXApplication).appComponent.inject(this)
        setContentView(R.layout.change_pin_layout)
        button_back.setOnClickListener({ onBackPressed() })

        pinView1 = currentPinView
        pinView1?.setAnimationEnable(true)

        pinView2 = newPinView
        pinView2?.setAnimationEnable(true)

        pinView3 = confirmPinView
        pinView3?.setAnimationEnable(true)

        savePin.setOnClickListener({ presenter.saveNewPin() })
    }



    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }

    override fun onResume() {
        super.onResume()
        compositeDisposable = CompositeDisposable()
        val currentPassDis =  presenter.setCurrentPin(pinView1!!.text?.toString()!!)
        val newPassDis = presenter.setNewPin(pinView2!!.text?.toString()!!)
        val confirmPassDis = presenter.setConfirmPin(pinView3!!.text?.toString()!!)

        presenter.attachUI(this)
    }


    override fun onPause() {
        presenter!!.detachUI()
        super.onPause()
    }

    override fun goToWelcome() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setButtonEnabled(enabled: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun goToAccount() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            resetErrorPin()
            s.let { presenter.setPinValue(s.toString()) }
        }

        override fun afterTextChanged(s: Editable) {

        }
    }

    private fun resetErrorPin() {
        setLinesColor(R.color.black)
    }

    private fun setLinesColor(@ColorRes color: Int) {
        pinView1?.setLineColor(ContextCompat.getColor(pinView1!!.context, color))

        pinView2?.setLineColor(ContextCompat.getColor(pinView2!!.context, color))

        pinView3?.setLineColor(ContextCompat.getColor(pinView3!!.context, color))
    }

    override fun showErrorPin() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }



/*    override fun showErrorPin() {
        pinView1?.setText(Constants.EMPTY)
        setLinesColor(R.color.red)
        pinView1?.requestFocus()

        val snackbar = Snackbar.make(view!!, getString(R.string.no_match_pin), Snackbar.LENGTH_LONG)
        snackbar.view.setBackgroundColor(Color.RED)
        snackbar.show()
    }*/






}
