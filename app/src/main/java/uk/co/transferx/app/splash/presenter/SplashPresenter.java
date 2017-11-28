package uk.co.transferx.app.splash.presenter;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import uk.co.transferx.app.BasePresenter;
import uk.co.transferx.app.UI;

/**
 * Created by sergey on 19.11.17.
 */

public class SplashPresenter extends BasePresenter<SplashPresenter.SplashUI> {


    private static final long DELAY = 800;

    private Disposable dis;

    @Inject
    public SplashPresenter() {
    }


    @Override
    public void attachUI(SplashUI ui) {
        super.attachUI(ui);
        dis = Observable.just(new Object())
                .delay(DELAY, TimeUnit.MILLISECONDS)
                .subscribe(obj -> ui.goToWelcomeScreen());
    }


    @Override
    public void detachUI() {
        dis.dispose();
        super.detachUI();
    }

    public interface SplashUI extends UI {

        void goToWelcomeScreen();



    }
}
