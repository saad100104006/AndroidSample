package uk.co.transferx.app.util.schedulers

import android.support.annotation.NonNull
import io.reactivex.Scheduler



/**
 * Allow providing different types of [Scheduler]s.
 */
interface BaseSchedulerProvider {

    @NonNull
    fun computation(): Scheduler

    @NonNull
    fun io(): Scheduler

    @NonNull
    fun ui(): Scheduler
}