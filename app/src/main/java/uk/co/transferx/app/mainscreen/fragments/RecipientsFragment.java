package uk.co.transferx.app.mainscreen.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import uk.co.transferx.app.BaseFragment;
import uk.co.transferx.app.R;
import uk.co.transferx.app.TransferXApplication;
import uk.co.transferx.app.dto.RecipientDto;
import uk.co.transferx.app.mainscreen.adapters.RecipientHorizontalRecyclerAdapter;
import uk.co.transferx.app.mainscreen.adapters.RecipientVerticalRecyclerAdapter;
import uk.co.transferx.app.mainscreen.presenters.RecipientsFragmentPresenter;
import uk.co.transferx.app.recipients.addrecipients.AddRecipientsActivity;

import static uk.co.transferx.app.splash.SplashActivity.INITIAL_TOKEN;

/**
 * Created by sergey on 17.12.17.
 */

public class RecipientsFragment extends BaseFragment implements RecipientsFragmentPresenter.RecipientsFragmentUI {

    private View view;
    private RecipientHorizontalRecyclerAdapter horizontalRecyclerAdapter;
    private RecipientVerticalRecyclerAdapter verticalRecyclerAdapter;
    private TextView emptyList;

    @Inject
    RecipientsFragmentPresenter presenter;

    @Inject
    SharedPreferences sharedPreferences;


    @Override
    public String tagName() {
        return RecipientsFragment.class.getSimpleName();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        ((TransferXApplication) getActivity().getApplication()).getAppComponent().inject(this);
        super.onCreate(savedInstanceState);
        presenter.setToken(sharedPreferences.getString(INITIAL_TOKEN, null));
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.recipients_fragment_layout, container, false);
            RecyclerView horizontalRecipientRecyclerView = view.findViewById(R.id.horizontal_recycler_view);
            RecyclerView verticalRecipientRecyclerView = view.findViewById(R.id.vertical_recycler_view);
            horizontalRecyclerAdapter = new RecipientHorizontalRecyclerAdapter(getContext());
            verticalRecyclerAdapter = new RecipientVerticalRecyclerAdapter(getContext());
            emptyList = view.findViewById(R.id.empty_list);
            horizontalRecipientRecyclerView.setAdapter(horizontalRecyclerAdapter);
            verticalRecipientRecyclerView.setAdapter(verticalRecyclerAdapter);
            view.findViewById(R.id.add_button).setOnClickListener(v -> AddRecipientsActivity.startAddRecipientActivity(getActivity()));
        }
        return view;

    }

    @Override
    public void setFavoriteRecipients(List<RecipientDto> recipientDtos) {
        emptyList.setVisibility(recipientDtos.isEmpty() ? View.VISIBLE : View.GONE);
        verticalRecyclerAdapter.setRecipients(recipientDtos);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.attachUI(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.detachUI();
    }

    @Override
    public void showError() {

    }
}
