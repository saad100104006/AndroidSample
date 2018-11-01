package uk.co.transferx.app.util.schedulers

import android.support.annotation.NonNull
import android.support.annotation.Nullable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.Schedulers.computation
import io.reactivex.schedulers.Schedulers.io
import io.reactivex.Scheduler


/**
 * Provides different types of schedulers.
 */
class SchedulerProvider// Prevent direct instantiation.
private constructor() : BaseSchedulerProvider {

    override fun computation(): Scheduler {
        return Schedulers.computation()
    }

    override fun io(): Scheduler {
        return Schedulers.io()
    }

    override fun ui(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    companion object {

        private var INSTANCE: SchedulerProvider? = null

        val instance: SchedulerProvider
            @Synchronized get() {
                if (INSTANCE == null) {
                    INSTANCE = SchedulerProvider()
                }
                return INSTANCE!!
            }
    }
}