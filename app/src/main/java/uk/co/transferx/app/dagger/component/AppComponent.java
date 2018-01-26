package uk.co.transferx.app.dagger.component;

import javax.inject.Singleton;

import dagger.Component;
import uk.co.transferx.app.TransferXApplication;
import uk.co.transferx.app.dagger.module.AppModule;
import uk.co.transferx.app.dagger.module.NetworkModule;
import uk.co.transferx.app.mainscreen.fragments.ActivityFragment;
import uk.co.transferx.app.mainscreen.fragments.RecipientDialogFragment;
import uk.co.transferx.app.mainscreen.fragments.RecipientsFragment;
import uk.co.transferx.app.mainscreen.fragments.SendFragment;
import uk.co.transferx.app.recipients.addrecipients.fragments.AddRecipientsFragment;
import uk.co.transferx.app.recipients.detailsrecipient.fragment.RecipientDetailsFragment;
import uk.co.transferx.app.settings.fragment.SettingsFragment;
import uk.co.transferx.app.settings.notification.NotificationSettingsActivity;
import uk.co.transferx.app.signin.fragment.SignInEmailFragment;
import uk.co.transferx.app.signup.fragment.SignUpStepOneFragment;
import uk.co.transferx.app.signup.fragment.SignUpStepThreeFragment;
import uk.co.transferx.app.signup.fragment.SignUpStepTwoFragment;
import uk.co.transferx.app.splash.SplashActivity;
import uk.co.transferx.app.welcom.fragment.WelcomeFragment;

/**
 * Created by smilevkiy on 13.11.17.
 */
@Singleton
@Component(modules = {AppModule.class, NetworkModule.class})
public interface AppComponent {
    void inject(WelcomeFragment welcomeFragment);

    void inject(SplashActivity splashActivity);

    void inject(SignUpStepThreeFragment signUpStepThreeFragment);

    void inject(SignUpStepTwoFragment signUpStepTwoFragment);

    void inject(SendFragment sendFragment);

    void inject(RecipientsFragment recipientsFragment);

    void inject(ActivityFragment activityFragment);

    void inject(SignUpStepOneFragment signUpStepOneFragment);

    void inject(SignInEmailFragment signInEmailFragment);

    void inject(SettingsFragment settingsFragment);

    void inject(AddRecipientsFragment addRecipientsFragment);

    void inject(RecipientDetailsFragment recipientDetailsFragment);

    void inject(TransferXApplication transferXApplication);

    void inject(RecipientDialogFragment recipientDialogFragment);

    void inject(NotificationSettingsActivity notificationSettingsActivity);
}
