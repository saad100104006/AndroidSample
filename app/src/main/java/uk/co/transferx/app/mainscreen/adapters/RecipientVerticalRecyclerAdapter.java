package uk.co.transferx.app.mainscreen.adapters;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import uk.co.transferx.app.R;
import uk.co.transferx.app.dto.RecipientDto;
import uk.co.transferx.app.recipients.detailsrecipient.RecipientDetailsActivity;

import static uk.co.transferx.app.mainscreen.fragments.RecipientsFragment.ADD_CHANGE_RECIPIENT;
import static uk.co.transferx.app.recipients.detailsrecipient.RecipientDetailsActivity.RECIPIENT;

/**
 * Created by sergey on 17.12.17.
 */

public class RecipientVerticalRecyclerAdapter extends RecyclerView.Adapter<RecipientVerticalRecyclerAdapter.RecipientVerticalHolder> {

    private List<RecipientDto> recipientDtoList;
    private final Fragment fragment;

    private interface ItemClickListener {

        void onClickItem(final RecipientDto recipientDto);
    }

    public RecipientVerticalRecyclerAdapter(Fragment fragment) {
        this.fragment = fragment;
    }


    @Override
    public RecipientVerticalHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecipientVerticalHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recipient_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecipientVerticalHolder holder, int position) {
        RecipientDto recipientDto = recipientDtoList.get(position);
        holder.recipientName.setText(recipientDto.getName());
        holder.recipientCountry.setText(recipientDto.getCountry());
        holder.itemClickListener = recip -> {
            Intent intent = new Intent(fragment.getActivity(), RecipientDetailsActivity.class);
            intent.putExtra(RECIPIENT, recip);
            fragment.startActivityForResult(intent, ADD_CHANGE_RECIPIENT);
        };

    }

    @Override
    public int getItemCount() {
        return recipientDtoList == null ? 0 : recipientDtoList.size();
    }

    public void setRecipients(List<RecipientDto> recipients) {
        this.recipientDtoList = recipients;
        if (recipients.isEmpty()) {
            return;
        }
        notifyDataSetChanged();

    }

    public class RecipientVerticalHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView recipientPhoto;
        TextView recipientName, recipientCountry;
        ItemClickListener itemClickListener;

        RecipientVerticalHolder(View itemView) {
            super(itemView);
            recipientPhoto = itemView.findViewById(R.id.recipient_img);
            recipientName = itemView.findViewById(R.id.recipient_name);
            recipientCountry = itemView.findViewById(R.id.recipient_country);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) {
                itemClickListener.onClickItem(recipientDtoList.get(getAdapterPosition()));
            }
        }


    }

}
