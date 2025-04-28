package org.example.travelingapp.ui.views.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import org.example.travelingapp.feature.onboarding.R
import org.example.travelingapp.core.datastore.StoreBoarding
import org.example.travelingapp.domain.entities.PageData


@Composable
fun MainOnBoarding(store: StoreBoarding, onNavigateToLogin: () -> Unit) {

    val items = listOf(
        PageData(
            R.drawable.onboarding1_image,
            stringResource(R.string.onboarding1_title),
            stringResource(R.string.onboarding1_main_text)
        ),
        PageData(
            R.drawable.onboarding2_image,
            stringResource(R.string.onboarding2_title),
            stringResource(R.string.onboarding2_main_text)
        ),
        PageData(
            R.drawable.onboarding3_image,
            stringResource(R.string.onboarding3_title),
            stringResource(R.string.onboarding3_main_text)
        )
    )

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { items.size }
    )

    OnBoardingPager(
        pages = items,
        pagerState = pagerState,
        store = store,
        onNavigateToLogin = onNavigateToLogin,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    )
}
