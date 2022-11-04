package ru.nikolaykolchin.astonrickandmorty.data.characters.model

import androidx.recyclerview.widget.DiffUtil
import ru.nikolaykolchin.astonrickandmorty.data.characters.dto.Character

class CharacterDiffUtil : DiffUtil.ItemCallback<Character>() {
    override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
        return oldItem == newItem
    }
}