package dev.filipebezerra.android.nearearthasteroids.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/**
 * The Data Access Object for the [AsteroidEntity] class.
 */
@Dao
interface AsteroidDao {

    /**
     * Insert a list of [AsteroidEntity] in the database. If the [AsteroidEntity] already exists, replace it.
     *
     * @param asteroids the list of [AsteroidEntity] to be inserted
     * @return the list of new `rowId` for each [AsteroidEntity] inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(asteroids: List<AsteroidEntity>): List<Long>

    /**
     * Query and observe list of [AsteroidEntity] filtering by their close approach date.
     *
     * @return all [AsteroidEntity].
     */
    @Query("SELECT * FROM asteroids WHERE close_approach_data_date between :fromDate and :toDate ORDER BY close_approach_data_date ASC")
    fun observeAsteroids(
        fromDate: LocalDate,
        toDate: LocalDate
    ): Flow<List<AsteroidEntity>>
}