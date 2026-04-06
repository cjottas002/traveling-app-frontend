package org.example.travelingapp.ui.views.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import org.example.travelingapp.ui.theme.Dimens
import org.example.travelingapp.ui.theme.TravelingAppTheme

/**
 * Visual style for a [TravelCard]. Kept as a sealed hierarchy of discrete
 * choices so screens pick a semantic intent instead of tweaking raw values.
 *
 * - [SurfaceStyle]: opaque `surface` color. Default for cards on plain backgrounds.
 * - [TranslucentStyle]: `surface` with alpha, meant to sit on top of a photo
 *   or hero image (login, onboarding, details).
 * - [ElevatedStyle]: opaque `surface` with a tonal elevation for floating
 *   cards in lists or dashboards.
 */
enum class TravelCardStyle {
    Surface,
    Translucent,
    Elevated
}

/**
 * Global card container. Any screen that needs "a rounded surface with
 * content inside" should wrap its content in [TravelCard] instead of
 * building its own [Surface] + [RoundedCornerShape] combo. Spacing and
 * radius come from [Dimens] so changing them once updates the whole app.
 */
@Composable
fun TravelCard(
    modifier: Modifier = Modifier,
    style: TravelCardStyle = TravelCardStyle.Surface,
    cornerRadius: Dp = Dimens.radiusLg,
    contentPadding: Dp = Dimens.cardPadding,
    content: @Composable () -> Unit
) {
    val shape = RoundedCornerShape(cornerRadius)
    val containerColor = when (style) {
        TravelCardStyle.Surface -> MaterialTheme.colorScheme.surface
        TravelCardStyle.Translucent -> MaterialTheme.colorScheme.surface.copy(alpha = 0.92f)
        TravelCardStyle.Elevated -> MaterialTheme.colorScheme.surface
    }
    val tonalElevation = when (style) {
        TravelCardStyle.Surface -> Dimens.elevationNone
        TravelCardStyle.Translucent -> Dimens.elevationMd
        TravelCardStyle.Elevated -> Dimens.elevationNone
    }
    val shadowElevation = when (style) {
        TravelCardStyle.Surface -> Dimens.elevationNone
        TravelCardStyle.Translucent -> Dimens.elevationNone
        TravelCardStyle.Elevated -> Dimens.elevationLg
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = shape,
        color = containerColor,
        tonalElevation = tonalElevation,
        shadowElevation = shadowElevation
    ) {
        Column(modifier = Modifier.padding(contentPadding)) {
            content()
        }
    }
}

@Preview(showBackground = true, name = "Surface")
@Composable
private fun TravelCardSurfacePreview() {
    TravelingAppTheme {
        TravelCard(modifier = Modifier.padding(Dimens.screenPadding)) {
            AppText(text = "Surface card")
            VerticalSpacer(Dimens.cardSpacing)
            AppText(text = "Default card for content over a plain background.")
        }
    }
}

@Preview(showBackground = true, name = "Translucent")
@Composable
private fun TravelCardTranslucentPreview() {
    TravelingAppTheme {
        TravelCard(
            style = TravelCardStyle.Translucent,
            modifier = Modifier.padding(Dimens.screenPadding)
        ) {
            AppText(text = "Translucent card")
            VerticalSpacer(Dimens.cardSpacing)
            AppText(text = "Sits on top of hero images.")
        }
    }
}

@Preview(showBackground = true, name = "Elevated")
@Composable
private fun TravelCardElevatedPreview() {
    TravelingAppTheme {
        TravelCard(
            style = TravelCardStyle.Elevated,
            modifier = Modifier.padding(Dimens.screenPadding)
        ) {
            AppText(text = "Elevated card")
            VerticalSpacer(Dimens.cardSpacing)
            AppText(text = "For floating cards in lists.")
        }
    }
}
