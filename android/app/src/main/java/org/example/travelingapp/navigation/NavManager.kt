package org.example.travelingapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.example.travelingapp.core.datastore.StoreBoarding
import org.example.travelingapp.ui.SplashScreen
import org.example.travelingapp.ui.views.auth.LoginView
import org.example.travelingapp.ui.views.auth.RegisterView
import org.example.travelingapp.ui.views.home.HomeView
import org.example.travelingapp.ui.views.onboarding.MainOnBoarding
import org.example.travelingapp.ui.views.rentcar.RentCarView

@Composable
fun NavManager(modifier: Modifier) {
    val context = LocalContext.current
    val dataStore = StoreBoarding(context)
    val store = dataStore.getBoarding.collectAsState(initial = false)
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = if (store.value) Routes.LOGIN else Routes.SPLASH,
        modifier = modifier
    ) {
        composable(Routes.SPLASH) {
            SplashScreen(navController, store.value)
        }
        composable(Routes.ON_BOARDING) {
            MainOnBoarding(
                store = dataStore,
                onNavigateToLogin = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.ON_BOARDING) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.LOGIN) {
            LoginView(
                onNavigateToHome = { navController.navigate(Routes.HOME) },
                onNavigateToRegister = { navController.navigate(Routes.REGISTER) }
            )
        }
        composable(Routes.REGISTER) {
            RegisterView(
                navController = navController,
                onNavigateToLogin = { navController.navigate(Routes.LOGIN) }
            )
        }
        composable(Routes.HOME) {
            HomeView(
                navController = navController,
                onNavigateToRentCar = { navController.navigate(Routes.RENT_CAR) }
            )
        }
        composable(Routes.RENT_CAR) {
            RentCarView(navController)
        }
    }
}
