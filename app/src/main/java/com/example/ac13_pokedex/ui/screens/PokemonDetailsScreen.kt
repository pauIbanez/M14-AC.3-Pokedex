package com.example.ac13_pokedex.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ac13_pokedex.data.LocalPokedex
import com.example.ac13_pokedex.data.Pokemon
import com.example.ac13_pokedex.data.PokemonType
import com.example.ac13_pokedex.data.composeColor
import com.example.ac13_pokedex.ui.components.Icons
import com.example.ac13_pokedex.ui.components.PokemonSprite
import com.example.ac13_pokedex.ui.components.TypeChip

@Composable
fun PokemonDetailsScreen(pokemon: Pokemon, onBack: () -> Unit) {
    val pokedex = LocalPokedex.current
    val types = pokemon.types.mapNotNull { pokedex.getTypeByName(it) }
    val mainColor = types.firstOrNull()?.composeColor() ?: MaterialTheme.colorScheme.primary
    val secondColor = types.getOrNull(1)?.composeColor() ?: mainColor
    val navBar = WindowInsets.navigationBars.asPaddingValues()


    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(bottom = navBar.calculateBottomPadding() + 24.dp),
    ) {

        Header(pokemon, types, mainColor, secondColor, onBack)


    }

}

@Composable
private fun Header(
    pokemon: Pokemon,
    types: List<PokemonType>,
    mainColor: Color,
    secondColor: Color,
    onBack: () -> Unit,
) {
    var showFemale by rememberSaveable(pokemon.id) { mutableStateOf(false) }
    val sprite = if (showFemale && pokemon.spriteFemale != null) pokemon.spriteFemale else pokemon.sprite

    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 36.dp, bottomEnd = 36.dp))
            .background(Brush.linearGradient(listOf(mainColor, secondColor.copy(alpha = 0.85f))))
            .statusBarsPadding()
            .padding(bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Back, "Back", tint = Color.White)
            }
            Spacer(Modifier.weight(1f))
            Text(pokemon.number, color = Color.White, fontWeight = FontWeight.Bold)
            Spacer(Modifier.width(2.dp))
        }

        Box(contentAlignment = Alignment.Center) {
            Box(Modifier.size(190.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.22f)))
            AnimatedContent(
                targetState = sprite,
                transitionSpec = { fadeIn(tween(200)) togetherWith fadeOut(tween(200)) },
                label = "sprite",
            ) { path ->
                PokemonSprite(path, contentDescription = pokemon.name, modifier = Modifier.size(220.dp))
            }
        }

        Text(
            pokemon.name,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Black,
            color = Color.White,
        )
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            types.forEach { TypeChip(it, large = true) }
        }

        if (pokemon.spriteFemale != null) {
            Spacer(Modifier.height(14.dp))
            Row(
                Modifier.clip(CircleShape).background(Color.Black.copy(alpha = 0.18f)).padding(10.dp, 3.dp),
            ) {
                GenderOption("♂ Male", !showFemale) { showFemale = false }
                GenderOption("♀ Female", showFemale) { showFemale = true }
            }
        }
    }
}

@Composable
private fun GenderOption(label: String, selected: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = if (selected) Color.White else Color.Transparent,
        contentColor = if (selected) Color.Black else Color.White,
    ) {
        Text(
            label,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelLarge,
        )
    }
}
