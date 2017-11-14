package uk.co.transferx.app;

import android.app.Application;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;
import uk.co.transferx.app.dagger.component.AppComponent;
import uk.co.transferx.app.dagger.component.DaggerAppComponent;
import uk.co.transferx.app.dagger.module.AppModule;

/**
 * Created by smilevkiy on 13.11.17.
 */

public class TransferXApplication extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule())
                .build();

        if (!BuildConfig.DEBUG) {
            Fabric.with(this, new Crashlytics());
        }
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }


}
