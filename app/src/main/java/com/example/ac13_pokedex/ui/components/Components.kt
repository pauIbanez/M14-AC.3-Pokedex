package com.example.ac13_pokedex.ui.components

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ac13_pokedex.data.PokemonType
import com.example.ac13_pokedex.data.composeColor

@Composable
fun TypeChip(type: PokemonType, modifier: Modifier = Modifier, large: Boolean = false) {
    val color = type.composeColor()
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(color)
            .padding(horizontal = if (large) 14.dp else 8.dp, vertical = if (large) 4.dp else 2.dp),
    ) {
        Text(
            text = type.name.uppercase(),
            color = Color.White,
            fontSize = if (large) 13.sp else 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.8.sp,
        )
    }
}

@Composable
fun PokemonSprite(path: String, contentDescription: String?, modifier: Modifier = Modifier) {
    val assets = LocalContext.current.assets
    val bitmap = remember(path) {
        assets.open(path).use { BitmapFactory.decodeStream(it).asImageBitmap() }
    }
    Image(
        bitmap = bitmap,
        contentDescription = contentDescription,
        modifier = modifier,
        filterQuality = FilterQuality.None,
    )
}