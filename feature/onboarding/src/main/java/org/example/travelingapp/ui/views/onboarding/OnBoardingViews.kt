package org.example.travelingapp.ui.views.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.example.travelingapp.core.ui.R
import org.example.travelingapp.domain.entities.PageData
import org.example.travelingapp.feature.onboarding.R as OnboardingR
import org.example.travelingapp.ui.theme.Alpha
import org.example.travelingapp.ui.theme.Dimens
import org.example.travelingapp.ui.theme.TravelGradients
import org.example.travelingapp.ui.theme.TravelingAppTheme
import org.example.travelingapp.ui.views.components.ButtonTone
import org.example.travelingapp.ui.views.components.TravelPagerIndicator
import org.example.travelingapp.ui.views.components.TravelPrimaryButton
import org.example.travelingapp.ui.views.components.TravelSecondaryButton
import org.example.travelingapp.ui.views.components.TravelVerticalSpacer

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
                .background(TravelGradients.heroOverlay)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = Dimens.screenPadding)
                .padding(bottom = Dimens.screenBottomPadding)
        ) {
            HeroTag(
                text = pageData.tag,
                modifier = Modifier.padding(top = Dimens.spacingMd)
            )

            Spacer(Modifier.weight(1f))

            StepIndicator(
                index = currentPage,
                total = pageCount,
                label = pageData.step
            )

            TravelVerticalSpacer(Dimens.spacingMd)

            Text(
                text = headlineWithAccent(pageData.title, pageData.titleAccent),
                style = MaterialTheme.typography.displaySmall.copy(
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            )

            TravelVerticalSpacer(Dimens.spacingMd)

            Text(
                text = pageData.desc,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = Alpha.muted)
            )

            TravelVerticalSpacer(Dimens.sectionSpacing)

            Actions(
                isFirstPage = isFirstPage,
                isLastPage = isLastPage,
                onNextClicked = onNextClicked,
                onSkipClicked = onSkipClicked,
                onLoginClicked = onLoginClicked
            )

            TravelVerticalSpacer(Dimens.cardSpacing)

            TravelPagerIndicator(
                size = pageCount,
                currentPage = currentPage,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

/** Top-aligned location chip: short ember rule + small mono uppercase text. */
@Composable
private fun HeroTag(text: String, modifier: Modifier = Modifier) {
    val emberColor = MaterialTheme.colorScheme.secondary
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        Box(
            modifier = Modifier
                .width(16.dp)
                .height(1.dp)
                .background(emberColor)
        )
        Spacer(Modifier.width(Dimens.spacingSm))
        Text(
            text = text.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = Color.White.copy(alpha = Alpha.muted)
        )
    }
}

/** Step indicator: "01 / 03 · DISCOVER" preceded by a short ember rule. */
@Composable
private fun StepIndicator(index: Int, total: Int, label: String) {
    val emberColor = MaterialTheme.colorScheme.secondary
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .width(16.dp)
                .height(1.dp)
                .background(emberColor)
        )
        Spacer(Modifier.width(Dimens.spacingSm))
        Text(
            text = "%02d / %02d · %s".format(index + 1, total, label.uppercase()),
            style = MaterialTheme.typography.labelMedium,
            color = emberColor
        )
    }
}

/** Buttons row, varies per slide. */
@Composable
private fun Actions(
    isFirstPage: Boolean,
    isLastPage: Boolean,
    onNextClicked: () -> Unit,
    onSkipClicked: () -> Unit,
    onLoginClicked: () -> Unit
) {
    when {
        isFirstPage -> {
            TravelPrimaryButton(
                textRes = OnboardingR.string.onboarding_get_started,
                onClick = onNextClicked,
                tone = ButtonTone.Ember
            )
        }
        isLastPage -> {
            TravelPrimaryButton(
                textRes = R.string.login,
                onClick = onLoginClicked,
                tone = ButtonTone.Ember
            )
        }
        else -> {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimens.spacingSm)
            ) {
                TravelSecondaryButton(
                    textRes = R.string.skip,
                    onClick = onSkipClicked,
                    onDark = true,
                    modifier = Modifier.weight(1f)
                )
                TravelPrimaryButton(
                    textRes = R.string.next,
                    onClick = onNextClicked,
                    tone = ButtonTone.Ember,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

/** Build the headline as "title" + optional italic ember accent on a new line. */
private fun headlineWithAccent(title: String, accent: String?): AnnotatedString =
    buildAnnotatedString {
        append(title)
        if (!accent.isNullOrBlank()) {
            append('\n')
            withStyle(
                SpanStyle(
                    color = Color(0xFFF26B4E), // ember — kept literal because we're on a forced-dark hero
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Normal
                )
            ) { append(accent) }
        }
    }

@Preview(showBackground = true, name = "Onboarding · First")
@Composable
private fun OnBoardingPageFirstPreview() {
    TravelingAppTheme {
        OnBoardingPage(
            pageData = PageData(
                image = OnboardingR.drawable.onboarding1_image,
                tag = "Sáhara · Erg Chebbi",
                step = "Descubre",
                title = "Descubre lo que",
                titleAccent = "aún no buscas.",
                desc = "Una curaduría de viajes pequeños, lentos y bien escritos."
            ),
            isFirstPage = true,
            currentPage = 0,
            pageCount = 3
        )
    }
}

@Preview(showBackground = true, name = "Onboarding · Middle")
@Composable
private fun OnBoardingPageMiddlePreview() {
    TravelingAppTheme {
        OnBoardingPage(
            pageData = PageData(
                image = OnboardingR.drawable.onboarding2_image,
                tag = "Atlas · Marruecos",
                step = "Reserva",
                title = "Reserva con",
                titleAccent = "sentido.",
                desc = "Hoteles, transportes y rutas curados por viajeros con criterio."
            ),
            currentPage = 1,
            pageCount = 3
        )
    }
}

@Preview(showBackground = true, name = "Onboarding · Last")
@Composable
private fun OnBoardingPageLastPreview() {
    TravelingAppTheme {
        OnBoardingPage(
            pageData = PageData(
                image = OnboardingR.drawable.onboarding3_image,
                tag = "Chefchaouen · Rif",
                step = "Empieza",
                title = "Empieza\ncuando",
                titleAccent = "quieras.",
                desc = "Tu primera reserva está a un toque."
            ),
            isLastPage = true,
            currentPage = 2,
            pageCount = 3
        )
    }
}
