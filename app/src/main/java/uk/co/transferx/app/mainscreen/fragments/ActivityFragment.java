package uk.co.transferx.app.mainscreen.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import uk.co.transferx.app.BaseFragment;
import uk.co.transferx.app.R;
import uk.co.transferx.app.TransferXApplication;
import uk.co.transferx.app.dto.TransactionDto;
import uk.co.transferx.app.mainscreen.adapters.ActivityRecylerViewAdapter;
import uk.co.transferx.app.mainscreen.presenters.ActivityFragmentPresenter;

/**
 * Created by sergey on 17.12.17.
 */

public class ActivityFragment extends BaseFragment implements ActivityFragmentPresenter.ActivityFragmentUI {

    private View view;

    @Inject
    ActivityFragmentPresenter presenter;
    private ActivityRecylerViewAdapter adapter;

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
        if (view == null) {
            view = inflater.inflate(R.layout.activity_fragment_layout, container, false);
            final RecyclerView historyView = view.findViewById(R.id.activity_recycler_view);
            adapter = new ActivityRecylerViewAdapter(getContext());
            historyView.setAdapter(adapter);
        }
        return view;

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
    public void setData(List<TransactionDto> transactionDtos) {
        adapter.setRecipients(transactionDtos);
    }

    @Override
    public void setError() {
        Toast.makeText(getContext(), "Error ", Toast.LENGTH_SHORT).show();
    }
}
