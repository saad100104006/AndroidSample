package uk.co.transferx.app.ui.homescreen.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import kotlinx.android.synthetic.main.frag_activity.*

import uk.co.transferx.app.R
import uk.co.transferx.app.TransferXApplication

import uk.co.transferx.app.data.pojo.Transaction
import uk.co.transferx.app.data.pojo.TransactionCreate
import uk.co.transferx.app.ui.base.BaseFragment
import uk.co.transferx.app.ui.homescreen.adapters.ActivityAllAdapter
import uk.co.transferx.app.data.pojo.Transactions
import uk.co.transferx.app.ui.homescreen.presenters.FragActivityPresenter
import javax.inject.Inject


class FragActivity : BaseFragment(), FragActivityPresenter.ActivityFragmentUI, ActivityAllAdapter.ItemClickListener{

    internal lateinit var adapter: ActivityAllAdapter

    @Inject
    lateinit var presenter: FragActivityPresenter

    override fun onItemClick(view: View, data: Transaction) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as TransferXApplication).appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frag_activity, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvAll.setOnClickListener{
            tvAll.setBackgroundColor(ContextCompat.getColor(context!!, R.color.amber))
            tvRecurrent.setBackgroundColor(ContextCompat.getColor(context!!, R.color.transparent))

            tvMsgEmptyData.setText(R.string.msg_add_recipient)
            imgEmptyItem.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.cash_send))
        }

        tvRecurrent.setOnClickListener{
            tvAll.setBackgroundColor(ContextCompat.getColor(context!!, R.color.transparent))
            tvRecurrent.setBackgroundColor(ContextCompat.getColor(context!!, R.color.amber))

            tvMsgEmptyData.setText(R.string.msg_empty_recurrent)
            imgEmptyItem.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_empty_recurrent))
        }


        // TODO: For showing history part ..
        adapter = ActivityAllAdapter(context!!, presenter)
        adapter.setClickListener(this)
        relativeLayout.setHasFixedSize(true)
        relativeLayout.setLayoutManager(GridLayoutManager(context, 1, LinearLayoutManager.VERTICAL, false))
        relativeLayout.setAdapter(adapter)
    }

    override fun onResume() {
        super.onResume()
        presenter?.attachUI(this)
    }

    override fun onPause() {
        presenter?.detachUI()
        super.onPause()
    }

    override fun tagName(): String {
        return FragActivity::class.java.simpleName
    }

    override fun setData(transactions: List<Transaction>) {
        val isEmpty = transactions.isEmpty()
        relativeLayout.setVisibility(if (isEmpty) View.VISIBLE else View.GONE)
        imgEmptyItem.setVisibility(if (isEmpty) View.GONE else View.VISIBLE)
        tvMsgEmptyData.setVisibility(if (isEmpty) View.GONE else View.VISIBLE)
        tvSendMoney.setVisibility(if (isEmpty) View.GONE else View.VISIBLE)

        adapter.setAllTransactions(transactions)
    }

    override fun goToWelcome() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setError() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun goToMoneySendScreen() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
