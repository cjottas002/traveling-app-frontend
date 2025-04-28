package org.example.travelingapp.ui.views.home

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiTransportation
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.SentimentSatisfied
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.example.travelingapp.feature.home.R
import org.example.travelingapp.ui.theme.ColorCamera
import org.example.travelingapp.ui.theme.ColorHeart
import org.example.travelingapp.ui.theme.ColorSmile
import org.example.travelingapp.ui.theme.ColorTent
import org.example.travelingapp.ui.theme.Dimens
import org.example.travelingapp.ui.theme.White
import org.example.travelingapp.ui.views.components.AppIconButton
import org.example.travelingapp.ui.views.components.AppToolBar

@Composable
fun HomeView(navController: NavController, onNavigateToRentCar: () -> Unit) {

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val tabIcons = listOf(
        Icons.Filled.PhotoCamera to ColorCamera,
        Icons.Filled.EmojiTransportation to ColorHeart,
        Icons.Filled.Hotel to ColorTent,
        Icons.Filled.SentimentSatisfied to ColorSmile,
    )
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { tabIcons.size }
    )

    Column {
        AppToolBar(showBack = true, navController = navController) {

            AppIconButton(
                iconRes = R.drawable.common_ic_castle,
                contentDescription = stringResource(R.string.eurodisney),
                iconSize = Dimens.large,
                iconTint = White
            ) {
                val intent = Intent(Intent.ACTION_VIEW, "https://www.disneylandparis.com/".toUri())
                context.startActivity(intent)
            }

            AppIconButton(
                iconRes = R.drawable.common_ic_car,
                contentDescription = stringResource(R.string.rent_a_car),
                iconSize = Dimens.large,
                iconTint = White
            ) {
                onNavigateToRentCar()
            }
        }
        PrimaryTabRow(
            selectedTabIndex = pagerState.currentPage,
            divider = {},
            containerColor = Color.Transparent,
        ) {
            tabIcons.forEachIndexed { index, (icon, bgColor) ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    selectedContentColor = MaterialTheme.colorScheme.onPrimary,
                    icon = {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(bgColor),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = if (pagerState.currentPage == index)
                                    MaterialTheme.colorScheme.onPrimary
                                else
                                    MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
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


