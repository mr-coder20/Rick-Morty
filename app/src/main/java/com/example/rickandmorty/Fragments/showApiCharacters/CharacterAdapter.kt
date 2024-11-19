package com.example.rickandmorty.Fragments.showApiCharacters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rickandmorty.R
import com.example.rickandmorty.data.Character // اطمینان حاصل کنید که این import صحیح است.

class CharacterAdapter(private val onCharacterClicked: (Character) -> Unit) :
    ListAdapter<Character, CharacterAdapter.CharacterViewHolder>(CharacterDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_character, parent, false)
        return CharacterViewHolder(view, onCharacterClicked)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class CharacterViewHolder(
        private val view: View,
        private val onCharacterClicked: (Character) -> Unit
    ) : RecyclerView.ViewHolder(view) {

        private val characterImage: ImageView = view.findViewById(R.id.characterImage)
        private val characterName: TextView = view.findViewById(R.id.characterName)
        private val characterSpecies: TextView = view.findViewById(R.id.characterSpecies)
        private val characterStatus: TextView = view.findViewById(R.id.characterStatus)

        fun bind(character: Character) {
            // Set the name and species of the character
            characterName.text = character.name
            characterSpecies.text = character.species
            characterStatus.text = character.status

            // Set text color based on character's status
            if (character.status == "Dead") {
                characterStatus.setTextColor(ContextCompat.getColor(view.context, R.color.md_red_A200))  // Red color for "Dead" status
            } else {
                characterStatus.setTextColor(ContextCompat.getColor(view.context, R.color.md_green_A400)) // Green color for other statuses (e.g., "Alive")
            }

            // Load the character image using Glide
            Glide.with(view.context).load(character.image).into(characterImage)

            // Set a click listener to trigger the callback function
            view.setOnClickListener {
                onCharacterClicked(character)
            }
        }
    }

    class CharacterDiffCallback : DiffUtil.ItemCallback<Character>() {
        override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean =
            oldItem == newItem
    }
}
