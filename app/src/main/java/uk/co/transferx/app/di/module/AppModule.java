package uk.co.transferx.app.di.module;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import uk.co.transferx.app.data.remote.SignUpApi;
import uk.co.transferx.app.data.firebase.SubscriptionManager;
import uk.co.transferx.app.data.repository.tokenmanager.TokenManager;
import uk.co.transferx.app.data.repository.tokenmanager.TokenRepository;

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
    TokenManager providesTokenManager(TokenRepository tokenRepository, SignUpApi signUpApi, SharedPreferences sharedPreferences) {
        return new TokenManager(tokenRepository, signUpApi, sharedPreferences);
    }

    @Singleton
    @Provides
    SubscriptionManager provideSubscribtionManager(SharedPreferences sharedPreferences) {
        return new SubscriptionManager(sharedPreferences);
    }
}
