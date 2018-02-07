package uk.co.transferx.app.mainscreen.adapters;

import android.support.v7.util.DiffUtil;

import java.util.List;

import uk.co.transferx.app.dto.RecipientDto;

/**
 * Created by sergey on 07/02/2018.
 */

public class RecipientDiffCallback extends DiffUtil.Callback {

    private List<RecipientDto> oldRecipientList;
    private List<RecipientDto> newRecipientList;


    public RecipientDiffCallback(List<RecipientDto> oldRecipientList, List<RecipientDto> newRecipientList) {
        this.oldRecipientList = oldRecipientList;
        this.newRecipientList = newRecipientList;
    }

    @Override
    public int getOldListSize() {
        return oldRecipientList.size();
    }

    @Override
    public int getNewListSize() {
        return newRecipientList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldRecipientList.get(oldItemPosition).getId().equals(newRecipientList.get(newItemPosition).getId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return newRecipientList.get(newItemPosition).equals(oldRecipientList.get(oldItemPosition));
    }
}
