package com.example.task3_gordon.details

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import com.example.task3_gordon.domain.entities.Hero

@Composable
fun ItemDetailScreen(navController: NavController, hero: Hero) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = hero.localizedName) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(text = "Hero Name: ${hero.localizedName}", style = MaterialTheme.typography.h5)
            Text(text = "Roles: ${hero.roles}", style = MaterialTheme.typography.body1)
        }
    }
}
