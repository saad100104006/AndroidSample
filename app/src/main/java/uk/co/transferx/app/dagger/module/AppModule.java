package uk.co.transferx.app.dagger.module;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import uk.co.transferx.app.tokenmanager.TokenManager;

/**
 * Created by smilevkiy on 13.11.17.
 */
@Module
public class AppModule {


    Application mApplication;

    public AppModule(Application mApplication) {
        this.mApplication = mApplication;
    }

    @Singleton
    @Provides
    SharedPreferences providesSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Singleton
    @Provides
    Application providesApplication() {
        return this.mApplication;
    }

    @Singleton
    @Provides
    TokenManager providesTokenManager(SharedPreferences sharedPreferences) {
        return new TokenManager(sharedPreferences);
    }
}
