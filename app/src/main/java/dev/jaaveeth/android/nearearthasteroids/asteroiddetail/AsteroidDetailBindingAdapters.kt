package dev.filipebezerra.android.nearearthasteroids.asteroiddetail

import android.text.format.DateUtils.*
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import dev.filipebezerra.android.nearearthasteroids.R
import dev.filipebezerra.android.nearearthasteroids.domain.CloseApproachData
import dev.filipebezerra.android.nearearthasteroids.domain.EstimatedDiameter
import dev.filipebezerra.android.nearearthasteroids.util.ext.LocalDateExt.dateNow
import dev.filipebezerra.android.nearearthasteroids.util.ext.applyImprovements
import dev.filipebezerra.android.nearearthasteroids.util.ext.asIsoLocalDate
import dev.filipebezerra.android.nearearthasteroids.util.ext.toEpochMilli

@BindingAdapter("asteroidHazardousOrSafe")
fun ImageView.bindAsteroidHazardousOrSafe(isPotentiallyHazardousAsteroid: Boolean) =
    Glide.with(context)
        .load(
            when (isPotentiallyHazardousAsteroid) {
                true -> R.drawable.asteroid_hazardous
                false -> R.drawable.asteroid_safe
            }
        )
        .transition(DrawableTransitionOptions.withCrossFade())
        .apply { applyImprovements() }
        .into(this)

@BindingAdapter("closeApproachDate")
fun TextView.bindCloseApproachDate(closeApproachDataEntity: CloseApproachData?) =
    closeApproachDataEntity?.approachDate?.let { approachDate ->
        getRelativeTimeSpanString(
            approachDate.toEpochMilli(),
            dateNow().toEpochMilli(),
            DAY_IN_MILLIS,
            FORMAT_NO_YEAR + FORMAT_ABBREV_MONTH,
        ).let { relativeTime ->
            text = context.getString(
                R.string.close_approach_date_format,
                relativeTime,
                approachDate.asIsoLocalDate(),
            )
        }
    }

@BindingAdapter("estimatedDiameter")
fun TextView.bindEstimatedDiameter(estimatedDiameterEntity: EstimatedDiameter?) =
    estimatedDiameterEntity?.let {
        text = context.getString(
            R.string.estimated_diameter_format,
            it.minimumInMeters,
            it.maximumInMeters,
        )
    }

@BindingAdapter("relativeVelocity")
fun TextView.bindRelativeVelocity(closeApproachDataEntity: CloseApproachData?) =
    closeApproachDataEntity?.let {
        text = context.getString(
            R.string.relative_velocity_format,
            it.relativeVelocityKilometersPerSecond,
        )
    }

@BindingAdapter("distanceFromEarth")
fun TextView.bindDistanceFromEarth(closeApproachDataEntity: CloseApproachData?) =
    closeApproachDataEntity?.let {
        text = context.getString(
            R.string.distance_from_earth_format,
            it.missDistanceInKilometers,
        )
    }