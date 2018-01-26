package uk.co.transferx.app.firebase;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by sergey on 25/01/2018.
 */

public class TransferXFirebaseIDService extends FirebaseInstanceIdService {



    @Override
    public void onTokenRefresh() {

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("Sergey ", "Refreshed token: " + refreshedToken);

    }

    private void sendRegistrationToServer(String token) {

    }
}
