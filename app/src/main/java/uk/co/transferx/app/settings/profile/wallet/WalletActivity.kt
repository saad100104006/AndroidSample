package uk.co.transferx.app.settings.profile.wallet

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.wallet_activity_layout.*
import org.jetbrains.anko.toast
import uk.co.transferx.app.BaseActivity
import uk.co.transferx.app.R
import uk.co.transferx.app.TransferXApplication
import uk.co.transferx.app.pojo.Card
import uk.co.transferx.app.settings.profile.wallet.adapter.WalletAdapter
import uk.co.transferx.app.settings.profile.wallet.presenter.WalletActivityPresenter
import uk.co.transferx.app.view.ConfirmationDialogFragment
import uk.co.transferx.app.view.ConfirmationDialogFragment.ADDITIONAL_DATA
import uk.co.transferx.app.view.ConfirmationDialogFragment.MESSAGE
import javax.inject.Inject

class WalletActivity : BaseActivity(), WalletActivityPresenter.WalletActivityUI {

    private lateinit var walletAdapter: WalletAdapter

    @Inject
    lateinit var presenter: WalletActivityPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as TransferXApplication).appComponent.inject(this)
        setContentView(R.layout.wallet_activity_layout)
        ButtonBackWallet.setOnClickListener({ onBackPressed() })
        walletAdapter = WalletAdapter(this)
        cardsView.adapter = walletAdapter

    }

    private fun showDialogConfirmation(position: Int) {
        val dialogFragment = ConfirmationDialogFragment()
        val bundle = Bundle()
        bundle.putString(
            MESSAGE,
            getString(
                R.string.delete_user_message,
                "message"
            )
        )
        bundle.putInt(ADDITIONAL_DATA, position)
        dialogFragment.arguments = bundle
        dialogFragment.isCancelable = false
        dialogFragment.show(supportFragmentManager, "TAG")
    }

    override fun onResume() {
        super.onResume()
        presenter.attachUI(this)
    }

    override fun onPause() {
        presenter.detachUI()
        super.onPause()
    }

    override fun fillCardsOnUI(cards: List<Card>) {
        walletAdapter.setCards(cards)
        additionalMargin.visibility = if (cards.isEmpty()) View.GONE else View.VISIBLE
    }

    override fun error(throwable: Throwable) {
        toast("Error occurred ${throwable.message}")
    }
}