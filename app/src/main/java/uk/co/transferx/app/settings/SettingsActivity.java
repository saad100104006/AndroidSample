package uk.co.transferx.app.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import uk.co.transferx.app.BaseActivity;
import uk.co.transferx.app.R;

/**
 * Created by sergey on 22/12/2017.
 */

public class SettingsActivity extends BaseActivity {


    public static void startSettings(final Context context) {
        context.startActivity(new Intent(context, SettingsActivity.class));
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.settings);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        return super.onOptionsItemSelected(item);
    }
}
