package com.example.task3_gordon.data.repositories

import com.example.task3_gordon.domain.entities.Hero

interface HeroRepository {
    fun getHeroes(): List<Hero>
}