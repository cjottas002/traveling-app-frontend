package org.example.travelingapp.ui.views.home

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiTransportation
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.example.travelingapp.feature.home.R

@Composable
fun HomeView(navController: NavController, onNavigateToRentCar: () -> Unit) {
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

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = org.example.travelingapp.ui.theme.Dimens.elevationSm
            ) {
                tabs.forEachIndexed { index, (icon, labelRes) ->
                    NavigationBarItem(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            scope.launch { pagerState.animateScrollToPage(index) }
                        },
                        icon = {
                            Icon(
                                imageVector = icon,
                                contentDescription = stringResource(labelRes)
                            )
                        },
                        label = {
                            Text(
                                text = stringResource(labelRes),
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .padding(innerPadding)
                .statusBarsPadding()
        ) { page ->
            when (page) {
                0 -> HomeTab()
                1 -> TransportTab()
                2 -> HotelTab()
                3 -> ThreeTab()
            }
        }
    }
}
