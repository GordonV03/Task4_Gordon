package com.example.task3_gordon.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.task3_gordon.details.ItemDetailScreen
import com.example.task3_gordon.home.HomeScreen
import com.example.task3_gordon.notifications.NotificationsScreen
import com.example.task3_gordon.domain.entities.Hero
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

sealed class Screen(val route: String, val title: String) {
    object Home : Screen("home", "Home")
    object Notifications : Screen("notifications", "Notifications")
    object Details : Screen("details/{hero}", "Details")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(navController)
            }
            composable(Screen.Notifications.route) {
                NotificationsScreen()
            }
            composable(
                route = Screen.Details.route,
                arguments = listOf(
                    navArgument("hero") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val heroJson = backStackEntry.arguments?.getString("hero") ?: "{}"
                val heroType = object : TypeToken<Hero>() {}.type
                val hero = Gson().fromJson<Hero>(heroJson, heroType)
                ItemDetailScreen(navController, hero)
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        Screen.Home,
        Screen.Notifications
    )
    BottomNavigation {
        val navBackStackEntry = navController.currentBackStackEntry
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { screen ->
            BottomNavigationItem(
                icon = {
                    when (screen) {
                        is Screen.Home -> Icon(Icons.Filled.Home, contentDescription = "Home")
                        is Screen.Notifications -> Icon(Icons.Filled.Notifications, contentDescription = "Notifications")
                        else -> {}
                    }
                },
                label = { Text(screen.title) },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        // Удаляем предыдущие маршруты из стека до стартового
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        // Запускаем верхний элемент без создания нового экземпляра
                        launchSingleTop = true
                        // Восстанавливаем состояние
                        restoreState = true
                    }
                }
            )
        }
    }
}