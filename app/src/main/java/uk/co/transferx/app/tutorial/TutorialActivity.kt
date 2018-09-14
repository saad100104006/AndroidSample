package uk.co.transferx.app.tutorial

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager

import uk.co.transferx.app.BaseActivity
import uk.co.transferx.app.R
import uk.co.transferx.app.tutorial.adapter.TutorialAdapter

/**
 * Created by sergey on 21/03/2018.
 */

class TutorialActivity : BaseActivity() {

    private var viewPager: ViewPager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tutorial_activity_layout)

        viewPager = findViewById(R.id.pager)
        val tutorialAdapter = TutorialAdapter(supportFragmentManager)
        viewPager!!.adapter = tutorialAdapter
    }

}
