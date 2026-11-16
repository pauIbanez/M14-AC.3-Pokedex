package com.example.ac13_pokedex.data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import kotlinx.serialization.Serializable


class Pokedex {
    var store = mutableStateListOf<Pokemon>()
    var captured = mutableStateListOf<Int>()
    var isLoaded: Boolean = false


    fun loadPokedex() {}

    fun toggleCaptured(id: Int) {
        if (id in captured) captured.remove(id)
        else captured.add(id)
    }
}

val LocalPokedex = compositionLocalOf<Pokedex> {
    error("No pokedex found!")
}

@Composable
fun PokedexProvider(content: @Composable () -> Unit) {
    val contextValue = remember { Pokedex() }
    CompositionLocalProvider(LocalPokedex provides contextValue) {
        content()
    }
}