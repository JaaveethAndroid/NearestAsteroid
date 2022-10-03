package dev.filipebezerra.android.nearearthasteroids.util

import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import dev.filipebezerra.android.nearearthasteroids.R

fun Toolbar.setNavigationContentDescriptionUsingPreviousDestinationLabel() {
    navigationContentDescription = context.getString(
        R.string.navigation_content_description,
        findNavController().previousBackStackEntry?.destination?.label
            ?: context.getString(R.string.unlabelled_destination)
    )
}