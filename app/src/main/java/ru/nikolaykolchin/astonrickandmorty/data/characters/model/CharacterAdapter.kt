package ru.nikolaykolchin.astonrickandmorty.data.characters.model

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.squareup.picasso.Picasso
import ru.nikolaykolchin.astonrickandmorty.databinding.ItemCharacterBinding
import ru.nikolaykolchin.astonrickandmorty.R
import ru.nikolaykolchin.astonrickandmorty.data.characters.dto.Character
import ru.nikolaykolchin.astonrickandmorty.presentation.characters.CharacterDetailsFragment

class CharacterAdapter :
    ListAdapter<Character, CharacterAdapter.CharacterViewHolder>(CharacterDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCharacterBinding.inflate(inflater)
        return CharacterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.apply {
            name.text = item.name
            species.text = item.species
            status.text = item.status
            gender.text = item.gender

            Picasso.get()
                .load(item?.image)
                .placeholder(R.drawable.ic_character_foreground)
                .error(R.drawable.launch_icon)
                .into(image)

            root.setOnClickListener {
                val fragmentDetails = CharacterDetailsFragment.newInstance(item.id)
                val activity = it.context as AppCompatActivity
                activity.supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragmentDetails)
                    .addToBackStack(null)
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                    .commit()
            }
        }
    }

    class CharacterViewHolder(val binding: ItemCharacterBinding) : ViewHolder(binding.root)
}