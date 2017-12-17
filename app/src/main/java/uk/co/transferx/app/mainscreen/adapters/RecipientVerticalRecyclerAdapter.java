package uk.co.transferx.app.mainscreen.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import uk.co.transferx.app.dto.RecipientDto;

/**
 * Created by sergey on 17.12.17.
 */

public class RecipientVerticalRecyclerAdapter extends RecyclerView.Adapter {

    private List<RecipientDto> recipientDtoList;
    private final Context context;

    public RecipientVerticalRecyclerAdapter(Context context) {
        this.context = context;
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
        return recipientDtoList == null ? 0 : recipientDtoList.size();
    }

    public void setRecipients(List<RecipientDto> recipients){

    }


    static class RecipientHorizontalHolder extends RecyclerView.ViewHolder {


        RecipientHorizontalHolder(View itemView) {
            super(itemView);

        }


    }

}
