package com.example.ac13_pokedex.ui.components

import android.graphics.BitmapFactory
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ac13_pokedex.data.PokemonType
import com.example.ac13_pokedex.data.composeColor

/**
 * AI AIDED IN THE CREATION OF THESE COMPONENTS
 */


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


@Composable
fun PokeballIndicator(captured: Boolean, modifier: Modifier = Modifier, size: Dp = 26.dp) {
    val scale by animateFloatAsState(
        targetValue = if (captured) 1f else 0.85f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "pokeballScale",
    )
    val outline = MaterialTheme.colorScheme.outline
    val ink = Color(0xFF222224)
    val red = Color(0xFFE3350D)
    Canvas(
        modifier
            .size(size)
            .graphicsLayer { scaleX = scale; scaleY = scale }
    ) {
        val r = this.size.minDimension / 2
        val stroke = r * 0.14f
        if (!captured) {
            drawCircle(outline, radius = r - stroke / 2, style = Stroke(stroke))
            drawCircle(outline, radius = r * 0.22f, style = Stroke(stroke * 0.8f))
            return@Canvas
        }
        drawArc(red, 180f, 180f, useCenter = true, size = this.size)
        drawArc(Color.White, 0f, 180f, useCenter = true, size = this.size)
        drawRect(ink, topLeft = Offset(0f, center.y - stroke / 2), size = Size(this.size.width, stroke))
        drawCircle(ink, radius = r - stroke / 2, style = Stroke(stroke))
        drawCircle(ink, radius = r * 0.32f)
        drawCircle(Color.White, radius = r * 0.18f)
    }
}