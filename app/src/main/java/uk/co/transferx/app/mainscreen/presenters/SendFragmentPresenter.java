package uk.co.transferx.app.mainscreen.presenters;

import javax.inject.Inject;

import uk.co.transferx.app.BasePresenter;
import uk.co.transferx.app.UI;

/**
 * Created by sergey on 15.12.17.
 */

public class SendFragmentPresenter extends BasePresenter<SendFragmentPresenter.SendFragmentUI> {

    @Inject
    public SendFragmentPresenter() {
    }

    public interface SendFragmentUI extends UI {

    }
}
