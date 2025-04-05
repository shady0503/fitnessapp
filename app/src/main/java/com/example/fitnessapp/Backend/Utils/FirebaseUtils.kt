package com.example.fitnessapp.Backend.Utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseToken
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.auth.oauth2.GoogleCredentials
import java.io.FileInputStream

// Firebase initialization
fun initFirebase() {
    val serviceAccount = FileInputStream("src/main/resources/google-services.json")
    val options = FirebaseOptions.Builder()
        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
        .build()
    FirebaseApp.initializeApp(options)
}

// Firebase ID token verification
fun verifyToken(token: String): FirebaseToken? {
    return try {
        val firebaseToken = FirebaseAuth.getInstance().verifyIdToken(token)
        firebaseToken  // Return the Firebase token if verification is successful
    } catch (e: Exception) {
        null // Return null if token verification fails
    }
}

