package uk.co.transferx.app.dagger.component;

import dagger.Component;
import uk.co.transferx.app.dagger.module.AppModule;
import uk.co.transferx.app.mainscreen.fragments.SendFragment;
import uk.co.transferx.app.signup.fragment.SignUpStepThreeFragment;
import uk.co.transferx.app.signup.fragment.SignUpStepTwoFragment;
import uk.co.transferx.app.splash.SplashActivity;
import uk.co.transferx.app.welcom.fragment.WelcomeFragment;

/**
 * Created by smilevkiy on 13.11.17.
 */

@Component(modules = {AppModule.class})
public interface AppComponent {
    void inject(WelcomeFragment welcomeFragment);

    void inject(SplashActivity splashActivity);

    void inject(SignUpStepThreeFragment signUpStepThreeFragment);

    void inject(SignUpStepTwoFragment signUpStepTwoFragment);

    void inject(SendFragment sendFragment);
}
