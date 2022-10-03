package dev.filipebezerra.android.nearearthasteroids

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.work.WorkManager
import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dev.filipebezerra.android.nearearthasteroids.database.AsteroidDao
import dev.filipebezerra.android.nearearthasteroids.database.AsteroidDatabase
import dev.filipebezerra.android.nearearthasteroids.datasource.remote.APOD_BASE_API_URL
import dev.filipebezerra.android.nearearthasteroids.datasource.remote.ApodWsService
import dev.filipebezerra.android.nearearthasteroids.datasource.remote.NEO_BASE_API_URL
import dev.filipebezerra.android.nearearthasteroids.datasource.remote.NeoWsService
import dev.filipebezerra.android.nearearthasteroids.repository.AsteroidRepository
import dev.filipebezerra.android.nearearthasteroids.repository.DefaultAsteroidRepository
import dev.filipebezerra.android.nearearthasteroids.repository.DefaultPictureOfDayRepository
import dev.filipebezerra.android.nearearthasteroids.repository.PictureOfDayRepository
import dev.filipebezerra.android.nearearthasteroids.util.interceptors.ApiKeyInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit.SECONDS

// TODO: Migrate DI using Koin
/**
 * A Service Locator object for providing application dependencies.
 */
object ServiceLocator {

    private val lock = Any()

    private var database: AsteroidDatabase? = null

    private val moshiBuilder: Moshi.Builder by lazy {
        Moshi.Builder().add(KotlinJsonAdapterFactory())
    }

    private val okHttpClientBuilder: OkHttpClient.Builder by lazy {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply { level = BODY })
            .addInterceptor(ApiKeyInterceptor())
            .addNetworkInterceptor(FlipperOkhttpInterceptor(networkFlipperPlugin))
            .readTimeout(30, SECONDS)
            .callTimeout(60, SECONDS)
            .retryOnConnectionFailure(true)
    }

    private val retrofitBuilder: Retrofit.Builder by lazy {
        Retrofit.Builder()
            .baseUrl(NEO_BASE_API_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshiBuilder.build()))
            .client(okHttpClientBuilder.build())
    }

    val networkFlipperPlugin by lazy { NetworkFlipperPlugin() }

    @Volatile
    var asteroidRepository: AsteroidRepository? = null
        @VisibleForTesting set

    @Volatile
    var pictureOfDayRepository: PictureOfDayRepository? = null
        @VisibleForTesting set

    @Volatile
    var neoWsService: NeoWsService? = null
        @VisibleForTesting set

    @Volatile
    var apodWsService: ApodWsService? = null
        @VisibleForTesting set

    @Volatile
    var workManager: WorkManager? = null
        @VisibleForTesting set

    fun provideWorkManager(context: Context): WorkManager = synchronized(lock) {
        workManager ?: WorkManager.getInstance(context.applicationContext)
    }

    fun provideAsteroidRepository(context: Context): AsteroidRepository = synchronized(lock) {
        asteroidRepository ?: createAsteroidRepository(context)
    }

    private fun createAsteroidRepository(context: Context): AsteroidRepository =
        DefaultAsteroidRepository(
            provideAsteroidDao(context),
            provideNeoWsService(),
        ).apply { asteroidRepository = this }

    private fun provideAsteroidDao(context: Context): AsteroidDao =
        provideAsteroidDatabase(context).run { asteroidDao() }

    private fun provideAsteroidDatabase(context: Context): AsteroidDatabase =
        database ?: AsteroidDatabase.createDatabase(context)
            .apply { database = this }

    private fun provideNeoWsService(): NeoWsService = synchronized(lock) {
        neoWsService ?: createNeoWsService()
    }

    private fun createNeoWsService(): NeoWsService = with(retrofitBuilder.build()) {
        if (this.baseUrl().equals(NEO_BASE_API_URL))
            create(NeoWsService::class.java)
        else
            newBuilder().baseUrl(NEO_BASE_API_URL).build().create(NeoWsService::class.java)
    }

    fun providePictureOfTheDayRepository(): PictureOfDayRepository = synchronized(lock) {
        pictureOfDayRepository ?: createPictureOfDayRepository()
    }

    private fun createPictureOfDayRepository(): PictureOfDayRepository =
        DefaultPictureOfDayRepository(provideApodWsService())
            .apply { pictureOfDayRepository = this }

    private fun provideApodWsService(): ApodWsService = synchronized(lock) {
        apodWsService ?: createApodWsService()
    }

    private fun createApodWsService(): ApodWsService = with(retrofitBuilder.build()) {
        if (this.baseUrl().equals(APOD_BASE_API_URL))
            create(ApodWsService::class.java)
        else
            newBuilder().baseUrl(APOD_BASE_API_URL).build().create(ApodWsService::class.java)
    }
}