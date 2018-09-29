package uk.co.transferx.app.ui.homescreen.adapters

import android.content.Context
import android.content.res.Resources
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.util.SparseBooleanArray
import android.util.SparseIntArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import uk.co.transferx.app.R
import uk.co.transferx.app.data.pojo.Transaction
import uk.co.transferx.app.ui.homescreen.presenters.FragActivityPresenter
import uk.co.transferx.app.ui.mainscreen.adapters.TransactionDiffCallback
import uk.co.transferx.app.ui.mainscreen.presenters.ActivityFragmentPresenter
import java.text.ParseException
import java.text.SimpleDateFormat


class ActivityAllAdapter(private val mContext: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var transactions: List<Transaction>? = null
    private var clickListener: ItemClickListener? = null
    private var headerText: String? = ""
    private var isRecurrent: Boolean = false;
    private var sparseArrayInteger : SparseIntArray? = null


    private val VIEW_WITH_HEADER: Int = 1;
    private val VIEW_ROW: Int = 2;

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        when (viewType) {
            VIEW_WITH_HEADER -> return ItemWithHeaderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_with_header_activity, parent, false))
            VIEW_ROW -> return ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_activity, parent, false))
        }
        return ItemWithHeaderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_with_header_activity, parent, false))
    }

    override fun getItemViewType(position: Int): Int {
        if(position == RecyclerView.NO_POSITION)
            return -1;
        if(sparseArrayInteger!![position]==VIEW_WITH_HEADER)
            return VIEW_WITH_HEADER

        if(sparseArrayInteger!![position]==VIEW_ROW)
            return VIEW_ROW

        if (isRecurrent) {

            if (!((transactions?.get(position)?.frequency)
                            ?: transactions?.get(position)?.status).equals(headerText)) {
                headerText = ((transactions?.get(position)?.frequency)
                        ?: transactions?.get(position)?.status)
                sparseArrayInteger!!.put(position, VIEW_WITH_HEADER);
                return VIEW_WITH_HEADER
            } else {
                sparseArrayInteger!!.put(position, VIEW_ROW);
                return VIEW_ROW
            }
        } else {
            if (!(getPrettyDate(transactions?.get(position)?.created).equals(headerText))) {
                headerText = getPrettyDate(transactions?.get(position)?.created)
                sparseArrayInteger!!.put(position, VIEW_WITH_HEADER);
                return VIEW_ROW
                return VIEW_WITH_HEADER
            } else {
                sparseArrayInteger!!.put(position, VIEW_ROW);

                return VIEW_ROW
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return position as Long
    }

    override fun onBindViewHolder(vholder: RecyclerView.ViewHolder, position: Int) {

        when (vholder.itemViewType) {
            VIEW_WITH_HEADER -> {
                 var holder : ItemWithHeaderViewHolder = vholder as ItemWithHeaderViewHolder
                if (isRecurrent) {
                    holder.date.text = ((transactions?.get(position)?.frequency) ?: transactions?.get(position)?.status)
                    holder.name.setText( mContext.resources.getString(R.string.recurrent))
                } else {

                    holder.date.text = getPrettyDate(transactions?.get(position)?.created)
                    holder.name.setText(transactions?.get(position)?.meta?.recipientInfo?.firstName)

                }
                holder.amount.setText(transactions?.get(position)?.currency + " "  + transactions?.get(position)?.amount)
            }
            VIEW_ROW -> {

                var holder : ItemViewHolder = vholder as ItemViewHolder
                if (isRecurrent) {
                    holder.name.setText(mContext.resources.getString(R.string.recurrent))
                } else {
                    holder.name.setText(transactions?.get(position)?.meta?.recipientInfo?.firstName)
                }
                holder.amount.setText(transactions?.get(position)?.currency + " "  + transactions?.get(position)?.amount)
            }
        }
    }

    fun getPrettyDate(date: String?): String {
        var dateString = date!!.split("T")[0]
        try {

            val sdf = SimpleDateFormat("yyyy-MM-dd")
            val date = sdf.parse(dateString)

            val outputFormat = SimpleDateFormat("MMM yyyy")
            val formattedDate = outputFormat.format(date)

            return formattedDate;
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return date;
    }

    fun setRecurrentType(isRecurrent: Boolean) {
        this.isRecurrent = isRecurrent;
        headerText = "";

    }

    override fun getItemCount(): Int {
        return transactions?.size ?: 0
    }

    fun setClickListener(clickListener: ItemClickListener) {
        this.clickListener = clickListener
    }

    fun setAllTransactions(transactions: List<Transaction>) {
        sparseArrayInteger = SparseIntArray(transactions.size)

        this.transactions = transactions
            notifyDataSetChanged()
    }

    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        var name: TextView
        var amount: TextView
        var userImg: ImageView
        var indicator: ImageView
        var divider: View

        init {

            this.name = view.findViewById(R.id.tvName)
            this.amount = view.findViewById(R.id.tvAmount)
            this.userImg = view.findViewById(R.id.imgUser)
            this.indicator = view.findViewById(R.id.imgIndicator)
            this.divider = view.findViewById(R.id.viewDivider)

            view.setOnClickListener { v -> if (clickListener != null) clickListener!!.onItemClick(v, transactions!![adapterPosition]) }
        }

        override fun onClick(v: View) {
            if (clickListener != null) clickListener!!.onItemClick(v, transactions!![adapterPosition])
        }
    }

    inner class ItemWithHeaderViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        var name: TextView
        var amount: TextView
        var userImg: ImageView
        var indicator: ImageView
        var headerView: LinearLayout
        var date: TextView

        init {

            this.name = view.findViewById(R.id.tvName)
            this.amount = view.findViewById(R.id.tvAmount)
            this.userImg = view.findViewById(R.id.imgUser)
            this.indicator = view.findViewById(R.id.imgIndicator)
            this.headerView = view.findViewById(R.id.llHeader)
            this.date = view.findViewById(R.id.tvDate)

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