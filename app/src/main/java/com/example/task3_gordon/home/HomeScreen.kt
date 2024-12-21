package com.example.task3_gordon.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.task3_gordon.domain.entities.Hero
import com.example.task3_gordon.navigation.Screen
import com.google.gson.Gson

@Composable
fun HomeScreen(viewModel: HomeViewModel = viewModel()) {
    val heroes by viewModel.heroes.collectAsState()

    LazyColumn {
        items(heroes) { hero ->
            Text(text = hero.localizedName)
        }
    }
}

@Composable
fun HeroItem(hero: Hero, onClick: () -> Unit) {
    var isFavorite by remember { mutableStateOf(false) }  // Состояние для избранности героя

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Имя героя
        Text(
            text = hero.localizedName,
            modifier = Modifier.weight(1f)  // Позволяет Text занимать доступное пространство
        )

        // Кнопка для переключения "избранности"
        IconButton(onClick = { isFavorite = !isFavorite }) {
            Icon(
                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = if (isFavorite) "Удалить из избранного" else "Добавить в избранное"
            )
        }
    }
}
