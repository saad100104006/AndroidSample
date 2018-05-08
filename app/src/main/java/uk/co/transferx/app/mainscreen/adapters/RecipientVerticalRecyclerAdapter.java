package uk.co.transferx.app.mainscreen.adapters;

import android.content.Context;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import uk.co.transferx.app.R;
import uk.co.transferx.app.dto.RecipientDto;
import uk.co.transferx.app.mainscreen.presenters.RecipientsFragmentPresenter;

/**
 * Created by sergey on 17.12.17.
 */

public class RecipientVerticalRecyclerAdapter extends RecyclerView.Adapter {

    private List<RecipientDto> recipientDtoList;
    private final Fragment fragment;
    private final RecipientsFragmentPresenter presenter;
    private final Vibrator vibe;
    private final static int TIME_OF_VIBRATION = 50;
    private final static int FOOTER_VIEW = 111;
    private final static int NORMAL_VIEW = 222;

    private interface ItemClickListener {

        void onClickItem(final RecipientDto recipientDto);
    }

    public RecipientVerticalRecyclerAdapter(Fragment fragment, RecipientsFragmentPresenter presenter) {
        this.fragment = fragment;
        this.presenter = presenter;
        vibe = (Vibrator) fragment.getContext().getSystemService(Context.VIBRATOR_SERVICE);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == NORMAL_VIEW) {
            return new RecipientVerticalHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recipient_item, parent, false));
        } else if (viewType == FOOTER_VIEW) {
            return new RecipientFooter(LayoutInflater.from(parent.getContext()).inflate(R.layout.recipient_footer, parent, false));
        }
        return null;
    }

    public RecipientDto getRecipient(int position) {
        return recipientDtoList.get(position);
    }

    private int getPositionOfRecipient(RecipientDto recipientDto) {
        return recipientDtoList.indexOf(recipientDto);
    }

    public void removeItem(int position) {
        recipientDtoList.remove(position);
        notifyItemRemoved(position);
    }

    public void removeItem(RecipientDto recipientDto) {
        final int pos = getPositionOfRecipient(recipientDto);
        recipientDtoList.remove(pos);
        notifyItemRemoved(pos);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RecipientVerticalHolder) {
            RecipientVerticalHolder recipientVerticalHolder = (RecipientVerticalHolder) holder;
            RecipientDto recipientDto = recipientDtoList.get(position);
            recipientVerticalHolder.recipientName.setText(recipientDto.getName());
        }
        if (holder instanceof RecipientFooter) {
            ((RecipientFooter) holder).addButton.setOnClickListener(v -> presenter.addRecipient());
        }
    }

    @Override
    public int getItemCount() {
        return recipientDtoList == null ? 0 : recipientDtoList.size() + 1;
    }

    public void setRecipients(List<RecipientDto> recipients) {
        if (this.recipientDtoList == null) {
            this.recipientDtoList = recipients;
            notifyDataSetChanged();
            return;
        }
        if (recipients.isEmpty()) {
            return;
        }
        final RecipientDiffCallback recipientDiffCallback = new RecipientDiffCallback(this.recipientDtoList, recipients);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(recipientDiffCallback);
        this.recipientDtoList.clear();
        this.recipientDtoList.addAll(recipients);
        diffResult.dispatchUpdatesTo(this);

    }

    class RecipientFooter extends RecyclerView.ViewHolder {

        Button addButton;

        RecipientFooter(View itemView) {
            super(itemView);
            addButton = itemView.findViewById(R.id.add_button);
        }

    }

    class RecipientVerticalHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        ImageView recipientPhoto;
        TextView recipientName, recipientCountry;
        ItemClickListener itemClickListener;

        RecipientVerticalHolder(View itemView) {
            super(itemView);
            recipientPhoto = itemView.findViewById(R.id.recipient_img);
            recipientName = itemView.findViewById(R.id.name_recipient);
            //   recipientCountry = itemView.findViewById(R.id.recipient_country);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) {
                itemClickListener.onClickItem(recipientDtoList.get(getAdapterPosition()));
            }
        }

        @Override
        public boolean onLongClick(View v) {
            vibe.vibrate(TIME_OF_VIBRATION);
            presenter.putToFavorite(recipientDtoList.get(getAdapterPosition()));
            return true;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (recipientDtoList != null && position == recipientDtoList.size()) {
            return FOOTER_VIEW;
        }
        return NORMAL_VIEW;
    }

}
