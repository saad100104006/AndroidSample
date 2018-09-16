package uk.co.transferx.app.di.module;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.co.transferx.app.BuildConfig;
import uk.co.transferx.app.data.remote.CardsApi;
import uk.co.transferx.app.data.remote.FcmMessagAPI;
import uk.co.transferx.app.data.remote.ProfileApi;
import uk.co.transferx.app.data.remote.RecipientsApi;
import uk.co.transferx.app.data.remote.SignInOutApi;
import uk.co.transferx.app.data.remote.SignUpApi;
import uk.co.transferx.app.data.remote.TransactionApi;
import uk.co.transferx.app.data.repository.recipientsrepository.RecipientRepository;
import uk.co.transferx.app.data.repository.recipientsrepository.RecipientRepositoryImpl;
import uk.co.transferx.app.data.repository.tokenmanager.TokenManager;

/**
 * Created by sergey on 24/12/2017.
 */
@Module
public class NetworkModule {


    private String baseUrl;

    public NetworkModule(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Singleton
    @Provides
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder.create();
    }

    @Singleton
    @Provides
    OkHttpClient provideOkhttpClient() {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            client.addInterceptor(interceptor);
        }
        return client.build();
    }

    @Singleton
    @Provides
    Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .build();

    }

    @Singleton
    @Provides
    SignUpApi provideSignUpApi(Retrofit retrofit) {
        return retrofit.create(SignUpApi.class);
    }

    @Singleton
    @Provides
    SignInOutApi provideSignInApi(Retrofit retrofit) {
        return retrofit.create(SignInOutApi.class);
    }

    @Singleton
    @Provides
    RecipientsApi provideRecipientsApi(Retrofit retrofit) {
        return retrofit.create(RecipientsApi.class);
    }

    @Singleton
    @Provides
    RecipientRepository provideRecipientRepository(RecipientsApi recipientsApi, TokenManager tokenManager) {
        return new RecipientRepositoryImpl(recipientsApi, tokenManager);
    }

    @Singleton
    @Provides
    TransactionApi provideTransactionApi(Retrofit retrofit) {
        return retrofit.create(TransactionApi.class);
    }

    @Singleton
    @Provides
    ProfileApi provideProfileApi(Retrofit retrofit) {
        return retrofit.create(ProfileApi.class);
    }

    @Singleton
    @Provides
    CardsApi provideCardsApi(Retrofit retrofit) {
        return retrofit.create(CardsApi.class);
    }

    @Singleton
    @Provides
    FcmMessagAPI provideFcmMessageApi(Retrofit retrofit) {
        return retrofit.create(FcmMessagAPI.class);
    }

}
