package com.example.rickandmorty.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// Interface for the API
interface RickAndMortyApi {
    @GET("character")
    suspend fun getCharacters(@Query("page") page: Int): RickAndMortyApiResponse<Character>

    @GET("episode")
    suspend fun getEpisodes(@Query("page") page: Int): RickAndMortyApiResponse<Episode>
}


// Data model for characters
data class Character(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val gender: String,
    val image: String
)

// Data model for episodes
data class Episode(
    val id: Int,
    val name: String,
    val air_date: String,
    val episode: String
)

// Generic response model for API that returns a list of items (characters or episodes)
data class RickAndMortyApiResponse<T>(
    val results: List<T>
)

// Retrofit instance for accessing the API
object RetrofitInstance {
    private const val BASE_URL = "https://rickandmortyapi.com/api/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: RickAndMortyApi by lazy {
        retrofit.create(RickAndMortyApi::class.java)
    }
}
