package uk.co.transferx.app.dagger.module;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import uk.co.transferx.app.api.SignUpApi;
import uk.co.transferx.app.crypto.CryptoManager;
import uk.co.transferx.app.firebase.SubscriptionManager;
import uk.co.transferx.app.tokenmanager.TokenManager;
import uk.co.transferx.app.tokenmanager.TokenRepository;

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
    TokenManager providesTokenManager(TokenRepository tokenRepository, SignUpApi signUpApi) {
        return new TokenManager(tokenRepository, signUpApi);
    }

    @Singleton
    @Provides
    SubscriptionManager provideSubscribtionManager(SharedPreferences sharedPreferences) {
        return new SubscriptionManager(sharedPreferences);
    }
}
