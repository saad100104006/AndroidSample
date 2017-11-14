package uk.co.transferx.app;

/**
 * Created by smilevkiy on 14.11.17.
 */

public abstract class BasePresenter<T extends UI> {

    protected T ui;

    public void attachUI(T ui) {
        this.ui = ui;
    }

    public void detachUI() {
        ui = null;
    }


}
