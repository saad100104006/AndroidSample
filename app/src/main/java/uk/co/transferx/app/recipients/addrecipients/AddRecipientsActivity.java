package uk.co.transferx.app.recipients.addrecipients;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import uk.co.transferx.app.BaseActivity;
import uk.co.transferx.app.R;
import uk.co.transferx.app.recipients.addrecipients.fragments.AddRecipientsFragment;

/**
 * Created by sergey on 03/01/2018.
 */

public class AddRecipientsActivity extends BaseActivity {


    public static void startAddRecipientActivity(final Activity activity) {
        //   activity.startActivityForResult(new Intent(activity, AddRecipientsActivity.class), ADD_RECIPIENT);

    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_recipients_activity_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.add_recipients);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        final AddRecipientsFragment addRecipientsFragment = new AddRecipientsFragment();
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, addRecipientsFragment, addRecipientsFragment.getTag());
        ft.commit();

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
