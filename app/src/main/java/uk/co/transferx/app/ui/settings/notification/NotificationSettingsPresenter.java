package uk.co.transferx.app.ui.settings.notification;

import javax.inject.Inject;

import uk.co.transferx.app.ui.base.BasePresenter;
import uk.co.transferx.app.ui.base.UI;
import uk.co.transferx.app.data.firebase.SubscriptionManager;

/**
 * Created by sergey on 26/01/2018.
 */

public class NotificationSettingsPresenter extends BasePresenter<NotificationSettingsPresenter.NotificationSettingsUI> {


    private final SubscriptionManager subscriptionManager;

    @Inject
    public NotificationSettingsPresenter(final SubscriptionManager subscriptionManager) {
        this.subscriptionManager = subscriptionManager;
    }

    @Override
    public void attachUI(NotificationSettingsUI ui) {
        super.attachUI(ui);

    }

    public boolean isNotificationSubscribed() {
        return subscriptionManager.isNotificationSubscribed();
    }

    public boolean isPaymentsSubscribed() {
        return subscriptionManager.isPaymentsSubscribed();
    }


    public boolean isAlertSubscribed() {
        return subscriptionManager.isAlertsSubscribed();
    }

    public boolean isNewsSubscribed() {
        return subscriptionManager.isNewsSubscribed();
    }

    public void setNewsSubscribtion(boolean subscription) {
        subscriptionManager.subscribeToNews(subscription);
        if (subscription && !subscriptionManager.isNotificationSubscribed()) {
            subscriptionManager.setNotificationOn();
            ui.setNotificationOn();
        }
    }

    public void setAlertSubscribtion(boolean subscription) {
        subscriptionManager.subscribeToAlerts(subscription);
        if (subscription && !subscriptionManager.isNotificationSubscribed()) {
            subscriptionManager.setNotificationOn();
            ui.setNotificationOn();
        }
    }

    public void setPaymentsSubscribtion(boolean subscription) {
        subscriptionManager.subscribeToPayments(subscription);
        if (subscription && !subscriptionManager.isNotificationSubscribed()) {
            subscriptionManager.setNotificationOn();
            ui.setNotificationOn();
        }
    }

    public void setNotificationSubscribtion(boolean subscribtion) {
        subscriptionManager.setAllNotification(subscribtion);
        ui.setPayments(subscribtion);
        ui.setNews(subscribtion);
        ui.setAlerts(subscribtion);
    }

    public interface NotificationSettingsUI extends UI {

        void setNews(boolean subscribed);

        void setAlerts(boolean subscribed);

        void setPayments(boolean subscribed);

        void setNotificationOn();

    }
}
