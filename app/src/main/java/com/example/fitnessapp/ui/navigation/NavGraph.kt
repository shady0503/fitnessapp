package com.example.fitnessapp.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.fitnessapp.auth.LoginScreen
import com.example.fitnessapp.auth.SignupScreen
import com.example.fitnessapp.ui.exerciselibrary.ExerciseDetailScreen
import com.example.fitnessapp.ui.exerciselibrary.ExerciseLibraryScreen
import com.example.fitnessapp.ui.exerciselibrary.ExerciseLibraryViewModel
import com.example.fitnessapp.ui.welcome.UserWelcomeScreen
import com.example.fitnessapp.ui.welcome.WelcomeScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    // Create a shared ExerciseLibraryViewModel to maintain state between screens
    val exerciseLibraryViewModel: ExerciseLibraryViewModel = viewModel()

    NavHost(navController = navController, startDestination = "welcome") {
        composable("welcome") {
            WelcomeScreen(
                onLoginClick = { navController.navigate("login") },
                onSignUpClick = { navController.navigate("signup") }
            )
        }

        composable("login") {
            LoginScreen(
                initialMode = "login",
                onAuthSuccess = { username ->
                    Log.d("NavGraph", "Login successful, navigating to exercise_library")
                    navController.navigate("exercise_library") {
                        popUpTo("welcome") { inclusive = true }
                    }
                },
                onSignUpClick = { navController.navigate("signup") }
            )
        }

        composable("signup") {
            SignupScreen(
                onSignupComplete = { username ->
                    Log.d("NavGraph", "Signup complete, navigating to exercise_library")
                    // Navigate to exercise library instead of home
                    navController.navigate("exercise_library") {
                        // Clear all destinations up to welcome screen
                        popUpTo("welcome") { inclusive = true }
                    }
                    Log.d("NavGraph", "Navigation command executed from signup to exercise_library")
                },
                onBackToLogin = {
                    Log.d("NavGraph", "Navigating back to login")
                    navController.navigate("login")
                }
            )
        }

        // The old home screen - keeping this for reference
        composable("home/{username}") { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: "User"
            Log.d("NavGraph", "Rendering home screen for user: $username")
            UserWelcomeScreen(username = username)
        }

        // Exercise Library screen
        composable("exercise_library") {
            ExerciseLibraryScreen(
                onExerciseSelected = { exercise ->
                    // Navigate to exercise detail screen
                    navController.navigate("exercise_detail/${exercise.id}")
                },
                onNavigateBack = {
                    // If needed, could navigate back to a user home screen
                    // For now, no action as we're using this as the main screen
                }
            )
        }

        // Exercise Detail screen
        composable(
            route = "exercise_detail/{exerciseId}",
            arguments = listOf(
                navArgument("exerciseId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val exerciseId = backStackEntry.arguments?.getString("exerciseId") ?: ""
            // Find the exercise by ID from the ViewModel
            val exercise = remember(exerciseId) {
                exerciseLibraryViewModel.exercises.find { it.id == exerciseId }
            }

            exercise?.let {
                ExerciseDetailScreen(
                    exercise = it,
                    onBackPressed = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}