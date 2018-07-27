package uk.co.transferx.app.mainscreen.schedule

import android.content.SharedPreferences
import uk.co.transferx.app.BasePresenter
import uk.co.transferx.app.UI
import javax.inject.Inject

class ScheduleActivityPresenter @Inject constructor(sharedPreferences: SharedPreferences) : BasePresenter<ScheduleActivityPresenter.ScheduleActivityUI>(sharedPreferences) {


    interface ScheduleActivityUI : UI {

    }
}