package uk.co.transferx.app.ui.tutorial

import android.os.Bundle
import kotlinx.android.synthetic.main.tutorial_activity_layout.*
import uk.co.transferx.app.R
import uk.co.transferx.app.ui.base.BaseActivity
import uk.co.transferx.app.ui.tutorial.adapter.TutorialAdapter

/**
 * Created by sergey on 21/03/2018.
 */

class TutorialActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tutorial_activity_layout)

        pager.adapter = TutorialAdapter(supportFragmentManager)
    }

}
