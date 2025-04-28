package org.example.travelingapp.ui.views.components


import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp

@Composable
fun AppIconButton(
    @DrawableRes iconRes: Int,
    contentDescription: String? = null,
    modifier: Modifier = Modifier,
    iconSize: Dp = Dp.Unspecified,
    iconTint: Color = Color.Unspecified,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = contentDescription,
            modifier = if (iconSize != Dp.Unspecified) Modifier.size(iconSize) else Modifier,
            tint = iconTint
        )
    }
}
