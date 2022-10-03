package dev.filipebezerra.android.nearearthasteroids.repository

import com.google.firebase.perf.metrics.AddTrace
import dev.filipebezerra.android.nearearthasteroids.datasource.remote.ApodWsService
import dev.filipebezerra.android.nearearthasteroids.datasource.remote.asDomainModel
import dev.filipebezerra.android.nearearthasteroids.domain.PictureOfDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

class DefaultPictureOfDayRepository(
    private val apodWsService: ApodWsService
) : PictureOfDayRepository, BaseDefaultRepository() {

    @AddTrace(name = "getPictureOfTheDayTrace")
    @ExperimentalCoroutinesApi
    override fun getPictureOfTheDay(): Flow<PictureOfDay> = flow {
        val pictureOfTheDay = logBeforeCall("getting Picture of The Day from APOD API")
            .let { startTimeGettingObjectFromRemote ->
                apodWsService.run {
                    retrievePictureOfTheDay()
                }.run {
                    asDomainModel()
                }.also {
                    logAfterCall(
                        "getting Picture of The Day from APOD API",
                        startTimeGettingObjectFromRemote
                    )
                }
            }
        emit(pictureOfTheDay)
    }
        .catch { cause -> logError(cause, "getting Picture of The Day from APOD API") }
        .flowOn(Dispatchers.IO)
}