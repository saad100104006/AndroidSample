package uk.co.transferx.app.ui.mainscreen.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.mynameismidori.currencypicker.ExtendedCurrency;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import uk.co.transferx.app.ui.base.BaseFragment;
import uk.co.transferx.app.R;
import uk.co.transferx.app.TransferXApplication;
import uk.co.transferx.app.data.dto.RecipientDto;
import uk.co.transferx.app.ui.mainscreen.presenters.TransferFragmentPresenter;
import uk.co.transferx.app.ui.mainscreen.schedule.RepeatTransferActivity;
import uk.co.transferx.app.ui.mainscreen.schedule.ReviewActivity;
import uk.co.transferx.app.ui.mainscreen.schedule.ScheduleActivity;
import uk.co.transferx.app.data.pojo.Card;
import uk.co.transferx.app.data.pojo.TransactionCreate;
import uk.co.transferx.app.ui.customview.CustomSpinner;
import uk.co.transferx.app.ui.signin.SignInActivity;

import static uk.co.transferx.app.util.Constants.EMPTY;
import static uk.co.transferx.app.util.Constants.TRANSACTION;

/**
 * Created by sergey on 14.12.17.
 */

public class TransferFragment extends BaseFragment implements TransferFragmentPresenter.SendFragmentUI {

    public final static int REQUEST_RECIPIENT = 321;
    public static final String RECIPIENT = "recipient";
    private TextView name, rate, country, currencyCodeFirst, currencyCodeSecond, calculatedValue;
    private RecipientDialogFragment recipientDialogFragment;
    private final static String GBP = "GBP";
    private final static String UGX = "UGX";
    private List<ExtendedCurrency> currencyList;
    private EditText sendInput, messageInput;
    private Disposable disposable;
    private Disposable messageDisposable;
    private CustomSpinner recipientSpinner, paymentMethod;
    private Pattern patternNumber = Pattern.compile("^(\\d+\\.)?\\d+$");
    private Button sendNowButton, sendLaterButton;
    private AppCompatCheckBox repeatTransfer;
    private View view;
    private boolean isInitializedCards;
    private boolean isInitializedRecipients;

    @Inject
    TransferFragmentPresenter presenter;


