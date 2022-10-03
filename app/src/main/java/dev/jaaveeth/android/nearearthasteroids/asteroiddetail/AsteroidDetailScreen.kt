package dev.filipebezerra.android.nearearthasteroids.asteroiddetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import dev.filipebezerra.android.nearearthasteroids.databinding.AsteroidDetailScreenBinding
import dev.filipebezerra.android.nearearthasteroids.util.ext.getViewModelFactory
import dev.filipebezerra.android.nearearthasteroids.util.setNavigationContentDescriptionUsingPreviousDestinationLabel

class AsteroidDetailScreen : Fragment() {

    private val arguments: AsteroidDetailScreenArgs by navArgs()

    private val asteroidDetailViewModel: AsteroidDetailViewModel by viewModels {
        getViewModelFactory()
    }

    private lateinit var viewBinding: AsteroidDetailScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = AsteroidDetailScreenBinding.inflate(inflater)
        .apply {
            viewBinding = this
            viewModel = asteroidDetailViewModel

            with(viewBinding.toolbar) {
                setNavigationOnClickListener { view -> view.findNavController().navigateUp() }
            }

            var isToolbarShown = false
            asteroidDetailScrollview.setOnScrollChangeListener(
                NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, _ ->
                    val shouldShowToolbar = scrollY > toolbar.height
                    if (isToolbarShown != shouldShowToolbar) {
                        isToolbarShown = shouldShowToolbar
                        appBar.isActivated = shouldShowToolbar
                        toolbarLayout.isTitleEnabled = shouldShowToolbar
                    }
                }
            )
        }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        asteroidDetailViewModel.initialize(arguments.asteroid)
        viewBinding.lifecycleOwner = viewLifecycleOwner
        viewBinding.toolbar.setNavigationContentDescriptionUsingPreviousDestinationLabel()
    }
}