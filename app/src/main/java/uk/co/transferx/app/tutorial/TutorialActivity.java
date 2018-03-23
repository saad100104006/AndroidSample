package uk.co.transferx.app.tutorial;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;

import uk.co.transferx.app.BaseActivity;
import uk.co.transferx.app.R;
import uk.co.transferx.app.tutorial.adapter.TutorialAdapter;

/**
 * Created by sergey on 21/03/2018.
 */

public class TutorialActivity extends BaseActivity {


    public static void startTutorialActivity(Activity activity) {
        Intent intent = new Intent(activity, TutorialActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial_activity_layout);
        final ViewPager viewPager = findViewById(R.id.pager);
        final TutorialAdapter tutorialAdapter = new TutorialAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tutorialAdapter);
    }
}
