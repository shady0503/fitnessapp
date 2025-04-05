package com.example.Utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseToken
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.auth.oauth2.GoogleCredentials
import java.io.FileInputStream


fun initFirebase() {
    //update it 3la 7ssabek akhay saad
    val serviceAccount = FileInputStream("C:\\Users\\Youssef\\fitnessapp\\app\\src\\main\\java\\com\\example\\fitnessapp\\DevMobileBackend\\ktor-sample\\src\\main\\resources\\fitnessapp-12bd7-firebase-adminsdk-fbsvc-1b97af51d0.json")
    val options = FirebaseOptions.Builder()
        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
        .build()
    FirebaseApp.initializeApp(options)
}

fun verifyToken(token: String): FirebaseToken? {
    return try {
        val firebaseToken = FirebaseAuth.getInstance().verifyIdToken(token)
        firebaseToken
    } catch (e: Exception) {
        null
    }
}