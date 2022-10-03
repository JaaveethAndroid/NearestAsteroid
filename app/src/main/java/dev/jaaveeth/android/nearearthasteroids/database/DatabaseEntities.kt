package dev.filipebezerra.android.nearearthasteroids.database

import androidx.room.*
import dev.filipebezerra.android.nearearthasteroids.domain.Asteroid
import dev.filipebezerra.android.nearearthasteroids.domain.CloseApproachData
import dev.filipebezerra.android.nearearthasteroids.domain.EstimatedDiameter
import java.time.LocalDate

@Entity(
    tableName = "asteroids",
    indices = [
        Index(value = ["neo_id"], unique = true),
        Index(value = ["name"]),
    ],
)
data class AsteroidEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long = 0,
    @ColumnInfo(name = "neo_id") val neoId: String? = null,
    @ColumnInfo(name = "name") val name: String? = null,
    @ColumnInfo(name = "nasa_jpl_url") val nasaJplUrl: String? = null,
    @ColumnInfo(name = "absolute_magnitude") val absoluteMagnitude: Float? = null,
    @Embedded(prefix = "estimated_diameter_") val estimatedDiameter: EstimatedDiameterEntity? = null,
    @ColumnInfo(name = "is_potentially_hazardous") val isPotentiallyHazardousAsteroid: Boolean? = null,
    @Embedded(prefix = "close_approach_data_") val closeApproachData: CloseApproachDataEntity? = null,
    @ColumnInfo(name = "orbit_class_description") val orbitClassDescription: String? = null,
)

data class CloseApproachDataEntity(
    @ColumnInfo(name = "date") val approachDate: LocalDate? = null,
    @ColumnInfo(name = "relative_velocity_kps") val relativeVelocityKilometersPerSecond: Double? = null,
    @ColumnInfo(name = "miss_distance_km") val missDistanceInKilometers: Double? = null,
    @ColumnInfo(name = "miss_distance_lunar") val missDistanceLunar: Double? = null,
)

data class EstimatedDiameterEntity(
    @ColumnInfo(name = "min_in_meters") val minimumInMeters: Double? = null,
    @ColumnInfo(name = "max_in_meters") val maximumInMeters: Double? = null,
)

fun List<AsteroidEntity>.asDomainModel(): List<Asteroid> = map {
    Asteroid(
        id = it.id,
        neoId = it.neoId,
        name = it.name,
        nasaJplUrl = it.nasaJplUrl,
        absoluteMagnitude = it.absoluteMagnitude,
        estimatedDiameter = EstimatedDiameter(
            it.estimatedDiameter?.minimumInMeters,
            it.estimatedDiameter?.maximumInMeters
        ),
        isPotentiallyHazardousAsteroid = it.isPotentiallyHazardousAsteroid,
        closeApproachData = CloseApproachData(
            approachDate = it.closeApproachData?.approachDate,
            relativeVelocityKilometersPerSecond = it.closeApproachData?.relativeVelocityKilometersPerSecond,
            missDistanceInKilometers = it.closeApproachData?.missDistanceInKilometers,
            missDistanceLunar = it.closeApproachData?.missDistanceLunar,
        ),
        orbitClassDescription = it.orbitClassDescription
    )
}