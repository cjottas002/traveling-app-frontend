package org.example.travelingapp.ui.views.home

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.example.travelingapp.feature.home.R
import org.example.travelingapp.ui.views.components.AppIconButton
import org.example.travelingapp.ui.views.components.AppToolBar

@Composable
fun HomeView(navController: NavController, onNavigateToRentCar: () -> Unit) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

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
        topBar = {
            AppToolBar(showBack = true, navController = navController) {
                AppIconButton(
                    iconRes = R.drawable.common_ic_castle,
                    contentDescription = stringResource(R.string.eurodisney),
                    iconSize = org.example.travelingapp.ui.theme.Dimens.iconLg,
                    iconTint = MaterialTheme.colorScheme.onPrimary
                ) {
                    val intent = Intent(Intent.ACTION_VIEW, "https://www.disneylandparis.com/".toUri())
                    context.startActivity(intent)
                }

                AppIconButton(
                    iconRes = R.drawable.common_ic_car,
                    contentDescription = stringResource(R.string.rent_a_car),
                    iconSize = org.example.travelingapp.ui.theme.Dimens.iconLg,
                    iconTint = MaterialTheme.colorScheme.onPrimary
                ) {
                    onNavigateToRentCar()
                }
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
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
            modifier = Modifier.padding(innerPadding)
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
