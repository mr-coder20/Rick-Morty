package com.example.rickandmorty.Fragments.showCreatedCharacters


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.Fragments.createCharacters.AppDatabase
import com.example.rickandmorty.Fragments.createCharacters.Character
import kotlinx.coroutines.launch

class UserCharactersViewModel(private val database: AppDatabase) : ViewModel() {

    val characters: LiveData<List<Character>> = database.characterDao().getAllCharactersLive()

    fun addCharacter(character: Character) {
        viewModelScope.launch {
            database.characterDao().insert(character)
        }
    }
    fun updateCharacter(character: Character) {
        viewModelScope.launch {
            database.characterDao().updateCharacter(character)
        }
    }
    fun deleteCharacter(character: Character) {
        viewModelScope.launch {
            database.characterDao().deleteCharacter(character)
        }
    }
}