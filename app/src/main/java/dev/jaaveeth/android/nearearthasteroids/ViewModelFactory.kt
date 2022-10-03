package dev.filipebezerra.android.nearearthasteroids

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import androidx.work.WorkManager
import dev.filipebezerra.android.nearearthasteroids.repository.AsteroidRepository
import dev.filipebezerra.android.nearearthasteroids.repository.PictureOfDayRepository
import dev.filipebezerra.android.nearearthasteroids.asteroiddetail.AsteroidDetailViewModel
import dev.filipebezerra.android.nearearthasteroids.asteroidlist.AsteroidListViewModel

/**
 * Factory for all ViewModels.
 */
@Suppress("UNCHECKED_CAST")
class ViewModelFactory constructor(
    private val asteroidRepository: AsteroidRepository,
    private val pictureOfDayRepository: PictureOfDayRepository,
    private val workManager: WorkManager,
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {

    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ) = with(modelClass) {
        when {
            isAssignableFrom(AsteroidListViewModel::class.java) ->
                AsteroidListViewModel(
                    asteroidRepository,
                    pictureOfDayRepository,
                    workManager,
                )
            isAssignableFrom(AsteroidDetailViewModel::class.java) ->
                AsteroidDetailViewModel()
            else ->
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    } as T
}