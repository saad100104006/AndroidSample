package uk.co.transferx.app.ui.settings.profile.presenter;

import android.content.SharedPreferences;

import javax.inject.Inject;

import uk.co.transferx.app.ui.base.BasePresenter;
import uk.co.transferx.app.ui.base.UI;

/**
 * Created by sergey on 30/01/2018.
 */

public class ProfileActivityPresenter extends BasePresenter<ProfileActivityPresenter.ProfileActivityUI> {

    @Inject
    public ProfileActivityPresenter(final SharedPreferences sharedPreferences) {
        super(sharedPreferences);
    }

    public interface ProfileActivityUI extends UI {

    }
}
