package uk.co.transferx.app.settings.notification;

import javax.inject.Inject;

import uk.co.transferx.app.BasePresenter;
import uk.co.transferx.app.UI;
import uk.co.transferx.app.firebase.SubscriptionManager;

/**
 * Created by sergey on 26/01/2018.
 */

public class NotificationSettingsPresenter extends BasePresenter<NotificationSettingsPresenter.NotificationSettingsUI> {


    private final SubscriptionManager subscriptionManager;
    private boolean isInitialized;

    @Inject
    public NotificationSettingsPresenter(final SubscriptionManager subscriptionManager) {
        this.subscriptionManager = subscriptionManager;
    }

    @Override
    public void attachUI(NotificationSettingsUI ui) {
        super.attachUI(ui);
        if (!isInitialized) {
            this.ui.setAlerts(subscriptionManager.isAlertsSubscribed());
            this.ui.setNews(subscriptionManager.isNewsSubscribed());
            this.ui.setNotifications(subscriptionManager.isNotificationSubscribed());
            this.ui.setPayments(subscriptionManager.isPaymentsSubscribed());
            isInitialized = true;
        }
    }


    public void setNewsSubscribtion(boolean subscription) {
        subscriptionManager.subscribeToNews(subscription);
    }

    public void setAlertSubscribtion(boolean subscribtion) {
        subscriptionManager.subscribeToAlerts(subscribtion);
    }

    public void setPaymentsSubscribtion(boolean subscribtion) {
        subscriptionManager.subscribeToPayments(subscribtion);
    }

    public void setNotificationSubscribtion(boolean subscribtion) {
        subscriptionManager.setAllNotification(subscribtion);
    }

    public interface NotificationSettingsUI extends UI {

        void setNews(boolean subscribed);

        void setAlerts(boolean subscribed);

        void setNotifications(boolean subscribed);

        void setPayments(boolean subscribed);

    }
}
