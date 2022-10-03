package dev.filipebezerra.android.nearearthasteroids.datasource.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dev.filipebezerra.android.nearearthasteroids.domain.Asteroid
import dev.filipebezerra.android.nearearthasteroids.domain.CloseApproachData
import dev.filipebezerra.android.nearearthasteroids.domain.EstimatedDiameter
import dev.filipebezerra.android.nearearthasteroids.domain.PictureOfDay
import dev.filipebezerra.android.nearearthasteroids.util.ext.toLocalDate

@JsonClass(generateAdapter = true)
data class NeoFeed(
    @Json(name = "element_count") val elementCount: Int,
    @Json(name = "near_earth_objects") val asteroidsByDate: Map<String, List<NearEarthObject>>
)

@JsonClass(generateAdapter = true)
data class NearEarthObject(
    @Json(name = "id") val id: String? = null,
    @Json(name = "name") val name: String? = null,
    @Json(name = "nasa_jpl_url") val nasaJplUrl: String? = null,
    @Json(name = "absolute_magnitude_h") val absoluteMagnitude: Float? = null,
    @Json(name = "estimated_diameter") val estimatedDiameter: NeoEstimatedDiameter? = null,
    @Json(name = "is_potentially_hazardous_asteroid") val isPotentiallyHazardousAsteroid: Boolean? = null,
    @Json(name = "close_approach_data") val closeApproachData: List<NeoCloseApproachData>? = null,
    @Json(name = "orbital_data") val orbitalData: NeoOrbitalData? = null,
)

@JsonClass(generateAdapter = true)
data class NeoEstimatedDiameter(
    @Json(name = "meters") val meters: NeoDiameterRange? = null
)

@JsonClass(generateAdapter = true)
data class NeoDiameterRange(
    @Json(name = "estimated_diameter_min") val minimumDiameter: Double? = null,
    @Json(name = "estimated_diameter_max") val maximumDiameter: Double? = null,
)

@JsonClass(generateAdapter = true)
data class NeoCloseApproachData(
    @Json(name = "close_approach_date") val approachDate: String? = null,
    @Json(name = "epoch_date_close_approach") val approachEpochDate: Long? = null,
    @Json(name = "relative_velocity") val relativeVelocity: NeoRelativeVelocity? = null,
    @Json(name = "miss_distance") val missDistance: NeoMissDistance? = null,
)

@JsonClass(generateAdapter = true)
data class NeoRelativeVelocity(
    @Json(name = "kilometers_per_second") val kilometersPerSecond: Double? = null
)

@JsonClass(generateAdapter = true)
data class NeoMissDistance(
    @Json(name = "kilometers") val kilometers: Double? = null,
    @Json(name = "lunar") val lunar: Double? = null,
)

@JsonClass(generateAdapter = true)
data class NeoOrbitalData(
    @Json(name = "orbit_class") val orbitClass: NeoOrbitClass? = null
)

@JsonClass(generateAdapter = true)
data class NeoOrbitClass(
    @Json(name = "orbit_class_description") val description: String? = null
)

fun NeoFeed.asDomainModel(): List<Asteroid> =
    asteroidsByDate.entries.map { it.value }.flatten().map {
        Asteroid(
            neoId = it.id,
            name = it.name,
            nasaJplUrl = it.nasaJplUrl,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = EstimatedDiameter(
                minimumInMeters = it.estimatedDiameter?.meters?.minimumDiameter,
                maximumInMeters = it.estimatedDiameter?.meters?.maximumDiameter,
            ),
            isPotentiallyHazardousAsteroid = it.isPotentiallyHazardousAsteroid,
            closeApproachData = it.closeApproachData?.firstOrNull()?.run {
                CloseApproachData(
                    approachDate = approachDate?.toLocalDate(),
                    relativeVelocityKilometersPerSecond = relativeVelocity?.kilometersPerSecond,
                    missDistanceInKilometers = missDistance?.kilometers,
                    missDistanceLunar = missDistance?.lunar,
                )
            },
            orbitClassDescription = it.orbitalData?.orbitClass?.description
        )
    }

@JsonClass(generateAdapter = true)
data class ApodPictureOfDay(
    @Json(name = "date") val date: String? = null,
    @Json(name = "explanation") val explanation: String? = null,
    @Json(name = "media_type") val mediaType: String? = null,
    @Json(name = "title") val title: String? = null,
    @Json(name = "url") val pictureUrl: String? = null,
    @Json(name = "hdurl") val hdPictureUrl: String? = null,
)

fun ApodPictureOfDay.asDomainModel() = PictureOfDay(
    date = this.date,
    explanation = this.explanation,
    mediaType = this.mediaType,
    title = this.title,
    pictureUrl = this.pictureUrl,
    hdPictureUrl = this.hdPictureUrl,
)
