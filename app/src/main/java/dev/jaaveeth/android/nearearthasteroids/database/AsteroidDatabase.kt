package dev.filipebezerra.android.nearearthasteroids.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.BackoffPolicy
import androidx.work.OneTimeWorkRequestBuilder
import dev.filipebezerra.android.nearearthasteroids.ServiceLocator
import dev.filipebezerra.android.nearearthasteroids.work.RefreshAsteroidDataWork
import dev.filipebezerra.android.nearearthasteroids.work.RefreshAsteroidDataWork.Companion.GET_INITIAL_ASTEROID_DATA_WORK
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * The Room database for this app that contains the [AsteroidEntity]s.
 */
@Database(
    entities = [
        AsteroidEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
@TypeConverters(DatabaseConverters::class)
abstract class AsteroidDatabase : RoomDatabase() {

    /**
     * The [AsteroidEntity] Data Access Object.
     */
    abstract fun asteroidDao(): AsteroidDao

    companion object {
        private const val DB_NAME = "NearEarthObjects.db"

        // https://medium.com/google-developers/7-pro-tips-for-room-fbadea4bfbd1#4785
        fun createDatabase(context: Context): AsteroidDatabase = Room.databaseBuilder(
            context.applicationContext,
            AsteroidDatabase::class.java,
            DB_NAME,
        ).addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                Timber.i("Database created. Scheduling one time work to refresh Asteroid data immediately")
                OneTimeWorkRequestBuilder<RefreshAsteroidDataWork>()
                    // For initial data loading our backoff delay will be 30 Secs EXPONENTIAL (60, 90, 120 ...)
                    .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 30, TimeUnit.SECONDS)
                    .addTag(GET_INITIAL_ASTEROID_DATA_WORK)
                    .build()
                    .let { workRequest ->
                        ServiceLocator.provideWorkManager(context).enqueue(workRequest)
                    }
            }
        }).build()
    }
}