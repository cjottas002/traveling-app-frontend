package org.example.travelingapp.ui.views.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.example.travelingapp.ui.theme.Alpha
import org.example.travelingapp.ui.theme.Dimens
import org.example.travelingapp.ui.theme.TravelMonoFamily
import org.example.travelingapp.ui.theme.TravelingAppTheme

/**
 * Shared Meridian list row: content-first, flat, with a 1 dp bottom hairline.
 *
 * Use it for hotels, transports, reservations, profile menu items and any
 * dense list where a Material Card would add too much chrome.
 */
@Composable
fun TravelHairlineRow(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    leading: (@Composable RowScope.() -> Unit)? = null,
    trailing: (@Composable RowScope.() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val dividerColor = MaterialTheme.colorScheme.primary.copy(alpha = Alpha.divider)
    val baseModifier = modifier
        .fillMaxWidth()
        .drawBehind {
            drawLine(
                color = dividerColor,
                start = Offset(0f, size.height),
                end = Offset(size.width, size.height),
                strokeWidth = 1.dp.toPx()
            )
        }
        .padding(vertical = Dimens.spacingMd)
    val rowModifier = if (onClick != null) {
        baseModifier.clickable(onClick = onClick)
    } else {
        baseModifier
    }

    Row(
        modifier = rowModifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (leading != null) {
            leading()
            Spacer(Modifier.width(Dimens.spacingMd))
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Dimens.spacingXxs),
            content = content
        )
        if (trailing != null) {
            Spacer(Modifier.width(Dimens.spacingMd))
            trailing()
        }
    }
}

@Preview(showBackground = true, name = "Hairline row")
@Composable
private fun TravelHairlineRowPreview() {
    TravelingAppTheme {
        TravelHairlineRow(
            modifier = Modifier.padding(horizontal = Dimens.screenPadding),
            leading = {
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier
                        .size(44.dp)
                        .padding(Dimens.spacingXs)
                )
            },
            trailing = {
                Text(
                    text = "€ 184",
                    style = MaterialTheme.typography.titleMedium.copy(fontFamily = TravelMonoFamily),
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        ) {
            Text(
                text = "Madrid -> Marrakech",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "DIRECT · 3H 25",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
