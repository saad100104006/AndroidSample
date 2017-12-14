package uk.co.transferx.app.welcom.presenter;

import javax.inject.Inject;

import uk.co.transferx.app.BasePresenter;
import uk.co.transferx.app.UI;

/**
 * Created by smilevkiy on 14.11.17.
 */

public class WelcomeFragmentPresenter extends BasePresenter<WelcomeFragmentPresenter.WelcomeUI> {

    @Inject
    public WelcomeFragmentPresenter() {
    }

    public interface WelcomeUI extends UI {

    }
}
