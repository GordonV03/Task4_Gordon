package com.example.task3_gordon.domain.entities

data class Hero(
    val heroId: Int,
    val localizedName: String,
    val roles: String // Используем для хранения описания и ролей
)