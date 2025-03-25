package com.example.fitnessapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.fitnessapp.auth.AuthScreen
import com.example.fitnessapp.ui.welcome.WelcomeScreen
import com.example.fitnessapp.ui.welcome.UserWelcomeScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "welcome") {
        composable("welcome") {
            WelcomeScreen(
                onLoginClick = { navController.navigate("login") },
                onSignUpClick = { navController.navigate("signup") }
            )
        }
        composable("login") {
            AuthScreen(
                initialMode = "login",
                onAuthSuccess = { username ->
                    navController.navigate("home/$username") {
                        popUpTo("welcome") { inclusive = true }
                    }
                }
            )
        }
        composable("signup") {
            AuthScreen(
                initialMode = "signup",
                onAuthSuccess = { username ->
                    navController.navigate("home/$username") {
                        popUpTo("welcome") { inclusive = true }
                    }
                }
            )
        }
        composable("home/{username}") { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: "User"
            UserWelcomeScreen(username = username)
        }
    }
}