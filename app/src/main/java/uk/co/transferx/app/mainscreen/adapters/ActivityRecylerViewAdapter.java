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
import uk.co.transferx.app.dto.TransactionDto;

/**
 * Created by sergey on 02/02/2018.
 */

public class ActivityRecylerViewAdapter extends RecyclerView.Adapter<ActivityRecylerViewAdapter.ActivityRecyclerHolder> {


    private List<TransactionDto> transactionDtos;
    private Context context;


    private interface ItemClickListener {

        void onClickItem(final TransactionDto transactionDto);
    }

    public ActivityRecylerViewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ActivityRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ActivityRecyclerHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item, parent, false));

    }

    @Override
    public void onBindViewHolder(ActivityRecyclerHolder holder, int position) {
        final TransactionDto transactionDto = transactionDtos.get(position);
        holder.amount.setText(context.getString(R.string.amount_with_currency, transactionDto.getAmount().toPlainString(), transactionDto.getCurrency()));
        holder.name.setText(transactionDto.getRecipientName());
        holder.data.setText("data");

    }

    @Override
    public int getItemCount() {
        return transactionDtos == null ? 0 : transactionDtos.size();
    }

    public void setRecipients(List<TransactionDto> transactionDtos) {
        if (this.transactionDtos == null) {
            this.transactionDtos = transactionDtos;
            notifyDataSetChanged();
            return;
        }
        if (transactionDtos.isEmpty()) {
            return;
        }
        final TransactionDiffCallback transactionDiffCallback = new TransactionDiffCallback(this.transactionDtos, transactionDtos);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(transactionDiffCallback);
        this.transactionDtos.clear();
        this.transactionDtos.addAll(transactionDtos);
        diffResult.dispatchUpdatesTo(this);
    }


    class ActivityRecyclerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView data, name, amount;
        ActivityRecylerViewAdapter.ItemClickListener itemClickListener;

        ActivityRecyclerHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            data = itemView.findViewById(R.id.data);
            name = itemView.findViewById(R.id.recipient_name);
            amount = itemView.findViewById(R.id.amount);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) {
                itemClickListener.onClickItem(transactionDtos.get(getAdapterPosition()));
            }
        }

    }
}
