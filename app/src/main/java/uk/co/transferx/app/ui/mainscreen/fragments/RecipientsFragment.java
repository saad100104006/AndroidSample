package uk.co.transferx.app.ui.mainscreen.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

import javax.inject.Inject;

import uk.co.transferx.app.ui.base.BaseFragment;
import uk.co.transferx.app.R;
import uk.co.transferx.app.TransferXApplication;
import uk.co.transferx.app.data.dto.RecipientDto;
import uk.co.transferx.app.ui.mainscreen.MainActivityOld;
import uk.co.transferx.app.ui.mainscreen.adapters.RecipientVerticalRecyclerAdapter;
import uk.co.transferx.app.ui.mainscreen.adapters.SwipeHelper;
import uk.co.transferx.app.ui.mainscreen.presenters.RecipientsFragmentPresenter;
import uk.co.transferx.app.ui.recipients.addrecipients.AddRecipientsActivity;
import uk.co.transferx.app.ui.recipients.addrecipients.Mode;
import uk.co.transferx.app.ui.customview.ConfirmationDialogFragment;
import uk.co.transferx.app.ui.signin.SignInActivity;

import static android.app.Activity.RESULT_OK;
import static uk.co.transferx.app.util.Constants.MODE;
import static uk.co.transferx.app.util.Constants.RECIPIENT;
import static uk.co.transferx.app.ui.customview.ConfirmationDialogFragment.MESSAGE;
import static uk.co.transferx.app.ui.customview.ConfirmationDialogFragment.POSITION;

/**
 * Created by sergey on 17.12.17.
 */

public class RecipientsFragment extends BaseFragment implements RecipientsFragmentPresenter.RecipientsFragmentUI {

    public static final int ADD_RECIPIENT = 333;
    public static final int CHANGE_RECIPIENT = 444;
    public static final int DELETE_USER = 234;
    private RecipientVerticalRecyclerAdapter verticalRecyclerAdapter;
    private RecyclerView recipientRecyclerView;
    private LinearLayout emptyDescription;
    private static final int DEFAULT_VALUE = -1;


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
        recipientRecyclerView = view.findViewById(R.id.vertical_recycler_view);
        emptyDescription = view.findViewById(R.id.empty_description);
        verticalRecyclerAdapter = new RecipientVerticalRecyclerAdapter(this, presenter);
        recipientRecyclerView.setAdapter(verticalRecyclerAdapter);
        new SwipeHelper(recipientRecyclerView) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        getString(R.string.delete).toUpperCase(),
                        ContextCompat.getColor(getContext(), R.color.red_delete),
                        pos -> showDialogConfirmation(pos)
                ));

                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        getString(R.string.transfer),
                        ContextCompat.getColor(getContext(), R.color.green),
                        pos -> presenter.goToTransfer(pos)
                ));
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        getString(R.string.edit),
                        ContextCompat.getColor(getContext(), R.color.gray),
                        pos -> presenter.editRecipient(Mode.EDIT, pos)
                ));
            }
        };
        view.findViewById(R.id.add_new_recipients).setOnClickListener(v -> presenter.addRecipient(Mode.ADD, null));
    }

    private void showDialogConfirmation(int position) {
        ConfirmationDialogFragment dialogFragment = new ConfirmationDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(MESSAGE, getString(R.string.delete_user_message, verticalRecyclerAdapter.getRecipient(position).toString()));
        bundle.putInt(POSITION, position);
        dialogFragment.setArguments(bundle);
        dialogFragment.setTargetFragment(this, DELETE_USER);
        dialogFragment.setCancelable(false);
        dialogFragment.show(getFragmentManager(), "TAG");
    }

    @Override
    public void setFavoriteRecipients(List<RecipientDto> recipientDtos) {

    }

    @Override
    public void setRecipients(List<RecipientDto> recipientDtos) {
        boolean isEmpty = recipientDtos.isEmpty();
        recipientRecyclerView.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        emptyDescription.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        verticalRecyclerAdapter.setRecipients(recipientDtos);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.attachUI(this);
        Log.d("Serge", "attach UI");
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.detachUI();
        Log.d("Serge", "detatache UI");
    }

    @Override
    public void showError() {

    }

    @Override
    public void goToTransfer(RecipientDto recipientDto) {
        final Bundle bundle = new Bundle();
        bundle.putParcelable(RECIPIENT, recipientDto);
        ((MainActivityOld) getActivity()).selectScreen(R.id.transfer, bundle);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_RECIPIENT && resultCode == RESULT_OK) {
            presenter.attachUI(this);
        } else if (requestCode == CHANGE_RECIPIENT) {
            presenter.attachUI(this);
            if (data != null) {
                presenter.goToTransfer(data.getParcelableExtra(RECIPIENT));
            }
        }
        if (requestCode == DELETE_USER) {
            int position = data.getIntExtra(POSITION, DEFAULT_VALUE);
            if (position != DEFAULT_VALUE) {
                presenter.deleteRecipient(verticalRecyclerAdapter.getRecipient(position));
            }
        }
    }

    @Override
    public void addRecipient(Mode mode, RecipientDto recipientDto) {
        final Intent intent = new Intent(getContext(), AddRecipientsActivity.class);
        intent.putExtra(MODE, mode.ordinal());
        if (recipientDto != null) {
            intent.putExtra(RECIPIENT, recipientDto);
        }
        startActivityForResult(intent, mode == Mode.ADD ? ADD_RECIPIENT : CHANGE_RECIPIENT);
    }

    @Override
    public void deleteRecipient(RecipientDto recipientDto) {
        verticalRecyclerAdapter.removeItem(recipientDto);
    }

    @Override
    public void goToWelcome() {
        SignInActivity.Companion.startSignInActivity(getActivity());
    }
}
