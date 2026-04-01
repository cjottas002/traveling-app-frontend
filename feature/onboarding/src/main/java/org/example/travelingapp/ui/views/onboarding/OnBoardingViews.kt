package org.example.travelingapp.ui.views.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import org.example.travelingapp.domain.entities.PageData
import org.example.travelingapp.ui.theme.Dimens
import org.example.travelingapp.ui.views.components.TravelPrimaryButton
import org.example.travelingapp.ui.views.components.TravelSecondaryButton
import org.example.travelingapp.ui.views.components.AppText
import org.example.travelingapp.ui.views.components.PagerIndicator
import org.example.travelingapp.ui.views.components.VerticalSpacer
import org.example.travelingapp.core.ui.R

@Composable
fun OnBoardingPage(
    pageData: PageData,
    isFirstPage: Boolean = false,
    isLastPage: Boolean = false,
    currentPage: Int = 0,
    pageCount: Int = 3,
    onNextClicked: () -> Unit = {},
    onSkipClicked: () -> Unit = {},
    onLoginClicked: () -> Unit = {},
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = pageData.image),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.15f),
                            Color.Black.copy(alpha = 0.7f)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = Dimens.spacingLg, vertical = Dimens.spacingMd),
            verticalArrangement = Arrangement.Bottom
        ) {
            AppText(
                text = pageData.title,
                style = androidx.compose.material3.MaterialTheme.typography.headlineMedium,
                color = Color.White,
                modifier = Modifier.fillMaxWidth()
            )
            VerticalSpacer(Dimens.spacingSm)
            AppText(
                text = pageData.desc,
                style = androidx.compose.material3.MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.85f),
                modifier = Modifier.fillMaxWidth()
            )

            VerticalSpacer(Dimens.spacingXl)

            if (isFirstPage) {
                TravelPrimaryButton(
                    textRes = R.string.next,
                    onClick = onNextClicked,
                    modifier = Modifier.fillMaxWidth()
                )
            } else if (!isLastPage) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Dimens.spacingSm)
                ) {
                    TravelSecondaryButton(
                        textRes = R.string.skip,
                        onClick = onSkipClicked,
                        modifier = Modifier.weight(1f)
                    )
                    TravelPrimaryButton(
                        textRes = R.string.next,
                        onClick = onNextClicked,
                        modifier = Modifier.weight(1f)
                    )
                }
            } else {
                TravelPrimaryButton(
                    textRes = R.string.login,
                    onClick = onLoginClicked,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            VerticalSpacer(Dimens.spacingMd)

            PagerIndicator(
                size = pageCount,
                currentPage = currentPage,
                activeColor = Color.White,
                inactiveColor = Color.White.copy(alpha = 0.4f),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            VerticalSpacer(Dimens.spacingSm)
        }
    }
}
