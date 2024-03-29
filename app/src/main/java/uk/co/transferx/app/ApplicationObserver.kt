package uk.co.transferx.app

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.SharedPreferences
import android.os.SystemClock
import android.util.Log
import uk.co.transferx.app.util.Constants.PIN_REQUIRED
import javax.inject.Inject

class ApplicationObserver @Inject constructor(val sharedPreferences: SharedPreferences) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onForeground() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onBackground() {
      sharedPreferences.edit().putBoolean(PIN_REQUIRED, true).apply()
    }

}