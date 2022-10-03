package dev.filipebezerra.android.nearearthasteroids

import android.annotation.SuppressLint
import android.app.Application
import androidx.work.*
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.android.utils.FlipperUtils
import com.facebook.flipper.plugins.databases.DatabasesFlipperPlugin
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.soloader.SoLoader
import dev.filipebezerra.android.nearearthasteroids.repository.AsteroidRepository
import dev.filipebezerra.android.nearearthasteroids.repository.PictureOfDayRepository
import dev.filipebezerra.android.nearearthasteroids.work.RefreshAsteroidDataWork
import dev.filipebezerra.android.nearearthasteroids.work.RefreshAsteroidDataWork.Companion.RECURRING_REFRESH_ASTEROID_DATA_WORK
import dev.filipebezerra.android.nearearthasteroids.work.RefreshAsteroidDataWork.Companion.REFRESH_ASTEROID_DATA_WORK
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import timber.log.Timber.DebugTree
import java.util.concurrent.TimeUnit.DAYS

class NeaApplication : Application(), Configuration.Provider {

    val asteroidRepository: AsteroidRepository
        get() = ServiceLocator.provideAsteroidRepository(this)

    val pictureOfDayRepository: PictureOfDayRepository
        get() = ServiceLocator.providePictureOfTheDayRepository()

    val workManager: WorkManager
        get() = ServiceLocator.provideWorkManager(this)

    private val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun getWorkManagerConfiguration(): Configuration {
        return if (BuildConfig.DEBUG) {
            Configuration.Builder()
                .setMinimumLoggingLevel(android.util.Log.DEBUG)
                .build()
        } else {
            Configuration.Builder()
                .setMinimumLoggingLevel(android.util.Log.ERROR)
                .build()
        }
    }

    override fun onCreate() {
        super.onCreate()
        initializeAnalytics()
        delayedInit()
        initializeDebugging()
    }

    private fun initializeDebugging() {
        SoLoader.init(this, false)

        if (BuildConfig.DEBUG && FlipperUtils.shouldEnableFlipper(this)) {
            val client = AndroidFlipperClient.getInstance(this)
            // https://fbflipper.com/docs/setup/layout-plugin
            client.addPlugin(InspectorFlipperPlugin(this, DescriptorMapping.withDefaults()))
            // https://fbflipper.com/docs/setup/network-plugin
            client.addPlugin(ServiceLocator.networkFlipperPlugin)
            // https://fbflipper.com/docs/setup/databases-plugin
            client.addPlugin(DatabasesFlipperPlugin(this));
            client.start()
        }
    }

    private fun initializeAnalytics() {
        when (BuildConfig.DEBUG) {
            true -> {
                Timber.plant(DebugTree())
            }
            false -> {
                Timber.plant(ReleaseTree())
            }
        }
    }

    private fun delayedInit() = applicationScope.launch {
        setupRefreshAsteroidDataWork()
    }

    @SuppressLint("NewApi")
    private fun setupRefreshAsteroidDataWork() {
        Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(true)
            .apply {
                android.os.Build.VERSION.SDK_INT
                    .takeIf { it >= android.os.Build.VERSION_CODES.M }
                    ?.apply { setRequiresDeviceIdle(true) }
            }
            .build()
            .let { constraints ->
                PeriodicWorkRequestBuilder<RefreshAsteroidDataWork>(1, DAYS)
                    .setConstraints(constraints)
                    .addTag(RECURRING_REFRESH_ASTEROID_DATA_WORK)
                    .build()
            }
            .let { workRequest ->
                // https://developer.android.com/topic/libraries/architecture/workmanager/how-to/define-work#schedule_periodic_work
                workManager.enqueueUniquePeriodicWork(
                    REFRESH_ASTEROID_DATA_WORK,
                    ExistingPeriodicWorkPolicy.KEEP,
                    workRequest
                )
            }
    }
}