package org.example.travelingapp.ui.views.onboarding

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.example.travelingapp.core.datastore.StoreBoarding
import org.example.travelingapp.domain.entities.PageData
import org.example.travelingapp.ui.views.components.PagerIndicator

@Composable
fun OnBoardingPager(
    pages: List<PageData>,
    pagerState: PagerState,
    store: StoreBoarding,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()

    Box(modifier = modifier.fillMaxSize()) {

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { index ->
            OnBoardingPage(
                pageData = pages[index],
                isFirstPage = (index == 0),
                isLastPage = (index == pages.lastIndex),
                onNextClicked = {
                    scope.launch {
                        pagerState.animateScrollToPage(index + 1)
                    }
                },
                onSkipClicked = {
                    scope.launch {
                        store.saveBoarding(true)
                        onNavigateToLogin()
                    }
                },
                onLoginClicked = {
                    scope.launch {
                        store.saveBoarding(true)
                        onNavigateToLogin()
                    }
                }
            )
        }

        PagerIndicator(
            size = pages.size,
            currentPage = pagerState.currentPage,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp)
        )
    }
}
