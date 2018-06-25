package uk.co.transferx.app.recipients.detailsrecipient;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import uk.co.transferx.app.BaseActivity;
import uk.co.transferx.app.R;
import uk.co.transferx.app.recipients.detailsrecipient.fragment.RecipientDetailsFragment;

/**
 * Created by sergey on 03/01/2018.
 */

public class RecipientDetailsActivity extends BaseActivity {

    public static final String RECIPIENT = "recipient";

//    public static void startAddRecipientActivity(final Activity activity, RecipientDto recipientDto) {
//        final Intent intent = new Intent(activity, RecipientDetailsActivity.class);
//        intent.putExtra(RECIPIENT, recipientDto);
//        activity.startActivityForResult(intent, ADD_CHANGE_RECIPIENT);
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_recipients_activity_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.view_recipient);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        final RecipientDetailsFragment recipientDetailsFragment = new RecipientDetailsFragment();
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, recipientDetailsFragment, recipientDetailsFragment.getTag());
        ft.commit();

    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(Activity.RESULT_CANCELED);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
