package uk.co.transferx.app.mainscreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.util.SparseArray;
import android.view.Menu;
import android.widget.Toast;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import uk.co.transferx.app.BaseActivity;
import uk.co.transferx.app.BaseFragment;
import uk.co.transferx.app.R;
import uk.co.transferx.app.mainscreen.fragments.ActivityFragment;
import uk.co.transferx.app.mainscreen.fragments.RecipientsFragment;
import uk.co.transferx.app.mainscreen.fragments.TransferFragment;
import uk.co.transferx.app.settings.fragment.SettingsFragment;

/**
 * Created by sergey on 14.12.17.
 */

public class MainActivity extends BaseActivity {

    private final SparseArray<BaseFragment> fragments = new SparseArray<>(4);
    private static short CURRENT_ITEM = 0;

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
        fragments.put(0, new ActivityFragment());
        fragments.put(1, new TransferFragment());
        fragments.put(2, new RecipientsFragment());
        fragments.put(3, new SettingsFragment());
        final BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottom_navigation);
        bottomNavigationViewEx.setTypeface(ResourcesCompat.getFont(this, R.font.montserrat));
        selectScreen(R.id.activity);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.setOnNavigationItemSelectedListener(item -> {
            selectScreen(item.getItemId());
            return true;
        });
    }

    private void selectScreen(@IdRes int item) {
        switch (item) {
            case R.id.activity:
                replaceFragment(fragments.get(0), CURRENT_ITEM, R.id.container_main);
                CURRENT_ITEM = 0;
                break;
            case R.id.transfer:
                replaceFragment(fragments.get(1), CURRENT_ITEM - 1, R.id.container_main);
                CURRENT_ITEM = 1;
                break;
            case R.id.recipients:
                replaceFragment(fragments.get(2), CURRENT_ITEM - 2, R.id.container_main);
                CURRENT_ITEM = 2;
                break;
            case R.id.settings:
                replaceFragment(fragments.get(3), CURRENT_ITEM - 3, R.id.container_main);
                CURRENT_ITEM = 3;
                break;
            default:
                throw new IllegalStateException(MainActivity.class.getSimpleName() + " Error state number " + item );
        }

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

    @Override
    protected void onDestroy() {
        fragments.clear();
        CURRENT_ITEM = 0;
        super.onDestroy();
    }
}
