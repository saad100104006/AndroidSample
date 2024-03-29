package uk.co.transferx.app.ui.settings.profile.wallet

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.wallet_activity_layout.*
import org.jetbrains.anko.toast
import uk.co.transferx.app.ui.base.BaseActivity
import uk.co.transferx.app.R
import uk.co.transferx.app.TransferXApplication
import uk.co.transferx.app.ui.mainscreen.adapters.SwipeHelper
import uk.co.transferx.app.data.pojo.Card
import uk.co.transferx.app.ui.settings.profile.wallet.adapter.WalletAdapter
import uk.co.transferx.app.ui.settings.profile.wallet.presenter.WalletActivityPresenter
import uk.co.transferx.app.util.Constants.DELETE
import uk.co.transferx.app.ui.customview.ConfirmationDialogFragment
import uk.co.transferx.app.ui.customview.ConfirmationDialogFragment.MESSAGE
import uk.co.transferx.app.ui.customview.ConfirmationDialogFragment.POSITION
import uk.co.transferx.app.ui.signin.SignInActivity
import javax.inject.Inject

class WalletActivity : BaseActivity(), WalletActivityPresenter.WalletActivityUI {

    private lateinit var walletAdapter: WalletAdapter
    private var isRegistered = false
    private lateinit var localBroadcastReceiver: BroadcastReceiver

    @Inject
    lateinit var presenter: WalletActivityPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as TransferXApplication).appComponent.inject(this)
        setContentView(R.layout.wallet_activity_layout)
        ButtonBackWallet.setOnClickListener { onBackPressed() }
        walletAdapter = WalletAdapter(this)
        cardsView.adapter = walletAdapter

        object : SwipeHelper(cardsView) {
            override fun instantiateUnderlayButton(
                viewHolder: RecyclerView.ViewHolder,
                underlayButtons: MutableList<UnderlayButton>?
            ) {
                underlayButtons!!.add(
                    UnderlayButton(
                        getString(R.string.delete).toUpperCase(), ContextCompat.getColor(
                            this@WalletActivity, R.color.red_delete
                        ),
                        object : UnderlayButtonClickListener {
                            override fun onClick(pos: Int) {
                                showDialogConfirmation(pos)
                            }
                        }
                    )
                )
             /*   underlayButtons.add(
                    UnderlayButton(
                        getString(R.string.edit), ContextCompat.getColor(
                            this@WalletActivity, R.color.gray
                        ),
                        object : UnderlayButtonClickListener {
                            override fun onClick(pos: Int) {
                              /*  val intent =
                                    Intent(this@WalletActivity, AddCardActivity::class.java)
                                intent.putExtra(MODE, CardMode.EDIT.ordinal)
                                intent.putExtra(CARD, walletAdapter.getCard(pos))
                                startActivity(intent)*/
                        toast("currently not operational")
                            }
                        }
                    )
                ) */
            }
        }
    }

    private fun showDialogConfirmation(position: Int) {
        localBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == null) {
                    return
                }
                if (intent.action == DELETE) {
                    val pos = intent.getIntExtra(POSITION, -1)
                    if (pos == -1) {
                        return
                    }
                    presenter.deleteCard(walletAdapter.getCard(position))
                }
            }
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(
            localBroadcastReceiver,
            IntentFilter(DELETE)
        )
        isRegistered = true
        val dialogFragment = ConfirmationDialogFragment()
        val bundle = Bundle()
        bundle.putString(
            MESSAGE,
            getString(R.string.card_delete_message)
        )
        bundle.putInt(POSITION, position)
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

    override fun onStop() {
        super.onStop()
        if (isRegistered) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(localBroadcastReceiver)
            isRegistered = false
        }
    }

    override fun deleteCard(card: Card) {
        walletAdapter.deleteCard(card)
    }

    override fun fillCardsOnUI(cards: List<Card>) {
        walletAdapter.setCards(cards)
        additionalMargin.visibility = if (cards.isEmpty()) View.GONE else View.VISIBLE
    }

    override fun error(throwable: Throwable) {
        toast("Error occurred ${throwable.message}")
    }

    override fun goToWelcome() {
        SignInActivity.startSignInActivity(this@WalletActivity)
    }

}