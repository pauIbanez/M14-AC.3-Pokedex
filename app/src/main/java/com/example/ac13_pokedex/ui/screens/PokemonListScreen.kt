package com.example.ac13_pokedex.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FilterChip
import com.example.ac13_pokedex.Filters
import com.example.ac13_pokedex.ui.components.PokemonSprite
import com.example.ac13_pokedex.ui.components.TypeChip
import com.example.ac13_pokedex.ui.components.PokeballIndicator
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import androidx.compose.runtime.setValue
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay


@Composable
fun PokemonListScreen(
    listState: LazyListState,
    onOpen: (Int) -> Unit,
    onToggleCaptured: (Int) -> Unit,
    filter: Filters,
    onFilterChange: (Filters) -> Unit,
) {
    val pokedex = LocalPokedex.current
    val navBar = WindowInsets.navigationBars.asPaddingValues()

    val keepAlive = remember(filter) { mutableStateListOf<Int>() }
    val keepAliveTimers = remember { mutableMapOf<Int, Job>() }
    val scope = rememberCoroutineScope()


    val visible by remember(keepAlive, pokedex.store, filter) {
        derivedStateOf {
            when (filter) {
                Filters.Captured -> pokedex.store.filter { pokemon -> pokemon.id in pokedex.captured || pokemon.id in keepAlive }
                Filters.Missing -> pokedex.store.filter { pokemon -> pokemon.id !in pokedex.captured || pokemon.id in keepAlive }
                else -> pokedex.store
            }
        }
    }



    Column(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Header()
        FilterRow(filter, onFilterChange)

        val emptyText = mapOf(
            Filters.All to "No pokemon loaded!",
            Filters.Captured to "You haven't captured any pokemon!",
            Filters.Missing to "You captured every pokemon!"
        )

        if (visible.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.TopCenter) {
                Text(
                    emptyText[filter] as String,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            return@Column
        }

        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(
                start = 12.dp, end = 12.dp, top = 4.dp,
                bottom = 12.dp + navBar.calculateBottomPadding(),
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {

            items(visible, key = { it.id }) {
                PokemonItem(
                    pokemon = it,
                    captured = it.id in pokedex.captured,
                    onClick = {
                        onOpen(it.id)
                    },
                    onToggleCaptured = {
                        onToggleCaptured(it.id)
                        keepAliveTimers[it.id]?.cancel()
                        keepAlive.add(it.id)
                        keepAliveTimers[it.id] = scope.launch {
                            delay(1000)
                            keepAlive.remove(it.id)
                            keepAliveTimers.remove(it.id)
                        }
                    },
                )
            }
        }
    }
}


@Composable
private fun Header() {
    val focusManager = LocalFocusManager.current
    val onPrimary = MaterialTheme.colorScheme.onPrimary
    val pokedex = LocalPokedex.current
    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
            .background(MaterialTheme.colorScheme.primary)
            .statusBarsPadding()
            .padding(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 20.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Spacer(Modifier.width(12.dp))
            Text(
                "Pokedex",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black,
                color = onPrimary,
            )
            Spacer(Modifier.weight(1f))
            Text(
                "${pokedex.captured.size} / ${pokedex.store.size}",
                style = MaterialTheme.typography.labelLarge,
                color = onPrimary,
            )
        }
        Spacer(Modifier.height(12.dp))
    }
}

@Composable
fun FilterRow(current: Filters, onFilterChange: (Filters) -> Unit) {
    Row(
        Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        val labels = mapOf(
            Filters.All to "All",
            Filters.Captured to "Captured",
            Filters.Missing to "Missing"
        )
        labels.forEach { (value, label) ->
            FilterChip(
                selected = current == value,
                onClick = { onFilterChange(value) },
                label = { Text(label) },
            )
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
        color = MaterialTheme.colorScheme.surfaceContainer,
        onClick = onClick,
    ) {
        Row(
            Modifier.padding(start = 8.dp, end = 4.dp, top = 6.dp, bottom = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                Modifier.size(64.dp).clip(CircleShape).background(mainColor.copy(alpha = 0.22f)),
                contentAlignment = Alignment.Center,
            ) {
                PokemonSprite(pokemon.sprite, contentDescription = null, modifier = Modifier.size(60.dp))
            }
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    pokemon.number,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    pokemon.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    pokemon.types.mapNotNull { pokedex.getTypeByName(it) }.forEach { TypeChip(it) }
                }
            }
            val description = if (captured) "Captured" else "Not captured"
            Box(
                Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .clickable(onClickLabel = description, onClick = onToggleCaptured),
                contentAlignment = Alignment.Center,
            ) {
                PokeballIndicator(captured)
            }
        }
    }
}

