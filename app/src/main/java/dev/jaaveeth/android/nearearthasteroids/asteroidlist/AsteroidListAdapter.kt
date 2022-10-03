package dev.filipebezerra.android.nearearthasteroids.asteroidlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.filipebezerra.android.nearearthasteroids.databinding.AsteroidListItemBinding
import dev.filipebezerra.android.nearearthasteroids.domain.Asteroid

class AsteroidListAdapter(
    private val itemListener: AsteroidItemListener
) : ListAdapter<Asteroid, AsteroidItemViewHolder>(AsteroidItemDiff()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        AsteroidItemViewHolder.from(parent)

    override fun onBindViewHolder(holder: AsteroidItemViewHolder, position: Int) =
        holder.bindTo(getItem(position), itemListener)
}

class AsteroidItemViewHolder private constructor(
    private val itemBinding: AsteroidListItemBinding
) : RecyclerView.ViewHolder(itemBinding.root) {

    fun bindTo(
        item: Asteroid,
        itemListener: AsteroidItemListener
    ) = with(itemBinding) {
        asteroidItemListener = itemListener
        asteroid = item
        executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): AsteroidItemViewHolder {
            val itemBinding = AsteroidListItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return AsteroidItemViewHolder(itemBinding)
        }
    }
}

class AsteroidItemDiff : DiffUtil.ItemCallback<Asteroid>() {
    override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid) =
        oldItem == newItem
}

interface AsteroidItemListener {
    fun onItemClicked(asteroid: Asteroid)

    fun onShareClicked(asteroid: Asteroid)

    fun onMoreInfoClicked(asteroid: Asteroid)
}