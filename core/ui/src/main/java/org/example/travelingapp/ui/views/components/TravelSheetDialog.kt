package org.example.travelingapp.ui.views.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.example.travelingapp.ui.theme.Dimens
import org.example.travelingapp.ui.theme.TravelingAppTheme

/**
 * Meridian sheet dialog for richer confirmation/editing flows.
 *
 * `TravelDialog` remains available for tiny destructive confirmations; use
 * this sheet when the content needs a clearer header and bottom-safe layout.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TravelSheetDialog(
    onDismissRequest: () -> Unit,
    title: String,
    modifier: Modifier = Modifier,
    dismissText: String? = null,
    onDismiss: (() -> Unit)? = null,
    confirmText: String? = null,
    onConfirm: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = Dimens.screenPadding)
                .padding(bottom = Dimens.screenBottomPadding)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (dismissText != null && onDismiss != null) {
                    TravelTextButton(text = dismissText, onClick = onDismiss)
                }
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.weight(1f)
                )
                if (confirmText != null && onConfirm != null) {
                    TravelTextButton(text = confirmText, onClick = onConfirm)
                }
            }
            TravelVerticalSpacer(Dimens.spacingLg)
            content()
        }
    }
}

@Composable
fun TravelSheetDialog(
    onDismissRequest: () -> Unit,
    @StringRes titleRes: Int,
    modifier: Modifier = Modifier,
    @StringRes dismissTextRes: Int? = null,
    onDismiss: (() -> Unit)? = null,
    @StringRes confirmTextRes: Int? = null,
    onConfirm: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    TravelSheetDialog(
        onDismissRequest = onDismissRequest,
        title = stringResource(titleRes),
        modifier = modifier,
        dismissText = dismissTextRes?.let { stringResource(it) },
        onDismiss = onDismiss,
        confirmText = confirmTextRes?.let { stringResource(it) },
        onConfirm = onConfirm,
        content = content
    )
}

@Preview(showBackground = true, name = "Sheet dialog content")
@Composable
private fun TravelSheetDialogContentPreview() {
    TravelingAppTheme {
        Column(modifier = Modifier.padding(Dimens.screenPadding)) {
            Text(
                text = "TravelSheetDialog is rendered by ModalBottomSheet at runtime.",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
