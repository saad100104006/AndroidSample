package uk.co.transferx.app.ui.homescreen.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import uk.co.transferx.app.R
import uk.co.transferx.app.data.pojo.Transaction


class ActivityAllAdapter(private val mContext: Context, private val transactions: List<Transaction>?) : RecyclerView.Adapter<ActivityAllAdapter.ItemViewHolder>() {

    private var clickListener: ItemClickListener? = null
    private var flagDate: String? = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityAllAdapter.ItemViewHolder {
        return ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_activity, parent, false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        if (!transactions?.get(position)?.created.equals(flagDate)) {
            flagDate = transactions?.get(position)?.created
            holder.headerView.visibility = View.VISIBLE
            holder.date.setText(transactions?.get(position)?.created)
            holder.divider.visibility = View.GONE
        }
        else {
            holder.headerView.visibility = View.GONE
            holder.divider.visibility = View.VISIBLE
        }
        holder.name.setText(transactions?.get(position)?.meta?.recipientInfo?.firstName)
        holder.amount.setText(transactions?.get(position)?.amount)
    }

    override fun getItemCount(): Int {
        return transactions?.size ?: 0
    }

    fun setClickListener(clickListener: ItemClickListener) {
        this.clickListener = clickListener
    }

    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        var name: TextView
        var amount: TextView
        var userImg: ImageView
        var indicator: ImageView
        var headerView: LinearLayout
        var date: TextView
        var divider: View

        init {

            this.name = view.findViewById(R.id.tvName)
            this.amount = view.findViewById(R.id.tvAmount)
            this.userImg = view.findViewById(R.id.imgUser)
            this.indicator = view.findViewById(R.id.imgIndicator)
            this.headerView = view.findViewById(R.id.llHeader)
            this.date = view.findViewById(R.id.tvDate)
            this.divider = view.findViewById(R.id.viewDivider)

            view.setOnClickListener { v -> if (clickListener != null) clickListener!!.onItemClick(v, transactions!![adapterPosition]) }
        }

        override fun onClick(v: View) {
            if (clickListener != null) clickListener!!.onItemClick(v, transactions!![adapterPosition])
        }
    }

    interface ItemClickListener {
        fun onItemClick(view: View, data: Transaction)
    }
}