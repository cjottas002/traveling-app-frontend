package org.example.travelingapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import org.example.travelingapp.R
import org.example.travelingapp.navigation.Route

@Composable
fun SplashScreen(navController: NavController, store: Boolean) {
    val destination: Route = if (store) Route.Login else Route.OnBoarding

    LaunchedEffect(Unit) {
        delay(2000)
        navController.navigate(destination)
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Image(
            painter = painterResource(id = R.drawable.splash_image),
            contentDescription = stringResource(R.string.app_name),
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}
