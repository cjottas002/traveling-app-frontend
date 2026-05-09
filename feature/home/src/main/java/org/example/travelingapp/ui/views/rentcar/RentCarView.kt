package org.example.travelingapp.ui.views.rentcar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import org.example.travelingapp.feature.home.R
import org.example.travelingapp.ui.theme.Dimens
import org.example.travelingapp.ui.theme.TravelingAppTheme
import org.example.travelingapp.ui.views.components.TravelEmptyState
import org.example.travelingapp.ui.views.components.TravelIconButton

@Composable
fun RentCarView(navController: NavController) {
    RentCarContent(onBackClicked = { navController.popBackStack() })
}

@Composable
private fun RentCarContent(onBackClicked: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.screenPadding, vertical = Dimens.spacingSm),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TravelIconButton(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                iconTint = MaterialTheme.colorScheme.onBackground,
                onClick = onBackClicked
            )
            Spacer(Modifier.width(Dimens.spacingSm))
            Text(
                text = stringResource(R.string.rent_a_car).uppercase(),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        TravelEmptyState(
            glyph = stringResource(R.string.empty_glyph),
            title = stringResource(R.string.rentcar_empty_title),
            accent = stringResource(R.string.rentcar_empty_accent),
            body = stringResource(R.string.rentcar_empty_body),
            badge = stringResource(R.string.empty_badge_soon)
        )
    }
}

@Preview(showBackground = true, name = "Rent car placeholder")
@Composable
private fun RentCarContentPreview() {
    TravelingAppTheme {
        RentCarContent(onBackClicked = {})
    }
}
