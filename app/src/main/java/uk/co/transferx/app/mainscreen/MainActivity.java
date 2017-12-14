package uk.co.transferx.app.mainscreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import uk.co.transferx.app.R;
import uk.co.transferx.app.mainscreen.adapter.TransferXTabAdapter;

/**
 * Created by sergey on 14.12.17.
 */

public class MainActivity extends AppCompatActivity {


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
}
