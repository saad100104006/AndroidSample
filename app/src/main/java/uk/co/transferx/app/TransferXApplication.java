package uk.co.transferx.app;

import android.support.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;

import javax.inject.Inject;

import io.fabric.sdk.android.Fabric;
import uk.co.transferx.app.dagger.component.AppComponent;
import uk.co.transferx.app.dagger.component.DaggerAppComponent;
import uk.co.transferx.app.dagger.module.AppModule;
import uk.co.transferx.app.dagger.module.NetworkModule;
import uk.co.transferx.app.recipientsrepository.RecipientRepository;

/**
 * Created by smilevkiy on 13.11.17.
 */

public class TransferXApplication extends MultiDexApplication {

    private AppComponent appComponent;

    @Inject
    RecipientRepository recipientRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .networkModule(new NetworkModule("http://api.transferx.co.uk"))
                .build();
        appComponent.inject(this);

        if (!BuildConfig.DEBUG) {
            Fabric.with(this, new Crashlytics());
        }
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }


}
