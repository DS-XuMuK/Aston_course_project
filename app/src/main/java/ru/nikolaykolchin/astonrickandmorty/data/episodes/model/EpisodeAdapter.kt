package ru.nikolaykolchin.astonrickandmorty.data.episodes.model

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.nikolaykolchin.astonrickandmorty.R
import ru.nikolaykolchin.astonrickandmorty.databinding.ItemEpisodeBinding
import ru.nikolaykolchin.astonrickandmorty.data.episodes.dto.Episode
import ru.nikolaykolchin.astonrickandmorty.presentation.episodes.EpisodeDetailsFragment

class EpisodeAdapter : ListAdapter<Episode, EpisodeAdapter.EpisodeViewHolder>(EpisodeDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemEpisodeBinding.inflate(inflater)
        return EpisodeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.apply {
            name.text = item.name
            episode.text = item.episode
            airDate.text = item.air_date

            root.setOnClickListener {
                val fragmentDetails = EpisodeDetailsFragment.newInstance(item.id)
                val activity = it.context as AppCompatActivity
                activity.supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragmentDetails)
                    .addToBackStack(null)
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                    .commit()
            }
        }
    }

    class EpisodeViewHolder(val binding: ItemEpisodeBinding) : ViewHolder(binding.root)
}