    @Override
    public String tagName() {
        return TransferFragment.class.getSimpleName();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        ((TransferXApplication) getActivity().getApplication()).getAppComponent().inject(this);
        super.onCreate(savedInstanceState);
        final Bundle bundle = getArguments();
        if (bundle != null) {
            presenter.chooseRecipientForTransfer(bundle.getParcelable(RECIPIENT));
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        presenter.setInitialization(isInitializedCards && isInitializedRecipients);
        final Bundle bundle = getArguments();
        if (bundle != null) {
            final RecipientDto recipient = bundle.getParcelable(RECIPIENT);
            presenter.chooseRecipientForTransfer(recipient);
            int position = recipientSpinner.getAdapter().getPosition(recipient);
            recipientSpinner.post(() -> recipientSpinner.setSelection(position));
        }
        if (view == null) {
            view = inflater.inflate(R.layout.transfer_fragment_layout, container, false);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rate = view.findViewById(R.id.exchange_rate);
        messageInput = view.findViewById(R.id.messageInput);
        sendInput = view.findViewById(R.id.sendInput);
        calculatedValue = view.findViewById(R.id.receiveInput);
        paymentMethod = view.findViewById(R.id.paymentCard);
        sendNowButton = view.findViewById(R.id.sendNow);
        sendLaterButton = view.findViewById(R.id.sendLater);
        repeatTransfer = view.findViewById(R.id.repeat);
        repeatTransfer.setOnCheckedChangeListener((buttonView, isChecked) -> presenter.setRepeatStatus(isChecked));
        sendNowButton.setOnClickListener(v -> presenter.goToNextScreen());
        sendLaterButton.setOnClickListener(v -> presenter.sendLaterClicked());
        paymentMethod.setOnItemSelectedListener((position, object) -> {
            if (object == null || !(object instanceof Card)) {
                return;
            }
            presenter.setCard((Card) object);
        });
        recipientSpinner = view.findViewById(R.id.SendToRecipient);
        recipientSpinner.setOnItemSelectedListener((position, object) -> {
            presenter.chooseRecipientForTransfer((RecipientDto) object);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.setPreviosStateCard((Card) paymentMethod.getSelectedItem());
        presenter.setPreviosStateRecipient((RecipientDto) recipientSpinner.getSelectedItem());
        presenter.attachUI(this);
        disposable = RxTextView.textChanges(sendInput)
                .debounce(300L, TimeUnit.MILLISECONDS)
                .filter(val -> !EMPTY.equals(val.toString()))
                .filter(value -> patternNumber.matcher(value.toString()).matches())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(sequence -> presenter.setValueToSend(sequence.toString()));
        messageDisposable = RxTextView.textChanges(messageInput)
                .debounce(100L, TimeUnit.MILLISECONDS)
                .filter(val -> !EMPTY.equals(val.toString()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(sequence -> presenter.setMessage(sequence.toString()));
    }

    @Override
    public void onPause() {
        presenter.detachUI();
        if (disposable != null) {
            disposable.dispose();
        }
        if (messageDisposable != null) {
            messageDisposable.dispose();
        }
        super.onPause();
    }

    @Override
    public void setRecipients(List<RecipientDto> recipients) {
        if (getActivity() != null && isAdded()) {
            recipientSpinner.setDataWithHintItem(recipients.toArray(), getString(R.string.recipient));
            isInitializedRecipients = true;
        }
        presenter.setInitialization(isInitializedCards && isInitializedRecipients);

    }

    @Override
    public void showDialogRecipients() {
        recipientDialogFragment = RecipientDialogFragment.newInstance();
        recipientDialogFragment.setTargetFragment(this, REQUEST_RECIPIENT);
        recipientDialogFragment.show(getFragmentManager(), RecipientDialogFragment.class.getSimpleName());
    }

    @Override
    public void showChoosenRecipient(RecipientDto recipientDto, int position) {
        recipientSpinner.setSelection(position);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_RECIPIENT && resultCode == Activity.RESULT_OK) {
            if (recipientDialogFragment != null) {
                recipientDialogFragment.dismiss();
            }
            presenter.setRecipient(data.getParcelableExtra(RECIPIENT));
        }
    }

    @Override
    public void setCardToSpinner(List<Card> cards) {
        paymentMethod.setDataWithHintItem(cards.toArray(), getString(R.string.select_a_payment_method));
        isInitializedCards = true;
        presenter.setInitialization(isInitializedCards && isInitializedRecipients);
    }

    @Override
    public void showRates(String rates) {
        rate.setText(rates);
    }

    @Override
    public void setCalculatedValueForTransfer(String value) {
        calculatedValue.setText(value);
    }

    @Override
    public void setButtonEnabled(boolean isEnabled) {
        sendNowButton.setEnabled(isEnabled);
        sendNowButton.setBackground(isEnabled ?
                ContextCompat.getDrawable(getContext(), R.drawable.oval_button_white) :
                ContextCompat.getDrawable(getContext(), R.drawable.oval_button_gray));
        sendLaterButton.setEnabled(isEnabled);
        sendLaterButton.setBackground(isEnabled ?
                ContextCompat.getDrawable(getContext(), R.drawable.oval_button_transparent_white) :
                ContextCompat.getDrawable(getContext(), R.drawable.oval_button_transparent_gray));
        sendLaterButton.setTextColor(isEnabled ? ContextCompat.getColor(getContext(), R.color.white) :
                ContextCompat.getColor(getContext(), R.color.hint));
    }

    @Override
    public void sendNowClick(TransactionCreate transactionCreate) {
        startNextScreen(new Intent(getContext(), ReviewActivity.class).putExtra(TRANSACTION, transactionCreate));
    }

    @Override
    public void sendWithRepeat(TransactionCreate transactionCreate) {
        startNextScreen(new Intent(getContext(), RepeatTransferActivity.class).putExtra(TRANSACTION, transactionCreate));
    }

    private void startNextScreen(final Intent intent) {
        startActivity(intent);
    }

    @Override
    public void goToScheduleScreen(TransactionCreate transactionCreate) {
        startNextScreen(new Intent(getContext(), ScheduleActivity.class).putExtra(TRANSACTION, transactionCreate));
    }

    @Override
    public void goToWelcome() {
        SignInActivity.Companion.startSignInActivity(getActivity());
    }

    @Override
    public void showErrorRates() {
        Toast.makeText(getContext(), "Rates error", Toast.LENGTH_SHORT).show();
    }
}
