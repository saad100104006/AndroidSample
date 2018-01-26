package uk.co.transferx.app.firebase;

import android.content.SharedPreferences;

import com.google.firebase.messaging.FirebaseMessaging;

import javax.inject.Inject;

/**
 * Created by sergey on 25/01/2018.
 */

public class SubscriptionManager {

    private final static String ALERTS = "alerts";
    private final static String PAYMENTS = "payments";
    private final static String NEWS = "news";
    private final static String NOTIFICATION = "notification";
    private final static String SECOND_TIMES = "second_times";
    private final FirebaseMessaging firebaseMessaging;


    private final SharedPreferences sharedPreferences;

    @Inject
    public SubscriptionManager(final SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
        firebaseMessaging = FirebaseMessaging.getInstance();
    }

    public void initSubscribtions() {
        if (!sharedPreferences.getBoolean(SECOND_TIMES, false)) {
            setAllNotification(true);
            sharedPreferences.edit().putBoolean(SECOND_TIMES, true).apply();
            return;
        }
        if (!sharedPreferences.getBoolean(NOTIFICATION, false)) {
            return;
        }
        subscribeToNews(sharedPreferences.getBoolean(NEWS, false));
        subscribeToPayments(sharedPreferences.getBoolean(PAYMENTS, false));
        subscribeToAlerts(sharedPreferences.getBoolean(ALERTS, false));
    }


    public void subscribeToNews(boolean shouldSubscribe) {
        if (shouldSubscribe) {
            firebaseMessaging.subscribeToTopic(NEWS);
        } else {
            firebaseMessaging.unsubscribeFromTopic(NEWS);
        }
        sharedPreferences.edit().putBoolean(NEWS, shouldSubscribe).apply();
    }

    public void subscribeToPayments(boolean shouldSubscribe) {
        if (shouldSubscribe) {
            firebaseMessaging.subscribeToTopic(PAYMENTS);
        } else {
            firebaseMessaging.unsubscribeFromTopic(PAYMENTS);
        }
        sharedPreferences.edit().putBoolean(PAYMENTS, shouldSubscribe).apply();
    }

    public void subscribeToAlerts(boolean shouldSubscribe) {
        if (shouldSubscribe) {
            firebaseMessaging.subscribeToTopic(ALERTS);
        } else {
            firebaseMessaging.unsubscribeFromTopic(ALERTS);
        }
        sharedPreferences.edit().putBoolean(ALERTS, shouldSubscribe).apply();
    }

    public boolean isNewsSubscribed() {
        return sharedPreferences.getBoolean(NEWS, false);
    }

    public boolean isPaymentsSubscribed() {
        return sharedPreferences.getBoolean(PAYMENTS, false);
    }

    public boolean isAlertsSubscribed() {
        return sharedPreferences.getBoolean(ALERTS, false);
    }

    public boolean isNotificationSubscribed() {
        return sharedPreferences.getBoolean(NOTIFICATION, false);
    }

    public void setAllNotification(boolean subscribe) {
        subscribeToNews(subscribe);
        subscribeToPayments(subscribe);
        subscribeToAlerts(subscribe);
        sharedPreferences.edit().putBoolean(NOTIFICATION, subscribe).apply();
    }


}
