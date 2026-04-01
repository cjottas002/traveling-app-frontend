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
        startDestination = if (store.value) Route.Login else Route.Splash,
        modifier = modifier
    ) {
        composable<Route.Splash> {
            SplashScreen(navController, store.value)
        }
        composable<Route.OnBoarding> {
            MainOnBoarding(
                store = dataStore,
                onNavigateToLogin = {
                    navController.navigate(Route.Login) {
                        popUpTo<Route.OnBoarding> { inclusive = true }
                    }
                }
            )
        }
        composable<Route.Login> {
            LoginView(
                onNavigateToHome = { navController.navigate(Route.Home) },
                onNavigateToRegister = { navController.navigate(Route.Register) }
            )
        }
        composable<Route.Register> {
            RegisterView(
                navController = navController,
                onNavigateToLogin = { navController.navigate(Route.Login) }
            )
        }
        composable<Route.Home> {
            HomeView(
                navController = navController,
                onNavigateToRentCar = { navController.navigate(Route.RentCar) }
            )
        }
        composable<Route.RentCar> {
            RentCarView(navController)
        }
    }
}
