package com.example.task3_gordon.domain.usecases

import com.example.task3_gordon.domain.entities.Hero
import com.example.task3_gordon.data.repositories.HeroRepository

class GetHeroesUseCase(private val repository: HeroRepository) {
    fun execute(): List<Hero> {
        return repository.getHeroes()
    }
}