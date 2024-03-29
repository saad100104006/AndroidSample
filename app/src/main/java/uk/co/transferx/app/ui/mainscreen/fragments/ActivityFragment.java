package uk.co.transferx.app.ui.mainscreen.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import uk.co.transferx.app.ui.base.BaseFragment;
import uk.co.transferx.app.R;
import uk.co.transferx.app.TransferXApplication;
import uk.co.transferx.app.ui.mainscreen.adapters.ActivityRecylerViewAdapter;
import uk.co.transferx.app.ui.mainscreen.presenters.ActivityFragmentPresenter;
import uk.co.transferx.app.data.pojo.Transaction;
import uk.co.transferx.app.data.pojo.TransactionCreate;
import uk.co.transferx.app.ui.transfersummary.TransferSummaryActivity;
import uk.co.transferx.app.ui.signin.SignInActivity;

import static uk.co.transferx.app.util.Constants.TRANSACTION;

/**
 * Created by sergey on 17.12.17.
 */

public class ActivityFragment extends BaseFragment implements ActivityFragmentPresenter.ActivityFragmentUI {


    @Inject
    ActivityFragmentPresenter presenter;
    private ActivityRecylerViewAdapter adapter;
    private RecyclerView historyView;
    private LinearLayout emptyDescription;
    private View view;

    @Override
    public String tagName() {
        return ActivityFragment.class.getSimpleName();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        ((TransferXApplication) getActivity().getApplication()).getAppComponent().inject(this);
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        historyView = view.findViewById(R.id.activity_recycler_view);
        emptyDescription = view.findViewById(R.id.empty_description);
        view.findViewById(R.id.title).setVisibility(View.VISIBLE);
        historyView.setHasFixedSize(true);
        adapter = new ActivityRecylerViewAdapter(getContext(), presenter);
        historyView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.attachUI(this);
    }

    @Override
    public void onPause() {
        presenter.detachUI();
        super.onPause();
    }

    @Override
    public void setData(List<Transaction> transactions) {
        boolean isEmpty = transactions.isEmpty();
        historyView.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        emptyDescription.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        adapter.setRecipients(transactions);
    }

    @Override
    public void goToRecieptScreen(TransactionCreate transaction) {
        final Intent intent = new Intent(getContext(), TransferSummaryActivity.class);
        intent.putExtra(TRANSACTION, transaction);
        startActivity(intent);
    }

    @Override
    public void goToWelcome() {
        SignInActivity.Companion.startSignInActivity(getActivity());
    }

    @Override
    public void setError() {
        Toast.makeText(getContext(), "Error ", Toast.LENGTH_SHORT).show();
    }
}
