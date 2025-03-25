package com.example.fitnessapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.fitnessapp.navigation.AppNavHost
import com.example.fitnessapp.theme.FitnessAppTheme
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize Firebase (google-services.json must be in place)
        FirebaseApp.initializeApp(this)

        setContent {
            FitnessAppTheme(darkTheme = false) {
                val navController = rememberNavController()
                AppNavHost(navController)
            }
        }
    }
}
