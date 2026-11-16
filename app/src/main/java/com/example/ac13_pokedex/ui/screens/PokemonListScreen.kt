package com.example.ac13_pokedex.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ac13_pokedex.data.LocalPokedex
import com.example.ac13_pokedex.data.Pokemon
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.example.ac13_pokedex.data.composeColor

@Composable
fun PokemonListScreen(
    listState: LazyListState,
    onOpen: (Int) -> Unit,
    onToggleCaptured: (Int) -> Unit,
) {
    val pokedex = LocalPokedex.current
    val pokemons: List<Pokemon> = remember(pokedex.store, pokedex.captured) { pokedex.store }

    val navBar = WindowInsets.navigationBars.asPaddingValues()

    Column(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {

        // Header
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(
                start = 12.dp, end = 12.dp, top = 4.dp,
                bottom = 12.dp + navBar.calculateBottomPadding(),
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {

            items(pokemons, key = { it.id }) {
                PokemonItem(
                    pokemon = it,
                    captured = it.id in pokedex.captured,
                    onClick = {
                        onOpen(it.id)
                    },
                    onToggleCaptured = { onToggleCaptured(it.id) },
                )
            }
        }
    }
}

@Composable
fun PokemonItem(
    pokemon: Pokemon,
    captured: Boolean,
    onClick: () -> Unit,
    onToggleCaptured: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val pokedex = LocalPokedex.current
    val mainColor: Color =
        pokedex.getTypeByName(pokemon.types.first())?.composeColor() ?: MaterialTheme.colorScheme.primary

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = mainColor,
        onClick = onClick,
    ) {
        Row(
            Modifier.padding(start = 8.dp, end = 4.dp, top = 6.dp, bottom = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                pokemon.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}