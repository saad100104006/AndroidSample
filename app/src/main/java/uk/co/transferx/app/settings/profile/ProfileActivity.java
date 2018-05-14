package uk.co.transferx.app.settings.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import javax.inject.Inject;

import uk.co.transferx.app.BaseActivity;
import uk.co.transferx.app.R;
import uk.co.transferx.app.TransferXApplication;
import uk.co.transferx.app.settings.profile.changepassword.ChangePasswordActivity;
import uk.co.transferx.app.settings.profile.personaldetails.PersonalDetailsActivity;
import uk.co.transferx.app.settings.profile.presenter.ProfileActivityPresenter;
import uk.co.transferx.app.settings.profile.wallet.WalletActivity;

/**
 * Created by sergey on 30/01/2018.
 */

public class ProfileActivity extends BaseActivity implements ProfileActivityPresenter.ProfileActivityUI {

    @Inject
    ProfileActivityPresenter presenter;

    public static void startProfileActivity(final Context context) {
        context.startActivity(new Intent(context, ProfileActivity.class));
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((TransferXApplication) getApplication()).getAppComponent().inject(this);
        setContentView(R.layout.profile_activity_layout);
        findViewById(R.id.button_back).setOnClickListener(v -> onBackPressed());
        findViewById(R.id.personal_details).setOnClickListener(v -> runActivity(PersonalDetailsActivity.class));
        findViewById(R.id.wallet).setOnClickListener(v -> runActivity(WalletActivity.class));
        findViewById(R.id.change_password).setOnClickListener(v -> runActivity(ChangePasswordActivity.class));
    }


    private void runActivity(Class clazz) {
        startActivity(new Intent(ProfileActivity.this, clazz));
    }


    @Override
    protected void onResume() {
        super.onResume();
        presenter.attachUI(this);
    }

    @Override
    protected void onPause() {
        presenter.detachUI();
        super.onPause();
    }


}
