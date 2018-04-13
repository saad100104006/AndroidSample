package uk.co.transferx.app.mainscreen.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import uk.co.transferx.app.BaseFragment;
import uk.co.transferx.app.R;
import uk.co.transferx.app.TransferXApplication;
import uk.co.transferx.app.dto.RecipientDto;
import uk.co.transferx.app.mainscreen.adapters.RecipientVerticalRecyclerAdapter;
import uk.co.transferx.app.mainscreen.adapters.SwipeHelper;
import uk.co.transferx.app.mainscreen.presenters.RecipientsFragmentPresenter;
import uk.co.transferx.app.recipients.addrecipients.AddRecipientsActivity;

import static android.app.Activity.RESULT_OK;

/**
 * Created by sergey on 17.12.17.
 */

public class RecipientsFragment extends BaseFragment implements RecipientsFragmentPresenter.RecipientsFragmentUI {

    private RecipientVerticalRecyclerAdapter verticalRecyclerAdapter;
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
        return inflater.inflate(R.layout.recipients_fragment_layout, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView verticalRecipientRecyclerView = view.findViewById(R.id.vertical_recycler_view);
        verticalRecyclerAdapter = new RecipientVerticalRecyclerAdapter(this, presenter);
        verticalRecipientRecyclerView.setAdapter(verticalRecyclerAdapter);
        verticalRecipientRecyclerView.setHasFixedSize(true);
        SwipeHelper swipeHelper = new SwipeHelper(getContext(), verticalRecipientRecyclerView) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        getString(R.string.delete).toUpperCase(),
                        0,
                        Color.parseColor("#da1010"),
                        pos -> {
                            // TODO: onDelete
                        }
                ));

                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        getString(R.string.edit),
                        0,
                        Color.parseColor("#01b1fb"),
                        pos -> {
                            // TODO: OnTransfer
                        }
                ));
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        getString(R.string.transfer),
                        0,
                        Color.parseColor("#00986e"),
                        pos -> {
                            // TODO: OnUnshare
                        }
                ));
            }
        };
        view.findViewById(R.id.add_button).setOnClickListener(v -> startActivityForResult(new Intent(getContext(), AddRecipientsActivity.class), ADD_CHANGE_RECIPIENT));
    }

    @Override
    public void setFavoriteRecipients(List<RecipientDto> recipientDtos) {
        Log.d("Sergey", "recipient settled");

    }

    @Override
    public void setRecipients(List<RecipientDto> recipientDtos) {
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
    public void addToFavorite(RecipientDto recipientDto) {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_CHANGE_RECIPIENT && resultCode == RESULT_OK) {
            presenter.setShouldRefresh(true);
            presenter.attachUI(this);
        }
    }

    @Override
    public void updateFavoriteRecipients() {

    }
}
