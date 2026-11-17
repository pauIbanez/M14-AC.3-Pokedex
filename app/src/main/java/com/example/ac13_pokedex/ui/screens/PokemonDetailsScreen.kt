package com.example.ac13_pokedex.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ac13_pokedex.data.LocalPokedex
import com.example.ac13_pokedex.data.Pokemon
import com.example.ac13_pokedex.data.PokemonType
import com.example.ac13_pokedex.data.composeColor
import com.example.ac13_pokedex.ui.components.Icons
import com.example.ac13_pokedex.ui.components.PokeballIndicator
import com.example.ac13_pokedex.ui.components.PokemonSprite
import com.example.ac13_pokedex.ui.components.TypeChip

@Composable
fun PokemonDetailsScreen(pokemon: Pokemon, onBack: () -> Unit) {
    val pokedex = LocalPokedex.current
    val types = pokemon.types.mapNotNull { pokedex.getTypeByName(it) }
    val mainColor = types.firstOrNull()?.composeColor() ?: MaterialTheme.colorScheme.primary
    val secondColor = types.getOrNull(1)?.composeColor() ?: mainColor
    val navBar = WindowInsets.navigationBars.asPaddingValues()
    val captured = pokemon.id in pokedex.captured

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(bottom = navBar.calculateBottomPadding() + 24.dp),
    ) {

        Header(pokemon, types, mainColor, secondColor, onBack)

        Column(
            Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Spacer(Modifier.height(4.dp))
            CaptureButton(captured, mainColor, { pokedex.toggleCaptured(pokemon.id) })
            InfoCard(pokemon)
            SectionCard("Description") {
                Text(
                    pokemon.description,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            StatsCard(pokemon, mainColor)
        }
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


@Composable
private fun CaptureButton(captured: Boolean, color: Color, onToggle: () -> Unit) {
    val content: @Composable () -> Unit = {
        PokeballIndicator(captured, size = 22.dp)
        Spacer(Modifier.width(10.dp))
        Text(
            if (captured) "Release" else "Capture",
            style = MaterialTheme.typography.titleMedium,
        )
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        if (captured) {
            OutlinedButton(onClick = onToggle, modifier = Modifier.fillMaxWidth().height(52.dp)) { content() }
        } else {
            Button(
                onClick = onToggle,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            ) { content() }
        }
        Spacer(Modifier.height(6.dp))
        Text(
            if (captured) "Captured" else "Not captured",
            style = MaterialTheme.typography.labelLarge,
            color = if (captured) color else MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun SectionCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surfaceContainer,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(20.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
private fun InfoCard(pokemon: Pokemon) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surfaceContainer,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(Modifier.padding(vertical = 18.dp)) {
            InfoItem("Height", "%.1f m".format(pokemon.height))
            InfoItem(
                "Weight", " % .1f kg ".format(pokemon.weight)
            )
            InfoItem(
                "Experience", pokemon.baseExperience.toString()
            )
        }
    }
}

@Composable
private fun RowScope.InfoItem(label: String, value: String) {
    Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text(
            label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun StatsCard(pokemon: Pokemon, color: Color) {
    val s = pokemon.stats
    val rows = listOf(
        "Health" to s.hp,
        "Attack" to s.attack,
        "Defense" to s.defense,
        "Special attack" to s.specialAttack,
        "Special defense" to s.specialDefense,
        "Speed" to s.speed,
    )

    var shown by rememberSaveable(pokemon.id) { mutableStateOf(false) }
    LaunchedEffect(pokemon.id) { shown = true }

    SectionCard("Stats") {
        rows.forEach { (label, value) ->
            val progress by animateFloatAsState(
                targetValue = if (shown) value / 255f else 0f,
                animationSpec = tween(700),
                label = "stat",
            )
            Row(Modifier.padding(vertical = 5.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    label,
                    modifier = Modifier.width(84.dp),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    value.toString(),
                    modifier = Modifier.width(36.dp),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                )
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.weight(1f).height(8.dp).clip(CircleShape),
                    color = color,
                    trackColor = color.copy(alpha = 0.18f),
                    drawStopIndicator = {},
                )
            }
        }
    }
}