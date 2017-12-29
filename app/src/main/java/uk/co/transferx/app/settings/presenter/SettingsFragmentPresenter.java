package uk.co.transferx.app.settings.presenter;

import javax.inject.Inject;

import uk.co.transferx.app.BasePresenter;
import uk.co.transferx.app.UI;

/**
 * Created by sergey on 29/12/2017.
 */

public class SettingsFragmentPresenter extends BasePresenter<SettingsFragmentPresenter.SettingsFragmentUI> {

    @Inject
    public SettingsFragmentPresenter() {
    }

    public void logOut(){
        if(ui != null){
            ui.goToWelcome();
        }
    }

    public interface SettingsFragmentUI extends UI {

        void goToWelcome();
    }
}
