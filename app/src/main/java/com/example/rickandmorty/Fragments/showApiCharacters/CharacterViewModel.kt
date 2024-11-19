package com.example.rickandmorty.Fragments.showApiCharacters

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.data.Character
import com.example.rickandmorty.data.RetrofitInstance
import kotlinx.coroutines.launch

class CharacterViewModel : ViewModel() {

    // MutableLiveData برای ذخیره و ارسال داده‌ها
    private val _characters = MutableLiveData<List<Character>>()
    val characters: LiveData<List<Character>> get() = _characters

    // MutableLiveData برای وضعیت بارگذاری
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    // MutableLiveData برای ذخیره و نمایش خطا
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    // متغیرهای مدیریت صفحات و بررسی اینکه آیا آخرین صفحه است
    private var currentPage = 1
    private var isLastPage = false

    init {
        _characters.value = emptyList()  // ابتدا لیست خالی
    }

    // متد برای دریافت شخصیت‌ها از API
    fun fetchNextPage() {
        if (isLastPage || _loading.value == true) return

        _loading.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getCharacters(currentPage)
                val newCharacters = response.results

                if (newCharacters.isNotEmpty()) {
                    _characters.value = _characters.value?.toMutableList()?.apply { addAll(newCharacters) }
                    currentPage++  // صفحه بعدی
                }

                // بررسی اینکه آیا آخرین صفحه است
                isLastPage = newCharacters.isEmpty()

            } catch (e: Exception) {
                _error.postValue("Error fetching characters: ${e.localizedMessage}")
            } finally {
                _loading.postValue(false)
            }
        }
    }
}
