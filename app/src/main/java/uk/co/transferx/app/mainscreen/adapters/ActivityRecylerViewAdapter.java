package uk.co.transferx.app.mainscreen.adapters;

import android.content.Context;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import uk.co.transferx.app.R;
import uk.co.transferx.app.mainscreen.presenters.ActivityFragmentPresenter;
import uk.co.transferx.app.pojo.Transaction;

/**
 * Created by sergey on 02/02/2018.
 */

public class ActivityRecylerViewAdapter extends RecyclerView.Adapter<ActivityRecylerViewAdapter.ActivityRecyclerHolder> {


    private List<Transaction> transactions;
    private Context context;
    private final ActivityFragmentPresenter presenter;

    private interface ItemClickListener {

        void onClickItem(final Transaction transaction);
    }

    public ActivityRecylerViewAdapter(Context context, ActivityFragmentPresenter presenter) {
        this.context = context;
        this.presenter = presenter;
    }

    @Override
    public ActivityRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ActivityRecyclerHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item, parent, false));

    }

    @Override
    public void onBindViewHolder(ActivityRecyclerHolder holder, int position) {
        final Transaction transaction = transactions.get(position);
        holder.amount.setText(context.getString(R.string.amount_with_currency, transaction.getAmount(), transaction.getCurrency()));
        if (transaction.getMeta().getRecipientInfo() != null) {
            holder.name.setText(String.format("%s %s", transaction.getMeta().getRecipientInfo().getFirstName(),
                    transaction.getMeta().getRecipientInfo().getLastName()));
        } else {
            holder.name.setText("");
        }
        holder.amount.setCompoundDrawablesWithIntrinsicBounds(0, 0, resolveStatus(transaction.getStatus()), 0);
        holder.itemClickListener = presenter::goToReceipt;

    }

    private int resolveStatus(String status){
        switch (status){
            case "PAYIN-SUCCESS": return R.drawable.ic_complete;
            case "PAYIN-FAILED" : return R.drawable.ic_error_red;
            default: return R.drawable.ic_in_progress;

        }
    }

    @Override
    public int getItemCount() {
        return transactions == null ? 0 : transactions.size();
    }

    public void setRecipients(List<Transaction> transactions) {
        if (this.transactions == null) {
            this.transactions = transactions;
            notifyDataSetChanged();
            return;
        }
        if (transactions.isEmpty()) {
            return;
        }
        final TransactionDiffCallback transactionDiffCallback = new TransactionDiffCallback(this.transactions, transactions);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(transactionDiffCallback);
        this.transactions.clear();
        this.transactions.addAll(transactions);
        diffResult.dispatchUpdatesTo(this);
    }


    class ActivityRecyclerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name, amount;
        ActivityRecylerViewAdapter.ItemClickListener itemClickListener;

        ActivityRecyclerHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            name = itemView.findViewById(R.id.name_recipient);
            amount = itemView.findViewById(R.id.amount_recipient);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) {
                itemClickListener.onClickItem(transactions.get(getAdapterPosition()));
            }
        }

    }
}
