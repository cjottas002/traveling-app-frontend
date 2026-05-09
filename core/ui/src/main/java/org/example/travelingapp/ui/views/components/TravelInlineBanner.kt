package org.example.travelingapp.ui.views.components

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.example.travelingapp.ui.theme.Alpha
import org.example.travelingapp.ui.theme.Dimens
import org.example.travelingapp.ui.theme.LocalTravelColors
import org.example.travelingapp.ui.theme.TravelingAppTheme

enum class TravelBannerTone {
    Synced,
    Pending,
    Offline,
    Error,
    Info
}

/**
 * Inline Meridian banner for entity-level feedback.
 *
 * Prefer this over Snackbar/Toast when the state belongs to a row, form or
 * screen section. Global transient feedback remains a separate concern.
 */
@Composable
fun TravelInlineBanner(
    title: String,
    modifier: Modifier = Modifier,
    body: String? = null,
    tone: TravelBannerTone = TravelBannerTone.Info
) {
    val travelColors = LocalTravelColors.current
    val accent = when (tone) {
        TravelBannerTone.Synced -> travelColors.synced
        TravelBannerTone.Pending -> travelColors.pending
        TravelBannerTone.Offline -> travelColors.offline
        TravelBannerTone.Error -> MaterialTheme.colorScheme.error
        TravelBannerTone.Info -> MaterialTheme.colorScheme.primary
    }
    val container = when (tone) {
        TravelBannerTone.Synced -> travelColors.synced.copy(alpha = 0.12f)
        TravelBannerTone.Pending -> travelColors.pending.copy(alpha = 0.18f)
        TravelBannerTone.Offline -> MaterialTheme.colorScheme.surfaceVariant
        TravelBannerTone.Error -> MaterialTheme.colorScheme.errorContainer
        TravelBannerTone.Info -> MaterialTheme.colorScheme.primaryContainer
    }
    val contentColor = when (tone) {
        TravelBannerTone.Error -> MaterialTheme.colorScheme.onErrorContainer
        else -> MaterialTheme.colorScheme.onBackground
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(Dimens.radiusXs),
        color = container,
        contentColor = contentColor,
        border = BorderStroke(1.dp, accent.copy(alpha = Alpha.subtle))
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier.padding(Dimens.spacingMd)
        ) {
            Surface(
                shape = RoundedCornerShape(Dimens.radiusFull),
                color = accent,
                contentColor = Color.White
            ) {
                Text(
                    text = tone.name.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(
                        horizontal = Dimens.spacingSm,
                        vertical = Dimens.spacingXs
                    )
                )
            }
            Column(
                modifier = Modifier.padding(start = Dimens.spacingMd)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = contentColor
                )
                if (!body.isNullOrBlank()) {
                    Text(
                        text = body,
                        style = MaterialTheme.typography.bodySmall,
                        color = contentColor.copy(alpha = Alpha.muted),
                        modifier = Modifier.padding(top = Dimens.spacingXxs)
                    )
                }
            }
        }
    }
}

@Composable
fun TravelInlineBanner(
    @StringRes titleRes: Int,
    modifier: Modifier = Modifier,
    @StringRes bodyRes: Int? = null,
    tone: TravelBannerTone = TravelBannerTone.Info
) {
    TravelInlineBanner(
        title = stringResource(titleRes),
        modifier = modifier,
        body = bodyRes?.let { stringResource(it) },
        tone = tone
    )
}

@Preview(showBackground = true, name = "Inline banners")
@Composable
private fun TravelInlineBannerPreview() {
    TravelingAppTheme {
        Column(
            modifier = Modifier.padding(Dimens.screenPadding)
        ) {
            TravelInlineBanner(
                title = "Reserva sincronizada",
                body = "El servidor ya tiene la última versión.",
                tone = TravelBannerTone.Synced
            )
            TravelVerticalSpacer(Dimens.spacingMd)
            TravelInlineBanner(
                title = "Pendiente de sincronizar",
                body = "Se enviará cuando vuelva la conexión.",
                tone = TravelBannerTone.Pending
            )
        }
    }
}
