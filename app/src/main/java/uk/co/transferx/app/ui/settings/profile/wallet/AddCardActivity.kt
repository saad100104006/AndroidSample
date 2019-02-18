package uk.co.transferx.app.ui.settings.profile.wallet

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.add_card_activity.*
import org.jetbrains.anko.toast
import uk.co.transferx.app.ui.base.BaseActivity
import uk.co.transferx.app.R
import uk.co.transferx.app.TransferXApplication
import uk.co.transferx.app.data.pojo.Card
import uk.co.transferx.app.ui.settings.profile.wallet.presenter.AddCardPresenter
import uk.co.transferx.app.util.Constants.CARD
import uk.co.transferx.app.util.Constants.MODE
import uk.co.transferx.app.util.CreditCardNumberFormattingTextWatcher
import uk.co.transferx.app.util.DataCardFormatter
import uk.co.transferx.app.ui.signin.SignInActivity
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AddCardActivity : BaseActivity(), AddCardPresenter.AddCardUI {

    private lateinit var creditCardNumberFormattingTextWatcher: CreditCardNumberFormattingTextWatcher
    private val dataCardFormatter = DataCardFormatter()
    @Inject
    lateinit var presenter: AddCardPresenter
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as TransferXApplication).appComponent.inject(this)
        setContentView(R.layout.activity_new_card)
        val intent = intent
        val cardMode = CardMode.values()[intent.getIntExtra(MODE, 0)]
        presenter.setMode(cardMode)
        ButtonBackCard.setOnClickListener { finish() }
        creditCardNumberFormattingTextWatcher = CreditCardNumberFormattingTextWatcher(cardNumber)
        dataCardFormatter.setLister { presenter.setExpirationDate(it) }
        creditCardNumberFormattingTextWatcher.setLister { presenter.setCardNumber(it) }
        if(cardMode == CardMode.EDIT){
            saveCard.text = getString(R.string.save_changes)
            presenter.setCard(intent.getParcelableExtra(CARD))
            mainCardTitle.text = getString(R.string.edit_card)
            saveCard.setOnClickListener({ presenter.saveEditedCard() })
            return
        }
        saveCard.setOnClickListener({ presenter.saveCard() })

    }

    override fun onResume() {
        super.onResume()
        if (compositeDisposable.isDisposed) {
            compositeDisposable = CompositeDisposable()
        }
        presenter.attachUI(this)
        cardNumber.addTextChangedListener(creditCardNumberFormattingTextWatcher)
        ExpirationDate.addTextChangedListener(dataCardFormatter)
        compositeDisposable += RxTextView.textChanges(nameOnCard)
            .filter({ it.length > 3 })
            .debounce(300, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ presenter.setNameOfCard(it.toString()) }, { Log.e("Serge", "error", it) })
        compositeDisposable += RxTextView.textChanges(securityCode)
            .debounce(300, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ presenter.setCVC(it.toString()) }, { Log.e("Serge", "error", it) })
    }

    override fun onPause() {
        compositeDisposable.dispose()
        cardNumber.removeTextChangedListener(creditCardNumberFormattingTextWatcher)
        ExpirationDate.removeTextChangedListener(dataCardFormatter)
        presenter.detachUI()
        super.onPause()
    }

    override fun setButtonEnabled(isEnabled: Boolean) {
        saveCard.isEnabled = isEnabled
        saveCard.background = if (isEnabled) ContextCompat.getDrawable(
            this,
            R.drawable.oval_button_black
        ) else ContextCompat.getDrawable(this, R.drawable.oval_button_gray)
    }

    override fun goBack() {
        finish()
    }

    override fun setCard(card: Card?) {
        nameOnCard.setText(card?.name)
        cardNumber.setText(card?.getFormattedNumber())
        securityCode.setText(card?.cvc)
        ExpirationDate.setText(card?.expDate)

    }

    override fun error() {
        toast("Error occurred. Card not added")
    }

    override fun goToWelcome() {
        SignInActivity.startSignInActivity(this@AddCardActivity)
    }
}