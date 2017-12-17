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

public class RecipientHorizontalRecyclerAdapter extends RecyclerView.Adapter<RecipientHorizontalRecyclerAdapter.RecipientHorizontalHolder> {


    private List<RecipientDto> recipientDtoList;
    private final Context context;

    private interface ItemClickListener {

        void onClickItem(RecipientDto recipientDto);
    }

    public RecipientHorizontalRecyclerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecipientHorizontalHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecipientHorizontalHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recipient_favourite_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecipientHorizontalHolder holder, int position) {
        final RecipientDto recipientDto = recipientDtoList.get(position);
        holder.recipientName.setText(recipientDto.getFavoriteName());
       /* GlideApp.with(context)
                .load(recipientDto.getImgUrl())
                .transform(new CircleCrop())
                .into(holder.recipientPhoto); */


    }

    @Override
    public int getItemCount() {
        return recipientDtoList == null ? 0 : recipientDtoList.size();
    }


    public void setRecipients(List<RecipientDto> recipients) {
        this.recipientDtoList = recipients;
        notifyDataSetChanged();

    }


    static class RecipientHorizontalHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView recipientPhoto;
        TextView recipientName;
        RecipientDto recipientDto;
        ItemClickListener itemClickListener;


        RecipientHorizontalHolder(View itemView) {
            super(itemView);
            recipientPhoto = itemView.findViewById(R.id.favourite_recipient_img);
            recipientName = itemView.findViewById(R.id.favourite_recipient_name);
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
