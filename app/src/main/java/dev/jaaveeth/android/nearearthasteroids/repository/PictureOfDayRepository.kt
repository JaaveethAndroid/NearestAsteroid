package dev.filipebezerra.android.nearearthasteroids.repository

import dev.filipebezerra.android.nearearthasteroids.domain.PictureOfDay
import kotlinx.coroutines.flow.Flow

/**
 * Interface to the [PictureOfDay] data layer.
 */
interface PictureOfDayRepository {

    fun getPictureOfTheDay(): Flow<PictureOfDay>
}