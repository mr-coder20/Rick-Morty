package com.example.rickandmorty.Fragments.createCharacters


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "characters")
data class Character(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val imageUrl: String,
    val species: String,
    val status: String,
    val origin: String
)