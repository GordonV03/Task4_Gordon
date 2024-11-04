package com.example.task3_gordon.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.task3_gordon.data.repositories.OpenDotaHeroRepository
import com.example.task3_gordon.domain.entities.Hero
import com.example.task3_gordon.domain.usecases.GetHeroesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel : ViewModel() {

    private val _heroes = MutableStateFlow<List<Hero>>(emptyList())
    val heroes: StateFlow<List<Hero>> get() = _heroes

    init {
        fetchHeroes()
    }

    private fun fetchHeroes() {
        val repository = OpenDotaHeroRepository()
        val useCase = GetHeroesUseCase(repository)

        // Запуск сетевого запроса в IO контексте
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val heroesList = useCase.execute()
                _heroes.value = heroesList
                Log.d("HomeViewModel", "Heroes received: ${heroesList.size}")
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("HomeViewModel", "Error fetching heroes", e)
                // Обработка ошибок
            }
        }
    }
}