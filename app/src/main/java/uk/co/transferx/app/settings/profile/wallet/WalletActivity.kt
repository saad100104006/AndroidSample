package uk.co.transferx.app.settings.profile.wallet

import android.os.Bundle
import kotlinx.android.synthetic.main.wallet_activity_layout.*
import uk.co.transferx.app.BaseActivity
import uk.co.transferx.app.R
import uk.co.transferx.app.TransferXApplication
import uk.co.transferx.app.settings.profile.wallet.adapter.WalletAdapter

class WalletActivity : BaseActivity() {

    private lateinit var walletAdapter: WalletAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as TransferXApplication).appComponent.inject(this)
        setContentView(R.layout.wallet_activity_layout)
        ButtonBackWallet.setOnClickListener({ onBackPressed() })
        walletAdapter = WalletAdapter(this)
        cardsView.adapter = walletAdapter

    }
}