package uk.co.transferx.app.ui.transfer.fragment


import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_select_recipient.*
import org.jetbrains.anko.intentFor

import uk.co.transferx.app.R
import uk.co.transferx.app.TransferXApplication
import uk.co.transferx.app.data.dto.RecipientDto
import uk.co.transferx.app.data.pojo.Recipient
import uk.co.transferx.app.ui.base.BaseFragment
import uk.co.transferx.app.ui.transfer.adapters.RecipientsAdapter
import uk.co.transferx.app.ui.transfer.presenters.SelectRecipientPresenter
import javax.inject.Inject
import uk.co.transferx.app.R.id.searchView
import uk.co.transferx.app.ui.landing.LandingActivity
import uk.co.transferx.app.ui.recipients.addrecipients.AddRecipientsActivity
import uk.co.transferx.app.ui.recipients.addrecipients.Mode
import uk.co.transferx.app.util.Constants


/**
 * Created by Catalin Ghita on 15.11.2018.
 */
class SelectRecipientFragment : BaseFragment(), SelectRecipientPresenter.SelectRecipientView,
        RecipientsAdapter.ItemClickListener, SearchView.OnQueryTextListener {
    internal lateinit var adapter: RecipientsAdapter
    @Inject
    lateinit var presenter: SelectRecipientPresenter

    override fun onItemClick(view: View, data: RecipientDto) {
        presenter.goToNextTransferStep(data)
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

        setupRecyclerView()

        searchView.setOnClickListener { searchView.onActionViewExpanded() }
        searchView.setOnQueryTextListener(this)
        val icon = searchView.findViewById(android.support.v7.appcompat.R.id.search_button) as ImageView
        icon.setColorFilter(Color.BLACK)

        ivCancel.setOnClickListener { presenter.cancelTransfer() }
        ivCAddRecipient.setOnClickListener { presenter.goToAddRecipient() }
    }

    override fun onResume() {
        super.onResume()
        presenter.attachUI(this)
    }

    override fun onPause() {
        presenter.detachUI()
        super.onPause()
    }

    override fun goToNextStep(data: RecipientDto) {
        // TODO later implementation
        Toast.makeText(activity, "This action should redirect you the the second transfer screen", Toast.LENGTH_LONG).show()
    }

    override fun goToAddRecipient(){
        context?.startActivity(context?.intentFor<AddRecipientsActivity>(Constants.MODE to Mode.ADD.ordinal))
    }

    override fun goBack() {
        activity?.finish()
    }

    override fun showRecipientList(recipients: List<RecipientDto>) {
        adapter.setRecipients(recipients)
    }

    override fun goToWelcome() {
        context?.startActivity(context?.intentFor<LandingActivity>())
        activity?.finish()
    }

    override fun showError() {
        val snackbar = Snackbar.make(view!!, getString(R.string.generic_error), Snackbar.LENGTH_LONG)
        snackbar.view.setBackgroundColor(Color.RED)
        snackbar.show()
    }

    override fun tagName(): String {
        return SelectRecipientFragment::class.java.simpleName
    }

    override fun onQueryTextSubmit(p0: String?): Boolean {
        // Not needed
        return true
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        p0?.let {  presenter.displayFilterRecipientDtos(p0) }
        recyclerviewHistory.scrollToPosition(0)
        return true
    }

    private fun setupRecyclerView(){
        val layoutManager = LinearLayoutManager(activity)
        recyclerviewHistory.layoutManager = layoutManager
        recyclerviewHistory.itemAnimator = DefaultItemAnimator()
        recyclerviewHistory.adapter = adapter
    }
}
