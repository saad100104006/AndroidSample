package uk.co.transferx.app.util.schedulers

import android.support.annotation.NonNull
import io.reactivex.schedulers.Schedulers
import io.reactivex.Scheduler


/**
 * Implementation of the [BaseSchedulerProvider] making all [Scheduler]s execute
 * synchronously so we can easily run assertions in our tests.
 *
 *
 * To achieve this, we are using the [io.reactivex.internal.schedulers.TrampolineScheduler] from the [Schedulers] class.
 */
class ImmediateSchedulerProvider : BaseSchedulerProvider {

    @NonNull
    override fun computation(): Scheduler {
        return Schedulers.trampoline()
    }

    @NonNull
    override fun io(): Scheduler {
        return Schedulers.trampoline()
    }

    @NonNull
    override fun ui(): Scheduler {
        return Schedulers.trampoline()
    }
}