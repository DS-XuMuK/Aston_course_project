package ru.nikolaykolchin.astonrickandmorty.data.locations.model

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.nikolaykolchin.astonrickandmorty.R
import ru.nikolaykolchin.astonrickandmorty.databinding.ItemLocationBinding
import ru.nikolaykolchin.astonrickandmorty.data.locations.dto.Location
import ru.nikolaykolchin.astonrickandmorty.presentation.locations.LocationDetailsFragment

class LocationAdapter :
    ListAdapter<Location, LocationAdapter.LocationViewHolder>(LocationDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLocationBinding.inflate(inflater)
        return LocationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.apply {
            name.text = item.name
            type.text = item.type
            dimension.text = item.dimension

            root.setOnClickListener {
                val fragmentDetails = LocationDetailsFragment.newInstance(item.id)
                val activity = it.context as AppCompatActivity
                activity.supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragmentDetails)
                    .addToBackStack(null)
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                    .commit()
            }
        }
    }

    class LocationViewHolder(val binding: ItemLocationBinding) : ViewHolder(binding.root)
}