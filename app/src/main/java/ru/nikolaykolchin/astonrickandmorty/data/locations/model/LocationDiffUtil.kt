package ru.nikolaykolchin.astonrickandmorty.data.locations.model

import androidx.recyclerview.widget.DiffUtil
import ru.nikolaykolchin.astonrickandmorty.data.locations.dto.Location

class LocationDiffUtil : DiffUtil.ItemCallback<Location>() {
    override fun areItemsTheSame(oldItem: Location, newItem: Location): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Location, newItem: Location): Boolean {
        return oldItem == newItem
    }
}