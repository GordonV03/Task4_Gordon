package com.example.task3_gordon.Filter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.task3_gordon.home.HomeViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import com.example.task3_gordon.domain.entities.Hero
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.launch

// ViewModel для фильтрации героев
class HeroFilterViewModel() : ViewModel() {
    private val _allHeroes = MutableStateFlow<List<Hero>>(emptyList())
    val allHeroes: StateFlow<List<Hero>> get() = _allHeroes

    private val _filteredHeroes = MutableStateFlow<List<Hero>>(emptyList())
    val filteredHeroes: StateFlow<List<Hero>> get() = _filteredHeroes

    private val _filterName = MutableStateFlow("")
    val filterName: StateFlow<String> get() = _filterName

    private val _filterRoles = MutableStateFlow<List<String>>(emptyList())
    val filterRoles: StateFlow<List<String>> get() = _filterRoles

    private val _isDefaultState = MutableStateFlow(true)
    val isDefaultState: StateFlow<Boolean> get() = _isDefaultState

    fun initializeHeroes(allHeroes: List<Hero>) {
        _allHeroes.value = allHeroes
        applyFilters()
    }

    fun setFilterName(name: String) {
        _filterName.value = name
        applyFilters()
    }

    fun setFilterRoles(roles: List<String>) {
        _filterRoles.value = roles
        applyFilters()
    }

    fun applyFilters() {
        _filteredHeroes.value = _allHeroes.value.filter { hero ->
            hero.localizedName.contains(filterName.value, ignoreCase = true) &&
                    filterRoles.value.all { it in hero.roles }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HeroFilterScreen(
    homeViewModel: HomeViewModel = viewModel(),
    filterViewModel: HeroFilterViewModel = viewModel()
) {
    val allHeroes by homeViewModel.heroes.collectAsState()
    filterViewModel.initializeHeroes(allHeroes)

    val filterName by filterViewModel.filterName.collectAsState()
    val filterRoles by filterViewModel.filterRoles.collectAsState()
    val filteredHeroes by filterViewModel.filteredHeroes.collectAsState()
    val isDefaultState by filterViewModel.isDefaultState.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        // Индикатор состояния фильтра
        Text(
            text = if (isDefaultState) "Фильтры отсутствуют" else "Фильтры применены",
            color = if (isDefaultState) Color.Gray else Color.Green
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = filterName,
            onValueChange = { filterViewModel.setFilterName(it) },
            label = { Text("Filter by Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        val allRoles = listOf("Carry", "Support", "Nuker", "Disabler", "Jungler", "Durable", "Escape", "Pusher", "Initiator")

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(allRoles.size) { index ->
                val role = allRoles[index]
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = role in filterRoles,
                        onCheckedChange = {
                            val newRoles = if (it) filterRoles + role else filterRoles - role
                            filterViewModel.setFilterRoles(newRoles)
                        }
                    )
                    Text(text = role)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                filterViewModel.applyFilters()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Применить фильтр")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(filteredHeroes) { hero ->
                Text(text = hero.localizedName)
            }
        }
    }
}
