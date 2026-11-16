package com.example.ac13_pokedex.data

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json


@Serializable
private data class PokedexFile(val pokemon: List<Pokemon>)

private val json = Json { ignoreUnknownKeys = true }

class Pokedex {
    var store = mutableStateListOf<Pokemon>()
    var captured = mutableStateListOf<Int>()
    var isLoaded by mutableStateOf(false)
        private set
    var pokemonTypes = mutableStateListOf<PokemonType>()

    suspend fun loadPokedex(context: Context) {
        if (isLoaded) return
        val data = withContext(Dispatchers.IO) {   // file read off the main thread
            val text = context.assets.open("pokedex.json").bufferedReader().use { it.readText() }
            json.decodeFromString<PokedexFile>(text)
        }
        store.addAll(data.pokemon)
        isLoaded = true
    }

    fun toggleCaptured(id: Int) {
        if (id in captured) captured.remove(id)
        else captured.add(id)
    }

    fun byId(id: Int): Pokemon? = store.find { it.id == id }
    fun getTypeByName(name: String): PokemonType? = pokemonTypes.find { it.name == name }

}

val LocalPokedex = compositionLocalOf<Pokedex> {
    error("No pokedex found!")
}

@Composable
fun PokedexProvider(content: @Composable () -> Unit) {
    val context = LocalContext.current
    val pokedex = remember { Pokedex() }

    LaunchedEffect(pokedex) {               // like useEffect(() => {...}, [pokedex])
        pokedex.loadPokedex(context.applicationContext)
    }


    CompositionLocalProvider(LocalPokedex provides pokedex) {
        content()
    }
}