package uk.co.transferx.app.ui.tutorial.fragments

import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.tutorial_fragment_layout.*
import org.jetbrains.anko.startActivity
import uk.co.transferx.app.R
import uk.co.transferx.app.ui.base.BaseFragment
import uk.co.transferx.app.ui.landing.LandingActivity
import uk.co.transferx.app.util.Constants.*

/**
 * Created by sergey on 21/03/2018.
 */

class TutorialFragment : BaseFragment() {
    @DrawableRes
    private var image = -1

    @DrawableRes
    private var backgroundColor = 0

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
        arguments?.let {
            backgroundColor = it.getInt(BACKGROUND_COLOR)
            image = it.getInt(TUTORIAL_IMG)
            descriptionOne = it.getInt(TITLE)
            descriptionTwo = it.getInt(DESCRIPTION)
            buttonText = it.getInt(BUTTON_TEXT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.tutorial_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rootLayout.setBackgroundResource(backgroundColor)
        tutorial_img.setImageDrawable(ContextCompat.getDrawable(context!!, image))

        title.setText(descriptionOne)
        description.setText(descriptionTwo)

        buttonAction.setText(buttonText)
        buttonAction.setOnClickListener {
            activity?.startActivity<LandingActivity>()
            activity?.finish()
        }
    }
}
