package uk.co.transferx.app.signin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import uk.co.transferx.app.BaseActivity;

/**
 * Created by sergey on 19.11.17.
 */

public class SignInActivity extends BaseActivity {

    private final static String TYPE_SIGNIN = "type_signin";


    public static void starSignInActivity(Activity activity, SignInType signInType) {

        Intent intent = new Intent(activity, SignInActivity.class);
        intent.putExtra(TYPE_SIGNIN, signInType.ordinal());

    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getIntent();
    }


}
