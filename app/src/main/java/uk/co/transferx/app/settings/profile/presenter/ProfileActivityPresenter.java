package uk.co.transferx.app.settings.profile.presenter;

import javax.inject.Inject;

import uk.co.transferx.app.BasePresenter;
import uk.co.transferx.app.UI;

/**
 * Created by sergey on 30/01/2018.
 */

public class ProfileActivityPresenter extends BasePresenter<ProfileActivityPresenter.ProfileActivityUI> {

    @Inject
    public ProfileActivityPresenter() {
    }

    public interface ProfileActivityUI extends UI {

    }
}