package uk.co.transferx.app;

import android.support.multidex.MultiDexApplication;

import com.squareup.leakcanary.LeakCanary;

import javax.inject.Inject;

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
                .networkModule(new NetworkModule("http://transferx.ddns.net"))
                .build();
        appComponent.inject(this);

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        // Normal app init code...
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }


}
