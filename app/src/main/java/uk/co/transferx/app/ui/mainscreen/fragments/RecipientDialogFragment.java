package uk.co.transferx.app.ui.mainscreen.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import uk.co.transferx.app.R;
import uk.co.transferx.app.TransferXApplication;
import uk.co.transferx.app.data.dto.RecipientDto;
import uk.co.transferx.app.ui.mainscreen.adapters.RecipientDialogAdapter;
import uk.co.transferx.app.ui.mainscreen.presenters.RecipientDialogFragmentPresenter;
import uk.co.transferx.app.ui.welcom.WelcomeActivity;

/**
 * Created by sergey on 15/01/2018.
 */

public class RecipientDialogFragment extends DialogFragment implements RecipientDialogFragmentPresenter.RecipientDialogFragmentUI {


    private RecipientDialogAdapter recipientDialogAdapter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private Disposable disposable;
    private EditText searchRecipient;
    private final static long DEBOUNCE_SEARCH = 500;

    @Inject
    RecipientDialogFragmentPresenter presenter;

    public RecipientDialogFragment() {
    }

    public static RecipientDialogFragment newInstance() {
        return new RecipientDialogFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppAlertTheme);
        ((TransferXApplication) getActivity().getApplication()).getAppComponent().inject(this);
        recipientDialogAdapter = new RecipientDialogAdapter(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipient_dialog_layout, null);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        recyclerView = view.findViewById(R.id.recycler_dialog_recipient);
        progressBar = view.findViewById(R.id.loading_list_recipients);
        searchRecipient = view.findViewById(R.id.search);
        recyclerView.setAdapter(recipientDialogAdapter);
        recyclerView.setHasFixedSize(true);
        return view;

    }

    private void hideKeyboard() {
        View view = getView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    @Override
    public void dismiss() {
        hideKeyboard();
        if (getDialog() != null) {
            super.dismiss();
        } else {
            getFragmentManager().popBackStack();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        presenter.attachUI(this);
        disposable = RxTextView.textChanges(searchRecipient)
                .debounce(DEBOUNCE_SEARCH, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(sequence -> {
                    presenter.searchRecipient(sequence.toString());
                });
    }

    @Override
    public void onPause() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        presenter.detachUI();
        super.onPause();
    }


    @Override
    public void setRecipients(List<RecipientDto> recipients) {
        progressBar.setVisibility(View.GONE);
        recipientDialogAdapter.setRecipients(recipients);
    }


    @Override
    public void goToWelcome() {
        WelcomeActivity.startWelcomeActivity(getActivity());
    }
}
