package uk.co.transferx.app.recoverpass;

import android.os.Bundle;
import android.support.annotation.Nullable;

import uk.co.transferx.app.BaseActivity;
import uk.co.transferx.app.R;
import uk.co.transferx.app.recoverpass.fragment.RecoverPasswordFragment;
import uk.co.transferx.app.recoverpass.fragment.RecoverPasswordSuccessFragment;

/**
 * Created by sergey on 23.11.17.
 */

public class RecoverPasswordActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recover_password_activity_layout);
        replaceFragment(new RecoverPasswordFragment(), 0, R.id.container);
    }

    public void goSuccess() {
        replaceFragment(new RecoverPasswordSuccessFragment(), -1, R.id.container);
    }


}
