package org.example.travelingapp.ui.views.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.example.travelingapp.ui.theme.Dimens
import org.example.travelingapp.ui.theme.TravelingAppTheme

@Composable
fun TravelPrimaryButton(
    @StringRes textRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(Dimens.buttonHeight),
        shape = RoundedCornerShape(Dimens.radiusMd),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary,
            disabledContainerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f),
            disabledContentColor = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.4f)
        )
    ) {
        Text(
            text = stringResource(textRes),
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
fun TravelSecondaryButton(
    @StringRes textRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(Dimens.buttonHeight),
        shape = RoundedCornerShape(Dimens.radiusMd),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary,
            disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
        )
    ) {
        Text(
            text = stringResource(textRes),
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
fun TravelTextButton(
    @StringRes textRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    androidx.compose.material3.TextButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Text(
            text = stringResource(textRes),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview(showBackground = true, name = "Buttons")
@Composable
private fun ButtonsPreview() {
    TravelingAppTheme {
        Column(modifier = Modifier.padding(Dimens.screenPadding)) {
            TravelPrimaryButton(textRes = android.R.string.ok, onClick = {})
            androidx.compose.foundation.layout.Spacer(
                Modifier.padding(Dimens.spacingSm)
            )
            TravelPrimaryButton(textRes = android.R.string.ok, onClick = {}, enabled = false)
            androidx.compose.foundation.layout.Spacer(
                Modifier.padding(Dimens.spacingSm)
            )
            TravelSecondaryButton(textRes = android.R.string.cancel, onClick = {})
            androidx.compose.foundation.layout.Spacer(
                Modifier.padding(Dimens.spacingSm)
            )
            TravelTextButton(textRes = android.R.string.ok, onClick = {})
        }
    }
}
