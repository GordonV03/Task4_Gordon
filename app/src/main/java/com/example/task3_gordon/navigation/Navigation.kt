package com.example.task3_gordon.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.task3_gordon.Filter.HeroFilterScreen
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
    object HeroFilter : Screen("hero_filter", "Filter")
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
            composable(Screen.HeroFilter.route) {
                HeroFilterScreen()
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        Screen.Home,
        Screen.Notifications,
        Screen.HeroFilter,
    )
    BottomNavigation {
        val navBackStackEntry = navController.currentBackStackEntryAsState().value
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { screen ->
            BottomNavigationItem(
                icon = {
                    when (screen) {
                        is Screen.Home -> Icon(Icons.Filled.Home, contentDescription = "Home")
                        is Screen.Notifications -> Icon(Icons.Filled.Notifications, contentDescription = "Notifications")
                        is Screen.HeroFilter -> Icon(Icons.Filled.List, contentDescription = "Filter")
                        Screen.Details -> TODO()
                    }
                },
                label = { Text(screen.title) },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

fun NavGraphBuilder.addHeroFilterScreen(navController: NavController) {
    composable(Screen.HeroFilter.route) {
        HeroFilterScreen() // Передача navController
    }
}