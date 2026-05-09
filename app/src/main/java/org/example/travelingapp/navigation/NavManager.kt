package org.example.travelingapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.example.travelingapp.core.datastore.StoreBoarding
import org.example.travelingapp.core.datastore.TokenManager
import org.example.travelingapp.ui.SplashScreen
import org.example.travelingapp.ui.views.auth.ForgotPasswordView
import org.example.travelingapp.ui.views.auth.LoginView
import org.example.travelingapp.ui.views.auth.RegisterView
import org.example.travelingapp.ui.views.home.HomeView
import org.example.travelingapp.ui.views.home.destinations.CreateDestinationView
import org.example.travelingapp.ui.views.home.destinations.DestinationDetailView
import org.example.travelingapp.ui.views.onboarding.MainOnBoarding
import org.example.travelingapp.ui.views.rentcar.RentCarView

@Composable
fun NavManager(modifier: Modifier) {
    val context = LocalContext.current
    val dataStore = StoreBoarding(context)
    val store = dataStore.getBoarding.collectAsState(initial = false)
    val navController = rememberNavController()

    val tokenManager = TokenManager(context)
    val role = produceState<String?>(initialValue = null) {
        tokenManager.roleFlow.collect { value = it }
    }
    val isAdmin = role.value?.equals("Admin", ignoreCase = true) == true

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
                onNavigateToRegister = { navController.navigate(Route.Register) },
                onNavigateToForgotPassword = { navController.navigate(Route.ForgotPassword) }
            )
        }
        composable<Route.Register> {
            RegisterView(
                navController = navController,
                onNavigateToLogin = { navController.navigate(Route.Login) }
            )
        }
        composable<Route.ForgotPassword> {
            ForgotPasswordView(
                onBackClicked = { navController.popBackStack() }
            )
        }
        composable<Route.Home> {
            HomeView(
                onNavigateToRentCar = { navController.navigate(Route.RentCar) },
                onNavigateToCreateDestination = { navController.navigate(Route.CreateDestination) },
                onNavigateToDestinationDetail = { id -> navController.navigate(Route.DestinationDetail(id)) },
                isAdmin = isAdmin
            )
        }
        composable<Route.RentCar> {
            RentCarView(navController)
        }
        composable<Route.CreateDestination> {
            CreateDestinationView(navController = navController)
        }
        composable<Route.DestinationDetail> { backStackEntry ->
            val route = backStackEntry.toRoute<Route.DestinationDetail>()
            DestinationDetailView(
                destinationId = route.destinationId,
                navController = navController,
                isAdmin = isAdmin
            )
        }
    }
}
