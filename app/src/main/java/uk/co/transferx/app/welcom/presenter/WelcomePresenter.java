package uk.co.transferx.app.welcom.presenter;

import javax.inject.Inject;

import uk.co.transferx.app.BasePresenter;
import uk.co.transferx.app.UI;

/**
 * Created by smilevkiy on 14.11.17.
 */

public class WelcomePresenter extends BasePresenter<WelcomePresenter.WelcomeUI> {

    @Inject
    public WelcomePresenter() {
    }

    public interface WelcomeUI extends UI {

    }
}
