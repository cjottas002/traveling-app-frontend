package org.example.travelingapp.ui.views.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.travelingapp.ui.theme.Dimens
import org.example.travelingapp.ui.theme.TravelingAppTheme

/**
 * Editorial empty-state. Italic Bricolage glyph (large), display headline with
 * optional italic ember accent, body sub, and an optional ember outlined badge.
 * Used when a real list or surface has no content yet.
 */
@Composable
fun TravelEmptyState(
    glyph: String,
    title: String,
    modifier: Modifier = Modifier,
    accent: String? = null,
    body: String? = null,
    badge: String? = null
) {
    val ember = MaterialTheme.colorScheme.secondary
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Dimens.spacingXl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = glyph,
            style = MaterialTheme.typography.displayLarge.copy(
                fontSize = 64.sp,
                lineHeight = 64.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.outlineVariant
            )
        )
        Spacer(Modifier.height(Dimens.spacingLg))
        Text(
            text = headlineWithAccent(title, accent, ember),
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground
            ),
            textAlign = TextAlign.Center
        )
        if (!body.isNullOrBlank()) {
            Spacer(Modifier.height(Dimens.spacingSm))
            Text(
                text = body,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = Dimens.spacingMd)
            )
        }
        if (!badge.isNullOrBlank()) {
            Spacer(Modifier.height(Dimens.spacingLg))
            Surface(
                shape = RoundedCornerShape(Dimens.radiusXs),
                color = Color.Transparent,
                border = BorderStroke(1.dp, ember)
            ) {
                Text(
                    text = badge.uppercase(),
                    style = MaterialTheme.typography.labelMedium,
                    color = ember,
                    modifier = Modifier.padding(
                        horizontal = Dimens.spacingMd,
                        vertical = Dimens.spacingSm
                    )
                )
            }
        }
    }
}

private fun headlineWithAccent(
    title: String,
    accent: String?,
    accentColor: Color
): AnnotatedString = buildAnnotatedString {
    append(title)
    if (!accent.isNullOrBlank()) {
        append('\n')
        withStyle(
            SpanStyle(
                color = accentColor,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Normal
            )
        ) { append(accent) }
    }
}

@Preview(showBackground = true, name = "Empty state")
@Composable
private fun TravelEmptyStatePreview() {
    TravelingAppTheme {
        TravelEmptyState(
            glyph = "Aa",
            title = "Nada por",
            accent = "mostrar.",
            body = "Cuando haya contenido disponible, aparecerá en esta zona.",
            badge = "SIN RESULTADOS"
        )
    }
}
