package dev.filipebezerra.android.nearearthasteroids.domain

import android.os.Parcelable
import dev.filipebezerra.android.nearearthasteroids.database.AsteroidEntity
import dev.filipebezerra.android.nearearthasteroids.database.CloseApproachDataEntity
import dev.filipebezerra.android.nearearthasteroids.database.EstimatedDiameterEntity
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class Asteroid(
    val id: Long = 0,
    val neoId: String? = null,
    val name: String? = null,
    val nasaJplUrl: String? = null,
    val absoluteMagnitude: Float? = null,
    val estimatedDiameter: EstimatedDiameter? = null,
    val isPotentiallyHazardousAsteroid: Boolean? = null,
    val closeApproachData: CloseApproachData? = null,
    val orbitClassDescription: String? = null,
) : Parcelable

@Parcelize
data class CloseApproachData(
    val approachDate: LocalDate? = null,
    val relativeVelocityKilometersPerSecond: Double? = null,
    val missDistanceInKilometers: Double? = null,
    val missDistanceLunar: Double? = null,
) : Parcelable

@Parcelize
data class EstimatedDiameter(
    val minimumInMeters: Double? = null,
    val maximumInMeters: Double? = null,
) : Parcelable

@Parcelize
data class PictureOfDay(
    val date: String? = null,
    val explanation: String? = null,
    val mediaType: String? = null,
    val title: String? = null,
    val pictureUrl: String? = null,
    val hdPictureUrl: String? = null,
) : Parcelable

fun List<Asteroid>.asEntity(): List<AsteroidEntity> = map {
    AsteroidEntity(
        id = it.id,
        neoId = it.neoId,
        name = it.name,
        nasaJplUrl = it.nasaJplUrl,
        absoluteMagnitude = it.absoluteMagnitude,
        estimatedDiameter = EstimatedDiameterEntity(
            it.estimatedDiameter?.minimumInMeters,
            it.estimatedDiameter?.maximumInMeters
        ),
        isPotentiallyHazardousAsteroid = it.isPotentiallyHazardousAsteroid,
        closeApproachData = CloseApproachDataEntity(
            approachDate = it.closeApproachData?.approachDate,
            relativeVelocityKilometersPerSecond = it.closeApproachData?.relativeVelocityKilometersPerSecond,
            missDistanceInKilometers = it.closeApproachData?.missDistanceInKilometers,
            missDistanceLunar = it.closeApproachData?.missDistanceLunar,
        ),
        orbitClassDescription = it.orbitClassDescription
    )
}
