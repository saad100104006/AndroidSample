package uk.co.transferx.app.ui.homescreen.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import uk.co.transferx.app.R;
import uk.co.transferx.app.data.pojo.Transaction;

public class ActivityAllAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Transaction> transactions;
    private Context mContext;
    private ItemClickListener mClickListener;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public ActivityAllAdapter(Context context, ArrayList<Transaction> transactions) {
        this.transactions = transactions;
        this.mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activity, parent, false);
            return new ItemViewHolder(itemView);
        } else if (viewType == TYPE_HEADER) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header_divider, parent, false);
            return new HeaderViewHolder(itemView);
        } else
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemViewType(int position) {

        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return (null != transactions ? transactions.size() : 0);
    }

    public void setClickListener(ItemClickListener clickListener) {
        this.mClickListener = clickListener;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        protected TextView name, amount;
        protected ImageView userImg, indicator;

        public ItemViewHolder(View view) {
            super(view);

            this.name = view.findViewById(R.id.tv_name);
            this.amount = view.findViewById(R.id.tv_amount);
            this.userImg = view.findViewById(R.id.img_user);
            this.indicator = view.findViewById(R.id.img_indicator);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mClickListener != null) mClickListener.onItemClick(v, transactions.get(getAdapterPosition()));
                }
            });
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) mClickListener.onItemClick(v, transactions.get(getAdapterPosition()));
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        protected TextView header;

        public HeaderViewHolder(View view) {
            super(view);
            this.header = view.findViewById(R.id.tv_header);
        }
    }

    public interface ItemClickListener {
        void onItemClick(View view, Transaction data);
    }
}