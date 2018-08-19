package uk.co.transferx.app.firebase;

import android.content.SharedPreferences;

import com.google.firebase.messaging.FirebaseMessagingService;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;
import uk.co.transferx.app.TransferXApplication;
import uk.co.transferx.app.api.FcmMessagAPI;
import uk.co.transferx.app.pojo.UpdateFCMToken;
import uk.co.transferx.app.tokenmanager.TokenManager;

import static uk.co.transferx.app.util.Constants.LOGGED_IN_STATUS;

/**
 * Created by sergey on 25/01/2018.
 */

public class TransferXFirebaseIDService extends FirebaseMessagingService {

    @Inject
    SharedPreferences sharedPreferences;
    @Inject
    FcmMessagAPI fcmMessagAPI;
    @Inject
    TokenManager tokenManager;
    private Disposable disposable = Disposables.disposed();

    @Override
    public void onCreate() {
        super.onCreate();
        ((TransferXApplication) getApplication()).getAppComponent().inject(this);

    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        if (sharedPreferences.getBoolean(LOGGED_IN_STATUS, false)) {
            sendRegistrationToServer(s);
        }
    }

    private void sendRegistrationToServer(String token) {
        disposable = tokenManager.getToken()
                .flatMap(tokenEn -> fcmMessagAPI.updateToken(tokenEn.getAccessToken(), new UpdateFCMToken(token, "android")))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> Timber.d("Status %s", result.code()), Timber::e);
    }

    @Override
    public void onDestroy() {
        disposable.dispose();
        super.onDestroy();
    }
}
