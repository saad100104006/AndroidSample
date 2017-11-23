package uk.co.transferx.app.recoverpass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;

import uk.co.transferx.app.BaseActivity;
import uk.co.transferx.app.BaseFragment;
import uk.co.transferx.app.R;
import uk.co.transferx.app.recoverpass.fragment.RecoverPasswordFragment;

/**
 * Created by sergey on 23.11.17.
 */

public class RecoverPasswordActivity extends BaseActivity {

    public static void starRecoverPasswordActivity(Activity activity) {
        activity.startActivity(new Intent(activity, RecoverPasswordActivity.class));
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recover_password_activity_layout);
        BaseFragment recoverPasswordFragment = new RecoverPasswordFragment();
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, recoverPasswordFragment, recoverPasswordFragment.getTag());
        ft.commit();
    }

}
