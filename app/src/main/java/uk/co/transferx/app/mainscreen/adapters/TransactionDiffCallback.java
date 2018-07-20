package uk.co.transferx.app.mainscreen.adapters;

import android.support.v7.util.DiffUtil;

import java.util.List;

import uk.co.transferx.app.dto.TransactionDto;
import uk.co.transferx.app.pojo.Transaction;

/**
 * Created by sergey on 07/02/2018.
 */

public class TransactionDiffCallback extends DiffUtil.Callback {

    private List<Transaction> oldTransactions;
    private List<Transaction> newTransactions;


    public TransactionDiffCallback(List<Transaction> oldTransactions, List<Transaction> newTransactions) {
        this.oldTransactions = oldTransactions;
        this.newTransactions = newTransactions;
    }

    @Override
    public int getOldListSize() {
        return oldTransactions.size();
    }

    @Override
    public int getNewListSize() {
        return newTransactions.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldTransactions.get(oldItemPosition).getId().equals(newTransactions.get(newItemPosition).getId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldTransactions.get(oldItemPosition).equals(newTransactions.get(newItemPosition));
    }
}
