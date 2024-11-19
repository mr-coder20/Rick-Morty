package com.example.rickandmorty.Fragments.showCreatedCharacters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.rickandmorty.Fragments.createCharacters.AppDatabase

class UserCharactersViewModelFactory(private val database: AppDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserCharactersViewModel::class.java)) {
            return UserCharactersViewModel(database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}