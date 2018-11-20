package uk.co.transferx.app.ui.transfer.fragment


import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_select_recipient.*

import uk.co.transferx.app.R
import uk.co.transferx.app.TransferXApplication
import uk.co.transferx.app.data.dto.RecipientDto
import uk.co.transferx.app.data.pojo.Recipient
import uk.co.transferx.app.ui.base.BaseFragment
import uk.co.transferx.app.ui.transfer.adapters.RecipientsAdapter
import uk.co.transferx.app.ui.transfer.presenters.SelectRecipientPresenter
import javax.inject.Inject

/**
 * Created by Catalin Ghita on 15.11.2018.
 */
class SelectRecipientFragment : BaseFragment(), SelectRecipientPresenter.SelectRecipientView,
        RecipientsAdapter.ItemClickListener {
    internal lateinit var adapter: RecipientsAdapter
    @Inject
    lateinit var presenter: SelectRecipientPresenter

    override fun onItemClick(view: View, data: RecipientDto) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (activity?.application as TransferXApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_recipient, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = RecipientsAdapter(context!!)
        adapter.setClickListener(this)

        // TODO edit those
        val layoutManager = LinearLayoutManager(activity)
        recyclerviewHistory.setLayoutManager(layoutManager)
        recyclerviewHistory.setItemAnimator(DefaultItemAnimator())
        recyclerviewHistory.setAdapter(adapter)
    }

    override fun onResume() {
        super.onResume()
        presenter.attachUI(this)
    }

    override fun onPause() {
        presenter.detachUI()
        super.onPause()
    }

    override fun goToNextStep() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showRecipientList(recipients: List<RecipientDto>) {
        adapter.setRecipients(recipients)
    }

    override fun goToWelcome() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun tagName(): String {
        return SelectRecipientFragment::class.java.simpleName
    }
}
