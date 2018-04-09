package uk.co.transferx.app.mainscreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import uk.co.transferx.app.BaseFragment;
import uk.co.transferx.app.R;
import uk.co.transferx.app.mainscreen.fragments.ActivityFragment;
import uk.co.transferx.app.mainscreen.fragments.RecipientsFragment;
import uk.co.transferx.app.mainscreen.fragments.TransferFragment;

/**
 * Created by sergey on 14.12.17.
 */

public class MainActivity extends AppCompatActivity {

    private final SparseArray<BaseFragment> fragments = new SparseArray<>(4);

    public static void startMainActivity(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);
        final BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottom_navigation);
        bottomNavigationViewEx.setTypeface(ResourcesCompat.getFont(this, R.font.montserrat));
        selectScreen(R.id.activity);
      //  bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectScreen(item.getItemId());
                return true;
            }
        });
      //  bottomNavigationViewEx.setIconsMarginTop(80);
    }

    private void selectScreen(@IdRes int item){
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction()
               // .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN );
        switch (item){
            case R.id.activity :
                ActivityFragment activityFragment = (ActivityFragment) fragments.get(0);
                if (activityFragment == null) {
                    activityFragment = new ActivityFragment();
                    fragments.put(0, activityFragment);
                }
                ft.replace(R.id.container_main, activityFragment, activityFragment.getTag());
                break;
            case R.id.transfer:
                TransferFragment transferFragment = (TransferFragment) fragments.get(1);
                if (transferFragment == null) {
                    transferFragment = new TransferFragment();
                    fragments.put(1, transferFragment);
                }
                ft.replace(R.id.container_main, transferFragment, transferFragment.getTag());
                break;
           /* case R.id.recipients:
                RecipientsFragment recipientsFragment = (RecipientsFragment) fragments.get(1);
                if (recipientsFragment == null) {
                    recipientsFragment = new RecipientsFragment();
                    fragments.put(1, recipientsFragment);
                }
                ft.replace(R.id.container, recipientsFragment, recipientsFragment.getTag());
                break; */
                default:
                    Toast.makeText(this, "Not implemented yet", Toast.LENGTH_SHORT).show();
        }
        ft.commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
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
