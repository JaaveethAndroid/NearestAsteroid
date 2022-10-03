package dev.filipebezerra.android.nearearthasteroids.asteroidlist

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.mdgiitr.suyash.graphkit.DataPoint
import dev.filipebezerra.android.nearearthasteroids.databinding.AsteroidGraphBinding
import dev.filipebezerra.android.nearearthasteroids.util.ext.getViewModelFactory
import java.util.*

class AsteroidGraphScreen : Fragment() {

    private val asteroidListViewModel: AsteroidListViewModel by activityViewModels { getViewModelFactory() }

    private lateinit var viewBinding: AsteroidGraphBinding

    private val navController: NavController by lazy { findNavController() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = AsteroidGraphBinding.inflate(inflater)
        .apply {
            viewBinding = this
            viewModel = asteroidListViewModel
        }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.lifecycleOwner = viewLifecycleOwner
        val data : ArrayList<DataPoint> = ArrayList()
        asteroidListViewModel.asteroids?.value.let {listAsteroid->
            listAsteroid?.distinctBy{it.closeApproachData?.approachDate}?.let { asteroid->
                for (item in asteroid ) {
                    val rnd = Random()
                    val color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
                    data.add(
                        DataPoint(
                            item.closeApproachData?.approachDate.toString(),
                            listAsteroid.filter { it.closeApproachData?.approachDate == item.closeApproachData?.approachDate }.size.toFloat(),
                            color
                        )
                    )
                    println("${item.closeApproachData?.approachDate.toString()} ${listAsteroid.filter { it.closeApproachData?.approachDate == item.closeApproachData?.approachDate }.size.toFloat()}")
                }
                viewBinding.barGraph.setPoints(data)
            }


        }
    }
}