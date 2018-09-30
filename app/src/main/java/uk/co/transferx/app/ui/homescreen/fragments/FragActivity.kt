package uk.co.transferx.app.ui.homescreen.fragments

import android.os.Bundle
import android.support.constraint.Group
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.frag_activity.*

import uk.co.transferx.app.R
import uk.co.transferx.app.TransferXApplication

import uk.co.transferx.app.data.pojo.Transaction
import uk.co.transferx.app.data.pojo.TransactionCreate
import uk.co.transferx.app.ui.base.BaseFragment
import uk.co.transferx.app.ui.homescreen.adapters.ActivityAllAdapter
import uk.co.transferx.app.ui.homescreen.ObservableBoolean
import uk.co.transferx.app.ui.homescreen.presenters.FragActivityPresenter
import javax.inject.Inject


class FragActivity : BaseFragment(), FragActivityPresenter.ActivityFragmentUI, ActivityAllAdapter.ItemClickListener {

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

        tvAll.setOnClickListener {
            tvAll.setBackgroundResource(R.drawable.rounded_corner_left_filled_amber)
            tvRecurrent.setBackgroundResource(R.drawable.rounded_corner_right_amber)
            presenter.loadData(false)
        }

        tvRecurrent.setOnClickListener {
            tvAll.setBackgroundResource(R.drawable.rounded_corner_left_amber)
            tvRecurrent.setBackgroundResource(R.drawable.rounded_corner_right_filled_amber)
            presenter.loadData(true)
        }

        adapter = ActivityAllAdapter(context!!)
        adapter.setClickListener(this)

        val layoutManager = LinearLayoutManager(activity)
        recyclerviewHistory.setLayoutManager(layoutManager)
        recyclerviewHistory.setItemAnimator(DefaultItemAnimator())
        recyclerviewHistory.setAdapter(adapter)

        presenter?.isLoading.addObserver { o, arg ->
            if (o == null || o !is ObservableBoolean) {
                hideProgress()
            } else {
                if (o.value)
                    showProgress()
                else
                    hideProgress()
            }
        }
        presenter?.attachUI(this)
    }

    override fun tagName(): String {
        return FragActivity::class.java.simpleName
    }

    override fun showAllTransactions(transactions: List<Transaction>) {

        emptyMessageGroup.visibility = Group.GONE
        emptyMessageGroup.updatePreLayout(constraintLayout)
        swapAdapters(false, transactions)
        recyclerviewHistory.visibility = View.VISIBLE
    }

    fun swapAdapters(recurrent: Boolean, transactions: List<Transaction>) {
            adapter.setRecurrentType(recurrent)
            adapter.setAllTransactions(transactions)
    }

    override fun onDestroyView() {
        presenter?.isLoading.deleteObservers()
        presenter?.detachUI()
        super.onDestroyView()
    }

    override fun hideAllTransactions() {
        recyclerviewHistory.visibility = View.GONE
        emptyMessageGroup.visibility = Group.VISIBLE
        tvMsgEmptyData.setText(R.string.msg_add_recipient)
        imgEmptyItem.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.cash_send))
    }


    override fun hideRecurrentTransactions() {
        recyclerviewHistory.visibility = View.GONE
        emptyMessageGroup.visibility = Group.VISIBLE
        tvMsgEmptyData.setText(R.string.msg_empty_recurrent)
        imgEmptyItem.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_empty_recurrent))
    }

    override fun showRecurrentTransactions(transactions: List<Transaction>) {

        emptyMessageGroup.visibility = Group.GONE
        emptyMessageGroup.updatePreLayout(constraintLayout)
        swapAdapters(true, transactions)
        recyclerviewHistory.visibility = View.VISIBLE

    }

    override fun goToWelcome() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setError() {
        //  TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun goToRecieptScreen(transaction: TransactionCreate) {
        //  TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
