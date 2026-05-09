package org.example.travelingapp.ui.views.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiTransportation
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.example.travelingapp.feature.home.R
import org.example.travelingapp.ui.theme.TravelingAppTheme
import org.example.travelingapp.ui.views.components.TravelBottomBar
import org.example.travelingapp.ui.views.components.TravelBottomBarItem
import org.example.travelingapp.ui.views.components.TravelEmptyState
import org.example.travelingapp.ui.views.components.TravelScaffold

@Composable
fun HomeView(
    navController: NavController,
    onNavigateToRentCar: () -> Unit,
    onNavigateToCreateDestination: () -> Unit = {},
    onNavigateToDestinationDetail: (String) -> Unit = {},
    isAdmin: Boolean = false
) {
    HomeViewScaffold { page ->
        when (page) {
            0 -> HomeTab(
                isAdmin = isAdmin,
                onNavigateToRentCar = onNavigateToRentCar,
                onCreateDestination = onNavigateToCreateDestination,
                onDestinationClick = onNavigateToDestinationDetail
            )
            1 -> TransportTab()
            2 -> HotelTab()
            3 -> ThreeTab()
        }
    }
}

@Composable
private fun HomeViewScaffold(
    contentForPage: @Composable (Int) -> Unit
) {
    val scope = rememberCoroutineScope()

    val tabs = listOf(
        Icons.Filled.Explore to R.string.tab_explore,
        Icons.Filled.EmojiTransportation to R.string.tab_transport,
        Icons.Filled.Hotel to R.string.tab_hotels,
        Icons.Filled.Person to R.string.tab_profile,
    )

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { tabs.size }
    )

    TravelScaffold(
        bottomBar = {
            TravelBottomBar {
                tabs.forEachIndexed { index, (icon, labelRes) ->
                    TravelBottomBarItem(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            scope.launch { pagerState.animateScrollToPage(index) }
                        },
                        icon = icon,
                        labelRes = labelRes
                    )
                }
            }
        }
    ) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .padding(innerPadding)
        ) { page -> contentForPage(page) }
    }
}

@Preview(showBackground = true, name = "Home shell - bottom menu")
@Composable
private fun HomeViewScaffoldPreview() {
    TravelingAppTheme {
        HomeViewScaffold { page ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                when (page) {
                    0 -> TravelEmptyState(
                        glyph = "01",
                        title = "Explore",
                        accent = "preview."
                    )
                    1 -> TravelEmptyState(
                        glyph = "02",
                        title = "Move",
                        accent = "preview."
                    )
                    2 -> TravelEmptyState(
                        glyph = "03",
                        title = "Hotels",
                        accent = "preview."
                    )
                    else -> TravelEmptyState(
                        glyph = "04",
                        title = "Me",
                        accent = "preview."
                    )
                }
            }
        }
    }
}
