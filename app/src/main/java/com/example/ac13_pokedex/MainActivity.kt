package com.example.ac13_pokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.ac13_pokedex.data.PokedexProvider
import com.example.ac13_pokedex.ui.theme.Ac13_pokedexTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.ac13_pokedex.data.LocalPokedex

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ac13_pokedexTheme {
                PokedexProvider {
                    PokedexApp()
                }
            }
        }
    }
}

@Composable
fun PokedexApp() {
    var detailsId by rememberSaveable { mutableStateOf<Int?>(null) }

    val detailsPokemon = detailsId?.let { LocalPokedex.current.byId(detailsId as Int) }
    if (detailsPokemon == null) {
        PokemonListScreen()
    } else {
        PokemonDetailsScreen(
            pokemon = detailsPokemon,
            onBack = { detailsId = null }
        )
    }
}
