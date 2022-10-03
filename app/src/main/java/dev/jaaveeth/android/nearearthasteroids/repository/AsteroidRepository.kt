package dev.filipebezerra.android.nearearthasteroids.repository

import dev.filipebezerra.android.nearearthasteroids.domain.Asteroid
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/**
 * Interface to the [Asteroid] data layer.
 */
interface AsteroidRepository {

    suspend fun refreshAsteroids()

    suspend fun resetAsteroids(fromDate: LocalDate, toDate: LocalDate): Flow<List<Asteroid>>

    fun observeAsteroids(fromDate: LocalDate, toDate: LocalDate): Flow<List<Asteroid>>

    suspend fun saveAsteroids(asteroids: List<Asteroid>): List<Long>
}