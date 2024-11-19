package com.example.rickandmorty.Fragments.showCreatedCharacters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.Fragments.createCharacters.AppDatabase
import com.example.rickandmorty.R
import com.example.rickandmorty.Fragments.createCharacters.Character
import com.example.rickandmorty.Fragments.createCharacters.CreateFragment
import com.example.rickandmorty.Fragments.showApiCharacters.ShowFragment

class UserCharacterFragment : Fragment(), OnItemActionListener {

    private lateinit var viewModel: UserCharactersViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var characterAdapter: CharacterAdapter
    private var characters: List<Character> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_show_score, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val emptyView = view.findViewById<LinearLayout>(R.id.emptyView)

        val db = AppDatabase.getDatabase(requireContext())
        viewModel = ViewModelProvider(this, UserCharactersViewModelFactory(db)).get(UserCharactersViewModel::class.java)

        characterAdapter = CharacterAdapter(characters, this)
        recyclerView.adapter = characterAdapter

        viewModel.characters.observe(viewLifecycleOwner, Observer { characterList ->
            characters = characterList
            characterAdapter.updateCharacters(characters)

            // Check if the list is empty and show/hide views accordingly
            if (characters.isEmpty()) {
                recyclerView.visibility = View.GONE
                emptyView.visibility = View.VISIBLE
            } else {
                recyclerView.visibility = View.VISIBLE
                emptyView.visibility = View.GONE
            }
        })

        }

    override fun onEdit(position: Int, character: Character) {
        Toast.makeText(requireContext(), "Edit: ${character.name}", Toast.LENGTH_SHORT).show()

        val editDialog = EditCharacterDialogFragment(character) { updatedCharacter ->
            viewModel.updateCharacter(updatedCharacter)
            Toast.makeText(requireContext(), "Updated: ${updatedCharacter.name}", Toast.LENGTH_SHORT).show()
            // Update character list
            characters = characters.toMutableList().apply { set(position, updatedCharacter) }
            characterAdapter.updateCharacters(characters)
        }
        editDialog.show(parentFragmentManager, "EditCharacterDialog")
    }

    override fun onDelete(position: Int, character: Character) {
        viewModel.deleteCharacter(character)
        Toast.makeText(requireContext(), "${character.name} deleted", Toast.LENGTH_SHORT).show()
        // Update character list
        characters = characters.toMutableList().apply { removeAt(position) }
        characterAdapter.updateCharacters(characters)
    }
}