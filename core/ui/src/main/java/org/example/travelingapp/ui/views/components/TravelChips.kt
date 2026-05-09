package org.example.travelingapp.ui.views.components

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.example.travelingapp.ui.theme.Alpha
import org.example.travelingapp.ui.theme.Dimens
import org.example.travelingapp.ui.theme.TravelingAppTheme

/**
 * Meridian filter chip: ink fill when selected, bone surface when idle.
 *
 * This wrapper intentionally avoids Material's default container/indicator
 * behavior so lists can keep the editorial hairline language from the HTML.
 */
@Composable
fun TravelFilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val containerColor = if (selected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }
    val contentColor = if (selected) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }
    val border = if (selected) {
        null
    } else {
        BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = Alpha.divider))
    }

    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(Dimens.radiusFull),
        color = containerColor,
        contentColor = contentColor,
        border = border
    ) {
        Text(
            text = text.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(
                horizontal = Dimens.spacingMd,
                vertical = Dimens.spacingSm
            )
        )
    }
}

@Composable
fun TravelFilterChip(
    @StringRes textRes: Int,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TravelFilterChip(
        text = stringResource(textRes),
        selected = selected,
        onClick = onClick,
        modifier = modifier
    )
}

@Composable
fun TravelChipRow(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(Dimens.spacingSm),
        content = content
    )
}

@Preview(showBackground = true, name = "Filter chips")
@Composable
private fun TravelFilterChipPreview() {
    TravelingAppTheme {
        TravelChipRow(modifier = Modifier.padding(Dimens.screenPadding)) {
            TravelFilterChip(text = "Todos", selected = true, onClick = {})
            TravelFilterChip(text = "Riad", selected = false, onClick = {})
            TravelFilterChip(text = "Boutique", selected = false, onClick = {})
        }
    }
}
