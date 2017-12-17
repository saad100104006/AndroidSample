package uk.co.transferx.app.mainscreen.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import uk.co.transferx.app.R;
import uk.co.transferx.app.dto.RecipientDto;

/**
 * Created by sergey on 17.12.17.
 */

public class RecipientVerticalRecyclerAdapter extends RecyclerView.Adapter<RecipientVerticalRecyclerAdapter.RecipientVerticalHolder> {

    private List<RecipientDto> recipientDtoList;
    private final Context context;

    private interface ItemClickListener {

        void onClickItem(RecipientDto recipientDto);
    }

    public RecipientVerticalRecyclerAdapter(Context context) {
        this.context = context;
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

    }

    @Override
    public int getItemCount() {
        return recipientDtoList == null ? 0 : recipientDtoList.size();
    }

    public void setRecipients(List<RecipientDto> recipients) {
        this.recipientDtoList = recipients;
        notifyDataSetChanged();

    }


    static class RecipientVerticalHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView recipientPhoto;
        TextView recipientName, recipientCountry;
        RecipientDto recipientDto;
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
                itemClickListener.onClickItem(recipientDto);
            }
        }


    }

}
