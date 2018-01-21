package uk.co.transferx.app.mainscreen.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import uk.co.transferx.app.R;
import uk.co.transferx.app.dto.RecipientDto;

import static uk.co.transferx.app.mainscreen.fragments.SendFragment.RECIPIENT;
import static uk.co.transferx.app.mainscreen.fragments.SendFragment.REQUEST_RECIPIENT;

/**
 * Created by sergey on 15/01/2018.
 */

public class RecipientDialogAdapter extends RecyclerView.Adapter<RecipientDialogAdapter.RecipientDialogHolder> {

    private List<RecipientDto> recipientDtoList;
    private final Fragment fragment;

    private interface ItemClickListener {

        void onClickItem(final RecipientDto recipientDto);
    }


    public RecipientDialogAdapter(Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public RecipientDialogHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecipientDialogAdapter.RecipientDialogHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recipient_item, parent, false));

    }

    @Override
    public void onBindViewHolder(RecipientDialogHolder holder, int position) {
        RecipientDto recipientDto = recipientDtoList.get(position);
        holder.recipientName.setText(recipientDto.getName());
        holder.recipientCountry.setText(recipientDto.getCountry());
        holder.itemClickListener = recip -> {
            Intent data = new Intent();
            data.putExtra(RECIPIENT, recip);
            fragment.getTargetFragment().onActivityResult(REQUEST_RECIPIENT, Activity.RESULT_OK, data);
        };

    }


    public void setRecipients(List<RecipientDto> recipients) {
        this.recipientDtoList = recipients;
        Log.d("Sergey", "recipients size " + recipients.size());
//        if (recipients.isEmpty()) {
//            return;
//        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return recipientDtoList == null ? 0 : recipientDtoList.size();
    }

    class RecipientDialogHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView recipientPhoto;
        TextView recipientName, recipientCountry;
        ItemClickListener itemClickListener;

        RecipientDialogHolder(View itemView) {
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
