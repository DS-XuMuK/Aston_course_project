package ru.nikolaykolchin.astonrickandmorty.data.episodes.model

import androidx.recyclerview.widget.DiffUtil
import ru.nikolaykolchin.astonrickandmorty.data.episodes.dto.Episode

class EpisodeDiffUtil : DiffUtil.ItemCallback<Episode>() {
    override fun areItemsTheSame(oldItem: Episode, newItem: Episode): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Episode, newItem: Episode): Boolean {
        return oldItem == newItem
    }
}