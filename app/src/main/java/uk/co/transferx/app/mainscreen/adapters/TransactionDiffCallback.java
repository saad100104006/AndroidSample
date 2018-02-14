package uk.co.transferx.app.mainscreen.adapters;

import android.support.v7.util.DiffUtil;

import java.util.List;

import uk.co.transferx.app.dto.TransactionDto;

/**
 * Created by sergey on 07/02/2018.
 */

public class TransactionDiffCallback extends DiffUtil.Callback {

    private List<TransactionDto> oldTransactionDto;
    private List<TransactionDto> newTransactionDto;


    public TransactionDiffCallback(List<TransactionDto> oldTransactionDto, List<TransactionDto> newTransactionDto) {
        this.oldTransactionDto = oldTransactionDto;
        this.newTransactionDto = newTransactionDto;
    }

    @Override
    public int getOldListSize() {
        return oldTransactionDto.size();
    }

    @Override
    public int getNewListSize() {
        return newTransactionDto.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldTransactionDto.get(oldItemPosition).getId() == newTransactionDto.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldTransactionDto.get(oldItemPosition).equals(newTransactionDto.get(newItemPosition));
    }
}
