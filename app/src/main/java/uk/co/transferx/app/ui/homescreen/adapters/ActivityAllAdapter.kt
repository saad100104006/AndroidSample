package uk.co.transferx.app.ui.homescreen.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import java.util.ArrayList

import uk.co.transferx.app.R
import uk.co.transferx.app.data.pojo.Transaction

class ActivityAllAdapter(private val mContext: Context, private val transactions: ArrayList<Transaction>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var clickListener: ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TYPE_ITEM) {
            return ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_activity, parent, false))
        } else {
            return HeaderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_header_divider, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }

    override fun getItemViewType(position: Int): Int {

        return TYPE_ITEM
    }

    override fun getItemCount(): Int {
        return transactions?.size ?: 0
    }

    fun setClickListener(clickListener: ItemClickListener) {
        this.clickListener = clickListener
    }

    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        protected var name: TextView
        protected var amount: TextView
        protected var userImg: ImageView
        protected var indicator: ImageView

        init {

            this.name = view.findViewById(R.id.tvName)
            this.amount = view.findViewById(R.id.tvAmount)
            this.userImg = view.findViewById(R.id.imgUser)
            this.indicator = view.findViewById(R.id.imgIndicator)

            view.setOnClickListener { v -> if (clickListener != null) clickListener!!.onItemClick(v, transactions!![adapterPosition]) }
        }

        override fun onClick(v: View) {
            if (clickListener != null) clickListener!!.onItemClick(v, transactions!![adapterPosition])
        }
    }

    inner class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        protected var header: TextView

        init {
            this.header = view.findViewById(R.id.tvDate)
        }
    }

    interface ItemClickListener {
        fun onItemClick(view: View, data: Transaction)
    }

    companion object {

        private val TYPE_HEADER = 0
        private val TYPE_ITEM = 1
    }
}