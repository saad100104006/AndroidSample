package uk.co.transferx.app.dagger.component;

import javax.inject.Singleton;

import dagger.Component;
import uk.co.transferx.app.BaseActivity;
import uk.co.transferx.app.TransferXApplication;
import uk.co.transferx.app.dagger.module.AppModule;
import uk.co.transferx.app.dagger.module.NetworkModule;
import uk.co.transferx.app.mainscreen.fragments.ActivityFragment;
import uk.co.transferx.app.mainscreen.fragments.RecipientDialogFragment;
import uk.co.transferx.app.mainscreen.fragments.RecipientsFragment;
import uk.co.transferx.app.mainscreen.fragments.TransferFragment;
import uk.co.transferx.app.mainscreen.schedule.CalendarActivity;
import uk.co.transferx.app.mainscreen.schedule.EndTransferActivity;
import uk.co.transferx.app.mainscreen.schedule.RepeatTransferActivity;
import uk.co.transferx.app.mainscreen.schedule.ReviewActivity;
import uk.co.transferx.app.mainscreen.schedule.ScheduleActivity;
import uk.co.transferx.app.mainscreen.schedule.TimeActivity;
import uk.co.transferx.app.recipients.addrecipients.fragments.AddRecipientsFragment;
import uk.co.transferx.app.recipients.detailsrecipient.fragment.RecipientDetailsFragment;
import uk.co.transferx.app.recipients.editrecipient.EditRecipientActivity;
import uk.co.transferx.app.recoverpass.fragment.RecoverPasswordFragment;
import uk.co.transferx.app.settings.fragment.SettingsFragment;
import uk.co.transferx.app.settings.notification.NotificationSettingsActivity;
import uk.co.transferx.app.settings.profile.ProfileActivity;
import uk.co.transferx.app.settings.profile.changepassword.ChangePasswordActivity;
import uk.co.transferx.app.settings.profile.personaldetails.PersonalDetailsActivity;
import uk.co.transferx.app.settings.profile.personaldetails.fragments.PersonalDetailsFragmentOne;
import uk.co.transferx.app.settings.profile.personaldetails.fragments.PersonalDetailsFragmentTwo;
import uk.co.transferx.app.settings.profile.wallet.AddCardActivity;
import uk.co.transferx.app.settings.profile.wallet.WalletActivity;
import uk.co.transferx.app.signin.fragment.SignInEmailFragment;
import uk.co.transferx.app.signin.fragment.SignInPinFragment;
import uk.co.transferx.app.signup.SignUpActivity;
import uk.co.transferx.app.signup.fragment.SignUpStepOneFragment;
import uk.co.transferx.app.signup.fragment.SignUpStepThreeFragment;
import uk.co.transferx.app.signup.fragment.SignUpStepTwoFragment;
import uk.co.transferx.app.splash.SplashActivity;
import uk.co.transferx.app.transfersummary.TransferSummaryActivity;
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

    void inject(TransferFragment transferFragment);

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

    void inject(ProfileActivity profileActivity);

    void inject(RecoverPasswordFragment recoverPasswordFragment);

    void inject(SignInPinFragment signInPinFragment);

    void inject(SignUpActivity signUpActivity);

    void inject(PersonalDetailsActivity personalDetailsActivity);

    void inject(PersonalDetailsFragmentOne personalDetailsFragmentOne);

    void inject(PersonalDetailsFragmentTwo personalDetailsFragmentTwo);

    void inject(WalletActivity walletActivity);

    void inject(ChangePasswordActivity changePasswordActivity);

    void inject(EditRecipientActivity editRecipientActivity);

    void inject(ScheduleActivity scheduleActivity);

    void inject(AddCardActivity addCardActivity);

    void inject(TransferSummaryActivity transferSummaryActivity);

    void inject(CalendarActivity calendarActivity);

    void inject(TimeActivity timeActivity);

    void inject(RepeatTransferActivity repeatTransferActivity);

    void inject(EndTransferActivity endTransferActivity);

    void inject(ReviewActivity reviewActivity);

    void inject(BaseActivity baseActivity);
}
