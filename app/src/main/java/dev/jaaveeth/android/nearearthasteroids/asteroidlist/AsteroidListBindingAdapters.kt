package dev.filipebezerra.android.nearearthasteroids.asteroidlist

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.work.WorkInfo
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import dev.filipebezerra.android.nearearthasteroids.R
import dev.filipebezerra.android.nearearthasteroids.asteroiddetail.bindAsteroidHazardousOrSafe
import dev.filipebezerra.android.nearearthasteroids.domain.Asteroid
import dev.filipebezerra.android.nearearthasteroids.domain.PictureOfDay
import dev.filipebezerra.android.nearearthasteroids.util.ext.applyImprovements
import dev.filipebezerra.android.nearearthasteroids.util.getNeoSizeImage

@BindingAdapter("asteroidList")
fun bindAsteroidList(listView: RecyclerView, asteroids: List<Asteroid>?) =
    asteroids?.let { (listView.adapter as AsteroidListAdapter).submitList(asteroids) }

@BindingAdapter("pictureOfDay", "bitmap")
fun ImageView.bindPictureOfDay(
    pictureOfDay: PictureOfDay?,
    bitmap: Boolean? = false,
) = pictureOfDay?.apply {
    when (bitmap) {
        true -> Glide.with(context)
            .asBitmap()
            .load(hdPictureUrl)
            .transition(BitmapTransitionOptions.withCrossFade())
        else -> Glide.with(context)
            .load(pictureOfDay)
            .transition(DrawableTransitionOptions.withCrossFade())
    }.apply {
        placeholder(R.drawable.picture_of_day_placeholder)
        error(R.drawable.picture_of_day_placeholder)
        fallback(R.drawable.picture_of_day_placeholder)
        contentDescription = if (hdPictureUrl != null) title
            else context.getString(R.string.picture_of_day_placeholder)
        applyImprovements(date)
    }.run { into(this@bindPictureOfDay) }
}

@BindingAdapter("titlePictureOfDay")
fun CollapsingToolbarLayout.bindTitleOfPictureOfDay(pictureOfDay: PictureOfDay?) =
    pictureOfDay?.let { picture ->
        title = when {
            picture.hdPictureUrl != null -> picture.title
            else -> context.getString(R.string.picture_of_day_placeholder)
        }
    }

@BindingAdapter("initialAsteroidDataState")
fun View.bindInitialAsteroidDataState(initialAsteroidDataState: WorkInfo.State?) =
    initialAsteroidDataState?.let {
        when (initialAsteroidDataState) {
            WorkInfo.State.SUCCEEDED,WorkInfo.State.ENQUEUED -> when (id) {
                R.id.asteroid_list_placeholder -> {
                    visibility = GONE
                    (this as ShimmerFrameLayout).stopShimmer()
                }
                R.id.asteroid_list -> visibility = VISIBLE
                R.id.asteroid_list_error -> {
                    visibility = GONE
                    (((parent as FrameLayout).layoutParams) as CoordinatorLayout.LayoutParams)
                        .behavior = AppBarLayout.ScrollingViewBehavior()
                }
            }
            WorkInfo.State.FAILED -> when (id) {
                R.id.asteroid_list_placeholder -> {
                    visibility = GONE
                    (this as ShimmerFrameLayout).stopShimmer()
                }
                R.id.asteroid_list -> visibility = GONE
                R.id.asteroid_list_error -> {
                    visibility = VISIBLE
                    (((parent as FrameLayout).layoutParams) as CoordinatorLayout.LayoutParams)
                        .behavior = null
                }
            }
            else -> when (id) {
                R.id.asteroid_list_placeholder -> {
                    visibility = VISIBLE
                    (this as ShimmerFrameLayout).startShimmer()
                }
                R.id.asteroid_list,
                R.id.asteroid_list_error -> visibility = GONE
            }
        }
}

@BindingAdapter("asteroidImageSize")
fun ImageView.bindAsteroidImageSize(asteroid: Asteroid?) = asteroid?.let {
    Glide.with(context)
        .load(it.getNeoSizeImage(context))
        .transition(DrawableTransitionOptions.withCrossFade())
        .apply { applyImprovements() }
        .into(this)
}