package com.example.ac13_pokedex.data

import androidx.compose.ui.graphics.Color
import kotlinx.serialization.Serializable
import android.graphics.Color as AndroidColor

@Serializable
data class Stats(
    val hp: Int,
    val attack: Int,
    val defense: Int,
    val specialAttack: Int,
    val specialDefense: Int,
    val speed: Int,
) {
    val total: Int get() = hp + attack + defense + specialAttack + specialDefense + speed
}


@Serializable
data class PokemonType(
    val id: String,
    val name: String,
    val color: String,
)

fun PokemonType.composeColor(): Color = Color(AndroidColor.parseColor(color))

@Serializable
data class Pokemon(
    val id: Int,
    val identifier: String,
    val name: String,
    val types: List<String>,
    val height: Double,
    val weight: Double,
    val baseExperience: Int,
    val stats: Stats,
    val description: String,
    val sprite: String,
) {
    val number: String get() = "#%03d".format(id)
}