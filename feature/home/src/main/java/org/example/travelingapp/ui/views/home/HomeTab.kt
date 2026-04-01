package org.example.travelingapp.ui.views.home

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import org.example.travelingapp.feature.home.R
import org.example.travelingapp.ui.theme.Dimens
import org.example.travelingapp.ui.views.components.AppImage
import org.example.travelingapp.ui.views.components.AppText
import org.example.travelingapp.ui.views.components.VerticalSpacer

@Composable
fun HomeTab() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Dimens.spacingMd)
    ) {
        AppText(
            textRes = R.string.upcoming_meetups,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = Dimens.spacingXs)
        )

        MeetingCard(
            imageRes = R.drawable.home_image_1,
            subtitle = stringResource(R.string.usa_los_angeles),
            title = stringResource(R.string.city_of_los_angeles)
        )

        MeetingCard(
            imageRes = R.drawable.home_image_2,
            subtitle = stringResource(R.string.maldives_3_weeks),
            title = stringResource(R.string.beach_vacation)
        )
    }
}

@Composable
fun MeetingCard(
    @DrawableRes imageRes: Int,
    subtitle: String,
    title: String,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = Dimens.spacingMd),
        shape = RoundedCornerShape(Dimens.radiusLg),
        elevation = CardDefaults.cardElevation(Dimens.elevationMd),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(Dimens.spacingMd)) {
            AppImage(
                resId = imageRes,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens.cardImageHeight)
                    .clip(RoundedCornerShape(Dimens.radiusMd)),
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )

            VerticalSpacer(Dimens.spacingSm)

            AppText(
                modifier = Modifier.padding(horizontal = Dimens.spacingSm),
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            VerticalSpacer(Dimens.spacingXs)

            AppText(
                modifier = Modifier.padding(horizontal = Dimens.spacingSm),
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            VerticalSpacer(Dimens.spacingSm)
        }
    }
}
