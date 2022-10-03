package dev.filipebezerra.android.nearearthasteroids.repository

import timber.log.Timber
import java.time.LocalTime
import java.time.temporal.ChronoUnit

abstract class BaseDefaultRepository {

    protected fun logBeforeCall(callLogDescription: String): LocalTime {
        return Timber.i("Data layer: starting $callLogDescription")
            .run { LocalTime.now() }
    }

    protected fun logAfterCall(
        callLogDescription: String,
        startTime: LocalTime,
    ) {
        LocalTime.now().let { finishTime ->
            ChronoUnit.SECONDS.between(startTime, finishTime).apply {
                takeIf { it > 0L }?.let { timeBetweenInSeconds ->
                    Timber.i("Data layer: Successfully done $callLogDescription in $timeBetweenInSeconds s")
                }
                takeUnless { it > 0L }?.let {
                    ChronoUnit.MILLIS.between(startTime, finishTime).let { timeBetweenInMillis ->
                        Timber.i("Data layer: Successfully done $callLogDescription in $timeBetweenInMillis ms")
                    }
                }
            }
        }
    }

    protected fun logError(
        error: Throwable,
        callLogDescription: String,
    ) = Timber.e(error, callLogDescription)
}