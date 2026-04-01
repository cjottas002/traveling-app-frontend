package org.example.travelingapp.ui.views.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import org.example.travelingapp.ui.theme.Dimens

@Composable
fun PagerIndicator(
    size: Int,
    currentPage: Int,
    modifier: Modifier = Modifier,
    activeColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.primary,
    inactiveColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.outlineVariant
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Dimens.spacingSm),
        modifier = modifier
    ) {
        repeat(size) {
            Indicator(
                isSelected = it == currentPage,
                activeColor = activeColor,
                inactiveColor = inactiveColor
            )
        }
    }
}

@Composable
private fun Indicator(
    isSelected: Boolean,
    activeColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.primary,
    inactiveColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.outlineVariant
) {
    val width by animateDpAsState(
        targetValue = if (isSelected) 28.dp else 10.dp,
        animationSpec = tween(300),
        label = "indicator_width"
    )
    val color by animateColorAsState(
        targetValue = if (isSelected) activeColor else inactiveColor,
        animationSpec = tween(300),
        label = "indicator_color"
    )

    Box(
        modifier = Modifier
            .height(10.dp)
            .width(width)
            .clip(CircleShape)
            .background(color)
    )
}
