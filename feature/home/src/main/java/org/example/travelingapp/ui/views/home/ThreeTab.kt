package org.example.travelingapp.ui.views.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.example.travelingapp.feature.home.R
import org.example.travelingapp.ui.theme.TravelingAppTheme
import org.example.travelingapp.ui.views.components.TravelEmptyState

@Composable
fun ThreeTab() {
    TravelEmptyState(
        glyph = stringResource(R.string.empty_glyph),
        title = stringResource(R.string.profile_empty_title),
        accent = stringResource(R.string.profile_empty_accent),
        body = stringResource(R.string.profile_empty_body),
        badge = stringResource(R.string.empty_badge_soon)
    )
}

@Preview(showBackground = true, name = "Profile placeholder")
@Composable
private fun ThreeTabPreview() {
    TravelingAppTheme {
        ThreeTab()
    }
}
