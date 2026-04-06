package org.example.travelingapp.ui.views.components

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.example.travelingapp.ui.theme.Dimens
import org.example.travelingapp.ui.theme.TravelingAppTheme

/**
 * Tertiary inline link button for actions like "Forgot password?" or
 * "Don't have an account? Sign up". Fills the available width, guarantees
 * a 48 dp Material touch target and uses `bodyMedium` styling.
 *
 * Use this instead of raw `Text { clickable { … } }` so touch targets and
 * typography stay consistent everywhere.
 */
@Composable
fun TravelLinkButton(
    @StringRes textRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onBackground,
    textAlign: TextAlign = TextAlign.Center
) {
    AppText(
        textRes = textRes,
        style = MaterialTheme.typography.bodyMedium,
        color = color,
        textAlign = textAlign,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = Dimens.buttonHeight)
            .clickable(onClick = onClick)
            .padding(vertical = Dimens.spacingSm)
    )
}

@Preview(showBackground = true)
@Composable
private fun TravelLinkButtonPreview() {
    TravelingAppTheme {
        TravelLinkButton(
            textRes = android.R.string.ok,
            onClick = {},
            modifier = Modifier.padding(Dimens.screenPadding)
        )
    }
}
