package uk.co.transferx.app.ui.homescreen.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import uk.co.transferx.app.R;
import uk.co.transferx.app.data.pojo.Transaction;

public class ActivityAllAdapter extends RecyclerView.Adapter<ActivityAllAdapter.SingleItemRowHolder> {

    private ArrayList<Transaction> transactions;
    private Context mContext;
    private ItemClickListener mClickListener;

    public ActivityAllAdapter(Context context, ArrayList<Transaction> transactions) {
        this.transactions = transactions;
        this.mContext = context;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_activity, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, int i) {

    }

    @Override
    public int getItemCount() {
        return (null != transactions ? transactions.size() : 0);
    }

    public void setClickListener(ItemClickListener clickListener) {
        this.mClickListener = clickListener;
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        protected TextView name, amount;
        protected ImageView indicator;

        public SingleItemRowHolder(View view) {
            super(view);

            this.name = view.findViewById(R.id.tv_name);
            this.amount = view.findViewById(R.id.tv_amount);
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

    public interface ItemClickListener {
        void onItemClick(View view, Transaction data);
    }
}