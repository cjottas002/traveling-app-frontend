package org.example.travelingapp.ui.views.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.example.travelingapp.domain.entities.PageData
import org.example.travelingapp.ui.theme.Dimens
import org.example.travelingapp.ui.views.components.TravelPrimaryButton
import org.example.travelingapp.ui.views.components.TravelSecondaryButton
import org.example.travelingapp.ui.views.components.AppImage
import org.example.travelingapp.ui.views.components.AppText
import org.example.travelingapp.ui.views.components.VerticalSpacer
import org.example.travelingapp.core.ui.R

@Composable
fun OnBoardingPage(
    pageData: PageData,
    isFirstPage: Boolean = false,
    isLastPage: Boolean = false,
    onNextClicked: () -> Unit = {},
    onSkipClicked: () -> Unit = {},
    onLoginClicked: () -> Unit = {},
) {
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        if (isFirstPage) {
            FirstOnboarding(pageData, onNextClicked)
        } else {
            SubsequentOnboardingPage(pageData, isLastPage, onSkipClicked, onNextClicked, onLoginClicked)
        }
    }
}

@Composable
private fun FirstOnboarding(pageData: PageData, onNextClicked: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(4.4f / 5f),
        shape = RoundedCornerShape(bottomStart = Dimens.radiusXl, bottomEnd = Dimens.radiusXl),
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .padding(
                    start = Dimens.spacingMd,
                    end = Dimens.spacingMd,
                    bottom = Dimens.spacingMd
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            VerticalSpacer(Dimens.spacingMd)
            TitleAndDescription(pageData, centered = true)
            VerticalSpacer(Dimens.spacingLg)
            AppImage(
                resId = pageData.image,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.9f)
            )
        }
    }

    TravelPrimaryButton(
        textRes = R.string.next,
        onClick = onNextClicked,
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.spacingMd)
    )
}

@Composable
private fun SubsequentOnboardingPage(
    pageData: PageData,
    isLastPage: Boolean,
    onSkipClicked: () -> Unit,
    onNextClicked: () -> Unit,
    onLoginClicked: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                if (isLastPage)
                    MaterialTheme.colorScheme.tertiaryContainer
                else
                    MaterialTheme.colorScheme.secondaryContainer
            )
            .statusBarsPadding()
            .padding(horizontal = Dimens.spacingMd, vertical = Dimens.spacingMd),
        horizontalAlignment = Alignment.Start
    ) {
        VerticalSpacer(Dimens.spacingLg)
        TitleAndDescription(pageData, centered = false)
        VerticalSpacer(Dimens.spacingLg)
        AppImage(
            resId = pageData.image,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f),
            contentDescription = null,
        )

        VerticalSpacer(Dimens.spacingLg)

        if (!isLastPage) {
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
                onClick = onLoginClicked
            )
        }
    }
}

@Composable
private fun TitleAndDescription(pageData: PageData, centered: Boolean) {
    AppText(
        text = pageData.title,
        style = MaterialTheme.typography.headlineSmall,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.fillMaxWidth()
    )
    VerticalSpacer(Dimens.spacingSm)
    AppText(
        text = pageData.desc,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.fillMaxWidth()
    )
}
