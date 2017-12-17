package uk.co.transferx.app.mainscreen.presenters;

import javax.inject.Inject;

import uk.co.transferx.app.BasePresenter;
import uk.co.transferx.app.UI;

/**
 * Created by sergey on 17.12.17.
 */

public class RecipientsFragmentPresenter extends BasePresenter<RecipientsFragmentPresenter.RecipientsFragmentUI> {

    @Inject
    public RecipientsFragmentPresenter() {
    }

    public interface RecipientsFragmentUI extends UI {

    }
}
