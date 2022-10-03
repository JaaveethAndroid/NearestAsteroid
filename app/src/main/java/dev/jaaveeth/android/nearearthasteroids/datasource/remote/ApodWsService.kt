package dev.filipebezerra.android.nearearthasteroids.datasource.remote

import dev.filipebezerra.android.nearearthasteroids.util.ext.LocalDateExt.dateNowAsIsoLocalDate
import retrofit2.http.GET
import retrofit2.http.Query

const val APOD_BASE_API_URL = "https://api.nasa.gov/planetary/"

interface ApodWsService {
    @GET("apod")
    suspend fun retrievePictureOfTheDay(
        @Query("hd") highResolutionImage: Boolean = true,
        @Query("date") dateOfTheImage: String = dateNowAsIsoLocalDate()
    ): ApodPictureOfDay
}
