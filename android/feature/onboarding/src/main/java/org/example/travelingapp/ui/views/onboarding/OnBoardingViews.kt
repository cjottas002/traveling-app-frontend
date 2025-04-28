package org.example.travelingapp.ui.views.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.travelingapp.domain.entities.PageData
import org.example.travelingapp.ui.theme.Dimens
import org.example.travelingapp.ui.theme.LilaFondo
import org.example.travelingapp.ui.theme.MainOnboardingTextColor
import org.example.travelingapp.ui.views.components.LoginButton
import org.example.travelingapp.ui.views.components.NextButton
import org.example.travelingapp.ui.views.components.SkipButton
import org.example.travelingapp.ui.views.components.AppImage
import org.example.travelingapp.ui.views.components.AppText
import org.example.travelingapp.ui.views.components.VerticalSpacer

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
        modifier = Modifier.fillMaxWidth().fillMaxHeight(4.4f / 5f),
        shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp),
        color = LilaFondo
    ) {
        Column(
            modifier = Modifier.padding(start = 10.dp, end = 10.dp, bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            VerticalSpacer(Dimens.dp20)
            TitleAndDescription(pageData, centered = true)
            VerticalSpacer(Dimens.dp20)
            AppImage(
                resId = pageData.image,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.9f)
            )
        }
    }

    NextButton(modifier = Modifier.fillMaxSize(), onClick = onNextClicked)
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
                when {
                    isLastPage -> MaterialTheme.colorScheme.tertiaryContainer
                    else -> MaterialTheme.colorScheme.secondaryContainer
                }
            )
            .padding(start = 10.dp, end = 10.dp, bottom = 16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        VerticalSpacer(Dimens.dp20)
        TitleAndDescription(pageData, centered = false)
        VerticalSpacer(Dimens.dp20)
        AppImage(
            resId =  pageData.image,
            modifier = Modifier.fillMaxWidth().fillMaxHeight(0.8f),
            contentDescription = null,
        )

        VerticalSpacer(Dimens.dp20)

        if (!isLastPage) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SkipButton(modifier = Modifier.weight(1f), onClick = onSkipClicked)
                NextButton(modifier = Modifier.weight(1f), onClick = onNextClicked)
            }
        } else {
            LoginButton(modifier = Modifier.fillMaxWidth(), onClick = onLoginClicked)
        }
    }
}


@Composable
private fun TitleAndDescription(pageData: PageData, centered: Boolean) {
    AppText(
        text = pageData.title,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black,
        textAlign = if (centered) TextAlign.Center else TextAlign.Start,
        modifier = Modifier.fillMaxWidth()
    )
    VerticalSpacer(10.dp)
    AppText(
        text = pageData.desc,
        fontSize = 16.sp,
        color = MainOnboardingTextColor,
        textAlign = if (centered) TextAlign.Center else TextAlign.Start,
        modifier = Modifier.fillMaxWidth()
    )
}


