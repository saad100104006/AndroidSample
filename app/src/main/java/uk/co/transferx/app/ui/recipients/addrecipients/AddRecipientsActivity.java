package uk.co.transferx.app.ui.recipients.addrecipients;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;

import uk.co.transferx.app.ui.base.BaseActivity;
import uk.co.transferx.app.R;
import uk.co.transferx.app.ui.recipients.addrecipients.fragments.AddRecipientsFragment;

import static uk.co.transferx.app.util.Constants.MODE;
import static uk.co.transferx.app.util.Constants.RECIPIENT;

/**
 * Created by sergey on 03/01/2018.
 */

public class AddRecipientsActivity extends BaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_recipients_activity_layout);
        final AddRecipientsFragment addRecipientsFragment = new AddRecipientsFragment();
        final Bundle bundle = new Bundle();
        final Intent intent = getIntent();
        final Mode mode = Mode.values()[intent.getIntExtra(MODE, 3)];
        if (mode == Mode.EDIT) {
            bundle.putParcelable(RECIPIENT, intent.getParcelableExtra(RECIPIENT));
        }
        bundle.putInt(MODE, intent.getIntExtra(MODE, -1));
        addRecipientsFragment.setArguments(bundle);
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, addRecipientsFragment, addRecipientsFragment.getTag());
        ft.commit();

    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        finish();
        super.onBackPressed();
    }
}
