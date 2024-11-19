package com.example.rickandmorty.Fragments.showCustomizedCharacters

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface RickAndMortyApiService2 {
    // درخواست برای گرفتن یک شخصیت با آیدی مشخص
    @GET("character/{id}")
    fun getCharacter(@Path("id") id: Int): Call<Character2>

    companion object {
        private const val BASE_URL = "https://rickandmortyapi.com/api/"

        fun create(): RickAndMortyApiService2 {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(RickAndMortyApiService2::class.java)
        }
    }

    // متد برای گرفتن یک شخصیت تصادفی با ارسال id تصادفی
    @GET("character/{id}")
    fun getRandomCharacter(@Path("id") id: Int): Call<Character2>
}
