package uk.co.transferx.app.ui.homescreen.fragments

import android.os.Bundle
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

import uk.co.transferx.app.data.pojo.Transaction
import uk.co.transferx.app.ui.base.BaseFragment
import uk.co.transferx.app.ui.homescreen.adapters.ActivityAllAdapter
import uk.co.transferx.app.data.pojo.Transactions


class FragActivity : BaseFragment(), ActivityAllAdapter.ItemClickListener{
    override fun onItemClick(view: View, data: Transaction) {

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

        imgEmptyItem.visibility = View.GONE
        tvMsgEmptyData.visibility = View.GONE
        tvSendMoney.visibility = View.GONE

        val relativeLayout = view.findViewById<RecyclerView>(R.id.relativeLayout)
        val str = "{\"transactions\": [{\"id\": 1,\"transaction_schedule_id\": 1,\"from\": \"5a5ca3375d82df077128a941\",\"to\": \"d71bd947-c9f9-451b-ab48-09abe1165154\",\"currency\": \"UGX\",\"amount\": 5000,\"status\": \"NEW\",\"message\": \"Happy birthdate Curtis!\",\"created\": \"2018-02-16T13:54:31.000Z\",\"updated\": \"2018-02-16T13:54:34.000Z\",\"meta\": {\"cardInfo\": {\"expDate\": \"04/22\",\"number\": \"6466\",\"name\": \"Emily Carter\",\"type\": \"Visa\"},\"recipientInfo\": {\"firstname\": \"Firstname\",\"lastname\": \"Lastname\",\"country\": \"Uganda\",\"phone\": \"+44 020 7234 34\",\"image_url\": \"example.com/wasm\"}}},{\"id\": 2,\"transaction_schedule_id\": 1,\"from\": \"5a5ca3375d82df077128a941\",\"to\": \"d71bd947-c9f9-451b-ab48-09abe1165154\",\"currency\": \"UGX\",\"amount\": 5000,\"status\": \"NEW\",\"message\": \"Happy birthdate Curtis!\",\"created\": \"2018-02-18T13:54:31.000Z\",\"updated\": \"2018-02-18T13:54:34.000Z\",\"meta\": {\"cardInfo\": {\"expDate\": \"04/22\",\"number\": \"6466\",\"name\": \"Emily Carter\",\"type\": \"Visa\"},\"recipientInfo\": {\"firstname\": \"Firstname2\",\"lastname\": \"Lastname\",\"country\": \"Uganda\",\"phone\": \"+44 020 7234 34\",\"image_url\": \"example.com/wasm\"}}},{\"id\": 2,\"transaction_schedule_id\": 1,\"from\": \"5a5ca3375d82df077128a941\",\"to\": \"d71bd947-c9f9-451b-ab48-09abe1165154\",\"currency\": \"UGX\",\"amount\": 5000,\"status\": \"NEW\",\"message\": \"Happy birthdate Curtis!\",\"created\": \"2018-02-18T13:54:31.000Z\",\"updated\": \"2018-02-18T13:54:34.000Z\",\"meta\": {\"cardInfo\": {\"expDate\": \"04/22\",\"number\": \"6466\",\"name\": \"Emily Carter\",\"type\": \"Visa\"},\"recipientInfo\": {\"firstname\": \"Firstname2\",\"lastname\": \"Lastname\",\"country\": \"Uganda\",\"phone\": \"+44 020 7234 34\",\"image_url\": \"example.com/wasm\"}}},{\"id\": 2,\"transaction_schedule_id\": 1,\"from\": \"5a5ca3375d82df077128a941\",\"to\": \"d71bd947-c9f9-451b-ab48-09abe1165154\",\"currency\": \"UGX\",\"amount\": 5000,\"status\": \"NEW\",\"message\": \"Happy birthdate Curtis!\",\"created\": \"2018-02-18T13:54:31.000Z\",\"updated\": \"2018-02-18T13:54:34.000Z\",\"meta\": {\"cardInfo\": {\"expDate\": \"04/22\",\"number\": \"6466\",\"name\": \"Emily Carter\",\"type\": \"Visa\"},\"recipientInfo\": {\"firstname\": \"Firstname2\",\"lastname\": \"Lastname\",\"country\": \"Uganda\",\"phone\": \"+44 020 7234 34\",\"image_url\": \"example.com/wasm\"}}}]}"
        val gson = Gson()
        val transactions: List<Transaction>? = gson.fromJson(str, Transactions::class.java).transactions
        val adapter = ActivityAllAdapter(context!!, transactions)
        adapter.setClickListener(this)
        relativeLayout.setHasFixedSize(true)
        relativeLayout.setLayoutManager(GridLayoutManager(context, 1, LinearLayoutManager.VERTICAL, false))
        relativeLayout.setAdapter(adapter)

    }

    override fun tagName(): String {
        return FragActivity::class.java.simpleName
    }
}
