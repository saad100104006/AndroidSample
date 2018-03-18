package uk.co.transferx.app.mainscreen;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.iid.FirebaseInstanceId;

import uk.co.transferx.app.R;
import uk.co.transferx.app.mainscreen.adapters.TransferXTabAdapter;
import uk.co.transferx.app.settings.SettingsActivity;
import uk.co.transferx.app.signin.SignInActivity;
import uk.co.transferx.app.signin.SignInType;

/**
 * Created by sergey on 14.12.17.
 */

public class MainActivity extends AppCompatActivity {
    private View progressView;
    private ProgressBar progressBar;

    public static void startMainActivity(Activity activity) {
        activity.startActivity(new Intent(activity, MainActivity.class));
        activity.finish();
    }

    private TransferXTabAdapter transferXTabAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);
        ViewPager viewPager = findViewById(R.id.pager_tabs);
        TabLayout tabLayout = findViewById(R.id.tabs);
        Toolbar toolbar = findViewById(R.id.toolbar);
        progressView = findViewById(R.id.alpha_view);
        progressBar = findViewById(R.id.progress_bar);
        toolbar.setOnMenuItemClickListener(item -> false);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        transferXTabAdapter = new TransferXTabAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(transferXTabAdapter);
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(transferXTabAdapter.getTabView(i));
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public final void onTabSelected(TabLayout.Tab tab) {
                transferXTabAdapter.setCheckStatus(tab.getPosition(), true);
            }

            @Override
            public final void onTabUnselected(TabLayout.Tab tab) {
                transferXTabAdapter.setCheckStatus(tab.getPosition(), false);
            }

            @Override
            public final void onTabReselected(TabLayout.Tab tab) {
                // not necessary
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public void setProrges(boolean visible) {
        progressView.setVisibility(visible ? View.VISIBLE : View.GONE);
        progressBar.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.one:
                SettingsActivity.startSettings(this);
                break;
            case R.id.two:
                String token = FirebaseInstanceId.getInstance().getToken();
                Log.d("Sergey", "Token " + token);
                new AlertDialog.Builder(this)
                        .setMessage(token)
                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                        .show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}
