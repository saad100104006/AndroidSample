package uk.co.transferx.app.tutorial.fragments

import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.tutorial_fragment_layout_last.*
import uk.co.transferx.app.BaseFragment
import uk.co.transferx.app.R
import uk.co.transferx.app.util.Constants.*
import uk.co.transferx.app.welcom.WelcomeActivity

/**
 * Created by sergey on 21/03/2018.
 */

class TutorialFragment : BaseFragment() {
    @DrawableRes
    private var image = -1

    @StringRes
    private var descriptionOne: Int = 0

    @StringRes
    private var descriptionTwo: Int = 0

    @StringRes
    private var buttonText: Int = 0

    override fun tagName(): String {
        return TutorialFragment::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // get bundled data
        val bundle = arguments
        if (bundle != null) {
            image = bundle.getInt(TUTORIAL_IMG)
            descriptionOne = bundle.getInt(TITLE)
            descriptionTwo = bundle.getInt(DESCRIPTION)
            buttonText = bundle.getInt(BUTTON_TEXT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.tutorial_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tutorial_img.setImageDrawable(ContextCompat.getDrawable(context!!, image))
        title.setText(descriptionOne)
        description.setText(descriptionTwo)

        skip.setText(buttonText)
        skip.setOnClickListener { WelcomeActivity.startWelcomeActivity(activity) }
    }
}
