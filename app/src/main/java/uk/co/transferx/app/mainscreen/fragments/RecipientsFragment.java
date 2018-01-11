package uk.co.transferx.app.mainscreen.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import static android.app.Activity.RESULT_OK;

/**
 * Created by sergey on 17.12.17.
 */

public class RecipientsFragment extends BaseFragment implements RecipientsFragmentPresenter.RecipientsFragmentUI {

    private View view;
    private RecipientHorizontalRecyclerAdapter horizontalRecyclerAdapter;
    private RecipientVerticalRecyclerAdapter verticalRecyclerAdapter;
    private TextView emptyListVertical, emptyListHorizontal;
    public static final int ADD_CHANGE_RECIPIENT = 333;


    @Inject
    RecipientsFragmentPresenter presenter;


    @Override
    public String tagName() {
        return RecipientsFragment.class.getSimpleName();
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
            view = inflater.inflate(R.layout.recipients_fragment_layout, container, false);
            RecyclerView horizontalRecipientRecyclerView = view.findViewById(R.id.horizontal_recycler_view);
            RecyclerView verticalRecipientRecyclerView = view.findViewById(R.id.vertical_recycler_view);
            horizontalRecyclerAdapter = new RecipientHorizontalRecyclerAdapter(getContext());
            verticalRecyclerAdapter = new RecipientVerticalRecyclerAdapter(this);
            emptyListVertical = view.findViewById(R.id.empty_list);
            emptyListHorizontal = view.findViewById(R.id.empty_list_horizontal);
            horizontalRecipientRecyclerView.setAdapter(horizontalRecyclerAdapter);
            verticalRecipientRecyclerView.setAdapter(verticalRecyclerAdapter);
            horizontalRecipientRecyclerView.setHasFixedSize(true);
            verticalRecipientRecyclerView.setHasFixedSize(true);
            view.findViewById(R.id.add_button).setOnClickListener(v -> startActivityForResult(new Intent(getContext(), AddRecipientsActivity.class), ADD_CHANGE_RECIPIENT));
        }
        return view;

    }

    @Override
    public void setFavoriteRecipients(List<RecipientDto> recipientDtos) {
        emptyListHorizontal.setVisibility(recipientDtos.isEmpty() ? View.VISIBLE : View.GONE);
        horizontalRecyclerAdapter.setRecipients(recipientDtos);
    }

    @Override
    public void setRecipients(List<RecipientDto> recipientDtos) {
        emptyListVertical.setVisibility(recipientDtos.isEmpty() ? View.VISIBLE : View.GONE);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("Sergey", "request code " + requestCode + " result_code " + (resultCode == RESULT_OK));
        if (requestCode == ADD_CHANGE_RECIPIENT && resultCode == RESULT_OK) {
            presenter.setShouldRefresh(true);
            presenter.attachUI(this);
        }
    }
}
