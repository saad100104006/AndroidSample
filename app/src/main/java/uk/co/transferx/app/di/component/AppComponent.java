package uk.co.transferx.app.di.component;

import javax.inject.Singleton;

import dagger.Component;
import uk.co.transferx.app.ui.base.BaseActivity;
import uk.co.transferx.app.TransferXApplication;
import uk.co.transferx.app.di.module.AppModule;
import uk.co.transferx.app.di.module.NetworkModule;
import uk.co.transferx.app.data.firebase.TransferXFirebaseIDService;
import uk.co.transferx.app.ui.landing.LandingActivity;
import uk.co.transferx.app.ui.landing.fragment.LandingFragment;
import uk.co.transferx.app.ui.mainscreen.fragments.ActivityFragment;
import uk.co.transferx.app.ui.mainscreen.fragments.RecipientDialogFragment;
import uk.co.transferx.app.ui.mainscreen.fragments.RecipientsFragment;
import uk.co.transferx.app.ui.mainscreen.fragments.TransferFragment;
import uk.co.transferx.app.ui.mainscreen.schedule.CalendarActivity;
import uk.co.transferx.app.ui.mainscreen.schedule.EndTransferActivity;
import uk.co.transferx.app.ui.mainscreen.schedule.RepeatTransferActivity;
import uk.co.transferx.app.ui.mainscreen.schedule.ReviewActivity;
import uk.co.transferx.app.ui.mainscreen.schedule.ScheduleActivity;
import uk.co.transferx.app.ui.mainscreen.schedule.TimeActivity;
import uk.co.transferx.app.ui.recipients.addrecipients.fragments.AddRecipientsFragment;
import uk.co.transferx.app.ui.recoverpass.fragment.RecoverPasswordFragment;
import uk.co.transferx.app.ui.settings.fragment.SettingsFragment;
import uk.co.transferx.app.ui.settings.notification.NotificationSettingsActivity;
import uk.co.transferx.app.ui.settings.profile.ProfileActivity;
import uk.co.transferx.app.ui.settings.profile.UploadDocumentsActivity;
import uk.co.transferx.app.ui.settings.profile.changepassword.ChangePasswordActivity;
import uk.co.transferx.app.ui.settings.profile.personaldetails.PersonalDetailsActivity;
import uk.co.transferx.app.ui.settings.profile.personaldetails.fragments.PersonalDetailsFragmentOne;
import uk.co.transferx.app.ui.settings.profile.personaldetails.fragments.PersonalDetailsFragmentTwo;
import uk.co.transferx.app.ui.settings.profile.wallet.AddCardActivity;
import uk.co.transferx.app.ui.settings.profile.wallet.WalletActivity;
import uk.co.transferx.app.ui.settings.support.SupportActivity;
import uk.co.transferx.app.ui.signin.fragment.SignInEmailFragment;
import uk.co.transferx.app.ui.signin.fragment.SignInPinFragment;
import uk.co.transferx.app.ui.signup.SignUpActivity;
import uk.co.transferx.app.ui.signup.fragment.SignUpStepTwoFragment;
import uk.co.transferx.app.ui.signup.fragment.SignUpStepThreeFragment;
import uk.co.transferx.app.ui.signup.fragment.SignUpStepOneFragment;
import uk.co.transferx.app.ui.signup.fragment.SignUpSuccessFragment;
import uk.co.transferx.app.ui.splash.SplashActivity;
import uk.co.transferx.app.ui.transfersummary.TransferSummaryActivity;
import uk.co.transferx.app.ui.welcom.fragment.WelcomeFragment;

/**
 * Created by smilevkiy on 13.11.17.
 */
@Singleton
@Component(modules = {AppModule.class, NetworkModule.class})
public interface AppComponent {
    void inject(WelcomeFragment welcomeFragment);

    void inject(LandingActivity landingActivity);

    void inject(LandingFragment landingFragment);

    void inject(SplashActivity splashActivity);

    void inject(SignUpStepThreeFragment signUpStepThreeFragment);

    void inject(SignUpStepOneFragment signUpStepOneFragment);

    void inject(SignUpSuccessFragment signUpSuccessFragment);

    void inject(TransferFragment transferFragment);

    void inject(RecipientsFragment recipientsFragment);

    void inject(ActivityFragment activityFragment);

    void inject(SignUpStepTwoFragment signUpStepTwoFragment);

    void inject(SignInEmailFragment signInEmailFragment);

    void inject(SettingsFragment settingsFragment);

    void inject(AddRecipientsFragment addRecipientsFragment);

    void inject(TransferXApplication transferXApplication);

    void inject(RecipientDialogFragment recipientDialogFragment);

    void inject(NotificationSettingsActivity notificationSettingsActivity);

    void inject(ProfileActivity profileActivity);

    void inject(RecoverPasswordFragment recoverPasswordFragment);

    void inject(SignInPinFragment signInPinFragment);

    void inject(SignUpActivity signUpActivity);

    void inject(PersonalDetailsActivity personalDetailsActivity);

    void inject(PersonalDetailsFragmentOne personalDetailsFragmentOne);

    void inject(PersonalDetailsFragmentTwo personalDetailsFragmentTwo);

    void inject(WalletActivity walletActivity);

    void inject(ChangePasswordActivity changePasswordActivity);


    void inject(ScheduleActivity scheduleActivity);

    void inject(AddCardActivity addCardActivity);

    void inject(TransferSummaryActivity transferSummaryActivity);

    void inject(CalendarActivity calendarActivity);

    void inject(TimeActivity timeActivity);

    void inject(RepeatTransferActivity repeatTransferActivity);

    void inject(EndTransferActivity endTransferActivity);

    void inject(ReviewActivity reviewActivity);

    void inject(SupportActivity supportActivity);

    void inject(BaseActivity baseActivity);

    void inject(UploadDocumentsActivity uploadDocumentsActivity);

    void inject(TransferXFirebaseIDService transferXFirebaseIDService);
}
