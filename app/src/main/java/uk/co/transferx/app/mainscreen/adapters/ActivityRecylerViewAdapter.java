package uk.co.transferx.app.mainscreen.adapters;

import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import uk.co.transferx.app.dto.RecipientDto;

/**
 * Created by sergey on 02/02/2018.
 */

public class ActivityRecylerViewAdapter extends RecyclerView.Adapter {

    private List<RecipientDto> recipientDtos;


    private interface ItemClickListener {

        void onClickItem(final RecipientDto recipientDto);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return recipientDtos == null ? 0 : recipientDtos.size();
    }

    public void setRecipients(List<RecipientDto> recipients) {
        if (this.recipientDtos == null) {
            this.recipientDtos = recipients;
            notifyDataSetChanged();
            return;
        }
        if (recipients.isEmpty()) {
            return;
        }
        final RecipientDiffCallback recipientDiffCallback = new RecipientDiffCallback(this.recipientDtos, recipients);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(recipientDiffCallback);
        this.recipientDtos.clear();
        this.recipientDtos.addAll(recipients);
        diffResult.dispatchUpdatesTo(this);
    }


    class ActivityRecylerlHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ActivityRecylerViewAdapter.ItemClickListener itemClickListener;

        ActivityRecylerlHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) {
                itemClickListener.onClickItem(recipientDtos.get(getAdapterPosition()));
            }
        }

    }
}
