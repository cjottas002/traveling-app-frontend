package org.example.travelingapp.ui.views.onboarding

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import org.example.travelingapp.core.datastore.StoreBoarding
import org.example.travelingapp.domain.entities.PageData

@Composable
fun OnBoardingPager(
    pages: List<PageData>,
    pagerState: PagerState,
    store: StoreBoarding,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()

    HorizontalPager(
        state = pagerState,
        modifier = modifier.fillMaxSize()
    ) { index ->
        OnBoardingPage(
            pageData = pages[index],
            isFirstPage = (index == 0),
            isLastPage = (index == pages.lastIndex),
            currentPage = pagerState.currentPage,
            pageCount = pages.size,
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
}
