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

import uk.co.transferx.app.ui.homescreen.MainActivity;
import uk.co.transferx.app.ui.homescreen.fragments.FragActivity;
import uk.co.transferx.app.ui.homescreen.fragments.FragRecipients;
import uk.co.transferx.app.ui.settings.profile.AccountActivity;
import uk.co.transferx.app.ui.settings.profile.account.PersonalInformationActivity;
import uk.co.transferx.app.ui.settings.fragment.FragSettings;
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
import uk.co.transferx.app.ui.recipients.addrecipients.fragments.AddFirstRecipientSuccessFragment;
import uk.co.transferx.app.ui.recipients.addrecipients.fragments.AddRecipientSuccessFragment;
import uk.co.transferx.app.ui.recipients.addrecipients.fragments.AddRecipientsFragment;
import uk.co.transferx.app.ui.recoverpass.fragment.RecoverPasswordFragment;
import uk.co.transferx.app.ui.settings.fragment.SettingsFragment;
import uk.co.transferx.app.ui.settings.notification.NotificationSettingsActivity;
import uk.co.transferx.app.ui.settings.profile.UploadDocumentsActivity;
import uk.co.transferx.app.ui.settings.profile.changepassword.ChangePasswordActivity;
import uk.co.transferx.app.ui.settings.profile.changepin.ChangePinActivity;
import uk.co.transferx.app.ui.settings.profile.changepin.fragment.ChangePinFragment;
import uk.co.transferx.app.ui.settings.profile.personaldetails.PersonalDetailsActivity;
import uk.co.transferx.app.ui.settings.profile.personaldetails.fragments.PersonalDetailsFragmentOne;
import uk.co.transferx.app.ui.settings.profile.personaldetails.fragments.PersonalDetailsFragmentTwo;
import uk.co.transferx.app.ui.settings.profile.wallet.AddCardActivity;
import uk.co.transferx.app.ui.settings.profile.wallet.WalletActivity;
import uk.co.transferx.app.ui.settings.support.SupportActivity;
import uk.co.transferx.app.ui.signin.SignInActivity;
import uk.co.transferx.app.ui.signin.fragment.SignInFragment;
import uk.co.transferx.app.ui.signinpin.fragment.SignInEmailFragment;
import uk.co.transferx.app.ui.signinpin.fragment.SignInPinFragment;
import uk.co.transferx.app.ui.signup.SignUpActivity;
import uk.co.transferx.app.ui.signup.fragment.SignUpStepOneFragment;
import uk.co.transferx.app.ui.signup.fragment.SignUpStepThreeFragment;
import uk.co.transferx.app.ui.signup.fragment.SignUpStepTwoFragment;
import uk.co.transferx.app.ui.signup.fragment.SignUpSuccessFragment;
import uk.co.transferx.app.ui.splash.SplashActivity;
import uk.co.transferx.app.ui.transfer.TransferActivity;
import uk.co.transferx.app.ui.transfer.fragment.SelectRecipientFragment;
import uk.co.transferx.app.ui.transfersummary.TransferSummaryActivity;

/**
 * Created by smilevkiy on 13.11.17.
 */
@Singleton
@Component(modules = {AppModule.class, NetworkModule.class})
public interface AppComponent {
    void inject(LandingActivity landingActivity);

    void inject(LandingFragment landingFragment);

    void inject(SplashActivity splashActivity);

    void inject(SignInFragment signInFragment);

    void inject(SignInActivity signInActivity);

    void inject(SignUpStepThreeFragment signUpStepThreeFragment);

    void inject(ChangePinFragment changePinFragment);

    void inject(SignUpStepTwoFragment signUpStepTwoFragment);

    void inject(SignUpSuccessFragment signUpSuccessFragment);

    void inject(TransferFragment transferFragment);

    void inject(RecipientsFragment recipientsFragment);

    void inject(ActivityFragment activityFragment);

    void inject(SignUpStepOneFragment signUpStepOneFragment);

    void inject(SignInEmailFragment signInEmailFragment);

    void inject(SettingsFragment settingsFragment);

    void inject(AddRecipientsFragment addRecipientsFragment);

    void inject(AddRecipientSuccessFragment addRecipientSuccessFragment);

    void inject(AddFirstRecipientSuccessFragment addFirstRecipientSuccessFragment);

    void inject(TransferXApplication transferXApplication);

    void inject(RecipientDialogFragment recipientDialogFragment);

    void inject(NotificationSettingsActivity notificationSettingsActivity);

    void inject(AccountActivity accountActivity);

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

    void inject (TransferActivity transferActivity);

    void inject (SelectRecipientFragment selectRecipientFragment);

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

    void inject(MainActivity mainActivity);

    void inject(FragActivity fragActivity);

    void inject(FragRecipients fragRecipients);

    void inject(FragSettings fragSettings);

    void inject(PersonalInformationActivity activityAccount);

    void inject(ChangePinActivity changePin);
}
