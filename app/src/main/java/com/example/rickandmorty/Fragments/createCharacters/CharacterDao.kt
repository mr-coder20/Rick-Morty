package com.example.rickandmorty.Fragments.createCharacters

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface CharacterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(character: Character)

    @Query("SELECT * FROM characters")
    fun getAllCharactersLive(): LiveData<List<Character>>

    @Update
    suspend fun updateCharacter(character: Character)

    @Delete
    suspend fun deleteCharacter(character: Character)
}