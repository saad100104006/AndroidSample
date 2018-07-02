package uk.co.transferx.app.tutorial.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import uk.co.transferx.app.R;
import uk.co.transferx.app.tutorial.fragments.TutorialFragment;

import static uk.co.transferx.app.util.Constants.BUTTON_TEXT;
import static uk.co.transferx.app.util.Constants.DESCRIPTION_ONE;
import static uk.co.transferx.app.util.Constants.DESCRIPTION_TWO;
import static uk.co.transferx.app.util.Constants.LAYOUT;
import static uk.co.transferx.app.util.Constants.TUTORIAL_IMG;

/**
 * Created by sergey on 21/03/2018.
 */

public class TutorialAdapter extends FragmentStatePagerAdapter {

    private final static int PAGES = 5;
    private final static int FIRST_PAGE = 0;
    private final static int SECOND_PAGE = 1;
    private final static int THIRD_PAGE = 2;
    private final static int FORTH_PAGE = 3;
    private final static int FIVE_PAGE = 4;


    public TutorialAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        switch (position) {
            case FIRST_PAGE:
                bundle.putInt(LAYOUT, R.layout.tutorial_fragment_first);
                TutorialFragment firstTutorialPage = new TutorialFragment();
                firstTutorialPage.setArguments(bundle);
                return firstTutorialPage;
            case SECOND_PAGE:
                bundle.putInt(LAYOUT, R.layout.tutorial_fragment_layout);
                bundle.putInt(TUTORIAL_IMG, R.drawable.ic_security);
                bundle.putInt(DESCRIPTION_ONE, R.string.security);
                bundle.putInt(DESCRIPTION_TWO, R.string.security_desc);
                TutorialFragment secondTutorialPage = new TutorialFragment();
                secondTutorialPage.setArguments(bundle);
                return secondTutorialPage;
            case THIRD_PAGE:
                bundle.putInt(LAYOUT, R.layout.tutorial_fragment_layout);
                bundle.putInt(TUTORIAL_IMG, R.drawable.ic_auto_transfers);
                bundle.putInt(DESCRIPTION_ONE, R.string.automated_transfer);
                bundle.putInt(DESCRIPTION_TWO, R.string.second_page_desc);
                TutorialFragment threeTutorialPage = new TutorialFragment();
                threeTutorialPage.setArguments(bundle);
                return threeTutorialPage;
            case FORTH_PAGE:
                bundle.putInt(LAYOUT, R.layout.tutorial_fragment_layout_last);
                bundle.putInt(TUTORIAL_IMG, R.drawable.ic_recipients);
                bundle.putInt(DESCRIPTION_ONE, R.string.recipients);
                bundle.putInt(DESCRIPTION_TWO, R.string.three_page_desc);
                bundle.putInt(BUTTON_TEXT, R.string.add_recipientss);
                TutorialFragment fourTutorialPage = new TutorialFragment();
                fourTutorialPage.setArguments(bundle);
                return fourTutorialPage;
            case FIVE_PAGE:
                bundle.putInt(LAYOUT, R.layout.tutorial_fragment_layout_last);
                bundle.putInt(TUTORIAL_IMG, R.drawable.ic_credit_card);
                bundle.putInt(DESCRIPTION_ONE, R.string.title_flex);
                bundle.putInt(DESCRIPTION_TWO, R.string.flexible_payment_desc);
                bundle.putInt(BUTTON_TEXT, R.string.add_payment_method);
                TutorialFragment fiveTutorialPage = new TutorialFragment();
                fiveTutorialPage.setArguments(bundle);
                return fiveTutorialPage;
            default:
                throw new IllegalStateException("item is illegal" + position);
        }
    }

    @Override
    public int getCount() {
        return PAGES;
    }
}
