package uk.co.transferx.app.settings.profile.wallet

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.wallet_activity_layout.*
import uk.co.transferx.app.BaseActivity
import uk.co.transferx.app.R
import uk.co.transferx.app.TransferXApplication
import uk.co.transferx.app.settings.profile.wallet.adapter.WalletAdapter
import uk.co.transferx.app.settings.profile.wallet.presenter.WalletActivityPresenter
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

    override fun onResume() {
        super.onResume()
        presenter.attachUI(this)
    }

    override fun onPause() {
        presenter.detachUI()
        super.onPause()
    }

    override fun fillCardsOnUI(cards: Set<String>) {
        walletAdapter.setCards(cards)
        additionalMargin.visibility = if(cards.isEmpty()) View.GONE else View.VISIBLE
    }
}