package uk.co.transferx.app.ui.transfer.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.SparseIntArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import uk.co.transferx.app.R
import uk.co.transferx.app.data.pojo.Recipient

class RecipientsAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var recipients: List<Recipient>? = null
    private var clickListener: ItemClickListener? = null
    private var headerLetter: String? = ""
    private var sparseArrayInteger: SparseIntArray? = null
    private val VIEW_WITH_HEADER: Int = 1
    private val VIEW_ROW: Int = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            VIEW_WITH_HEADER -> return ItemWithHeaderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_with_header_recipients, parent, false))
            VIEW_ROW -> return ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_recipient, parent, false))
        }
        return ItemWithHeaderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_with_header_recipients, parent, false))
    }

    override fun getItemViewType(position: Int): Int {
        if (position == RecyclerView.NO_POSITION)
            return -1
        if (sparseArrayInteger!![position] == VIEW_WITH_HEADER)
            return VIEW_WITH_HEADER

        if (sparseArrayInteger!![position] == VIEW_ROW)
            return VIEW_ROW

        // If current header letter is different from required header letter
        if (!recipients?.get(position)?.firstname?.take(1).equals(headerLetter, true)) {
            // Update header letter
            headerLetter = recipients?.get(position)?.firstname?.take(1)
            sparseArrayInteger!!.put(position, VIEW_WITH_HEADER)
            return VIEW_WITH_HEADER
        } else {
            // Then we need classic row item
            sparseArrayInteger!!.put(position, VIEW_ROW)
            return VIEW_ROW
        }
    }

    override fun onBindViewHolder(vholder: RecyclerView.ViewHolder, position: Int) {
        val name = recipients?.get(position)?.firstname + recipients?.get(position)?.lastname
        when (vholder.itemViewType) {
            VIEW_WITH_HEADER -> {
                val holder: ItemWithHeaderViewHolder = vholder as ItemWithHeaderViewHolder
                holder.initialLetter.text = name[0].toString()
                holder.name.text = name
            }
            VIEW_ROW -> {
                val holder: ItemViewHolder = vholder as ItemViewHolder
                holder.name.text = name
            }
        }
    }

    override fun getItemCount(): Int {
        return recipients?.size ?: 0
    }

    fun setRecipients(recipients: List<Recipient>) {
        sparseArrayInteger = SparseIntArray(recipients.size)

        this.recipients = recipients
        notifyDataSetChanged()
    }

    fun setClickListener(clickListener: ItemClickListener) {
        this.clickListener = clickListener
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        var name: TextView
        var userImg: ImageView
        var divider: View

        init {
            this.name = view.findViewById(R.id.tvName)
            this.userImg = view.findViewById(R.id.imgUser)
            this.divider = view.findViewById(R.id.viewDivider)

            view.setOnClickListener { v ->  clickListener?.onItemClick(v, recipients!![adapterPosition]) }
        }

        override fun onClick(v: View) {
            clickListener?.onItemClick(v, recipients!![adapterPosition])
        }
    }

    inner class ItemWithHeaderViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        var name: TextView
        var userImg: ImageView
        var headerView: LinearLayout
        var initialLetter: TextView

        init {
            this.name = view.findViewById(R.id.tvName)
            this.userImg = view.findViewById(R.id.imgUser)
            this.headerView = view.findViewById(R.id.llHeader)
            this.initialLetter = view.findViewById(R.id.tvInitialLetter)

            view.setOnClickListener { v ->  clickListener?.onItemClick(v, recipients!![adapterPosition]) }
        }

        override fun onClick(v: View) {
            clickListener?.onItemClick(v, recipients!![adapterPosition])
        }
    }

    interface ItemClickListener {
        fun onItemClick(view: View, data: Recipient)
    }

}