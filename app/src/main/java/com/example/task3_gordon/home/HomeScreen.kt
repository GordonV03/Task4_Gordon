package com.example.task3_gordon.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.task3_gordon.domain.entities.Hero
import com.google.gson.Gson

@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = viewModel()) {
    val heroes by viewModel.heroes.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(heroes) { hero ->
            HeroItem(hero) {
                // Сериализация объекта Hero в строку JSON
                val heroJson = Gson().toJson(hero)
                // Переход на экран деталей с передачей сериализованного объекта
                navController.navigate("details/$heroJson")
            }
        }
    }
}

@Composable
fun HeroItem(hero: Hero, onClick: () -> Unit) {
    Text(
        text = hero.localizedName,
        modifier = Modifier
            .clickable(onClick = onClick)
    )
}
