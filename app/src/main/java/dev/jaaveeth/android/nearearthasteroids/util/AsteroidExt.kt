package dev.filipebezerra.android.nearearthasteroids.util

import android.content.Context
import android.content.res.Resources.NotFoundException
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import dev.filipebezerra.android.nearearthasteroids.R
import dev.filipebezerra.android.nearearthasteroids.domain.Asteroid


fun Asteroid.getNeoSizeImage(context: Context): Drawable? {
    val dimension = if (estimatedDiameter?.maximumInMeters == null) null else {
        if (estimatedDiameter.minimumInMeters != null)
            (estimatedDiameter.minimumInMeters + estimatedDiameter.maximumInMeters) / 2.0
        else
            estimatedDiameter.maximumInMeters
    }

    val sizePrefix = getSizePrefix(dimension)
    val distancePrefix = getDistancePrefix(closeApproachData?.missDistanceLunar)
    val imageIdentifier = sizePrefix + distancePrefix
    return getImageResource(
        context,
        context.resources.getIdentifier(imageIdentifier, "drawable", context.packageName)
    )
}

private fun getSizePrefix(dimension: Double?): String {
    return when (dimension) {
        null -> "asteroid"
        in 0.0..5.0 -> "car"
        in 6.0..15.0 -> "bus"
        in 15.1..30.0 -> "semi"
        in 30.1..100.0 -> "shuttle"
        in 100.1..300.0 -> "arena"
        in 300.1..600.0 -> "scraper"
        in 600.1..1000.0 -> "arenas"
        in 1000.01..3000.0 -> "mountain"
        else -> "asteroid"
    }
}

private fun getDistancePrefix(missDistanceLunar: Double?): String {
    return when {
        missDistanceLunar == null -> "_08_morethan50ld"
        missDistanceLunar <= 0.0 -> "_00_contact"
        missDistanceLunar in 0.0..1.0 -> "_01_closermoon"
        missDistanceLunar in 1.01..3.0 -> "_02_1_1to3ld"
        missDistanceLunar in 3.01..5.0 -> "_03_3_1to5ld"
        missDistanceLunar in 5.01..7.0 -> "_04_5_1to7ld"
        missDistanceLunar in 7.01..10.0 -> "_05_7_1to10ld"
        missDistanceLunar in 10.01..30.0 -> "_06_10_1to30ld"
        missDistanceLunar in 30.01..50.0 -> "_07_30_1to50ld"
        else -> "_08_morethan50ld"
    }
}

private fun getImageResource(context: Context, @DrawableRes neoImageRes: Int): Drawable? {
    return try {
        ContextCompat.getDrawable(context, neoImageRes)
    } catch (ignored: NotFoundException) {
        ContextCompat.getDrawable(context, R.drawable.asteroid)
    }
}