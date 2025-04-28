package org.example.travelingapp.ui.views.home

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.travelingapp.feature.home.R
import org.example.travelingapp.ui.theme.Dimens
import org.example.travelingapp.ui.theme.LightGray
import org.example.travelingapp.ui.theme.White
import org.example.travelingapp.ui.views.components.AppImage
import org.example.travelingapp.ui.views.components.AppText
import org.example.travelingapp.ui.views.components.VerticalSpacer

@Composable
fun HomeTab() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Dimens.medium)
    ) {

        AppText(
            textRes = R.string.upcoming_meetups,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = Dimens.extraSmall)
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
            .padding(bottom = Dimens.medium),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(Dimens.small),
        colors = CardDefaults.cardColors(
            containerColor = White
        )
    ) {
        Column(
            modifier = Modifier
                .padding(Dimens.medium)
        ) {

            AppImage(
                resId = imageRes,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )

            VerticalSpacer(Dimens.small)
            AppText(
                modifier = Modifier.padding(horizontal = Dimens.small),
                fontSize = 16.sp,
                text = subtitle,
                color = LightGray

            )
            VerticalSpacer(Dimens.extraSmall)
            AppText(
                modifier = Modifier.padding(horizontal = Dimens.small),
                text = title,
                fontSize = 20.sp,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            VerticalSpacer(8.dp)
        }
    }
}