package com.example.fitnessapp.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnessapp.ui.Network.ApiClient
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class AuthState(
    val email: String = "",
    val password: String = "",
    val isLoginMode: Boolean = true,
    val isLoading: Boolean = false,
    val error: String = ""
    , val isAuthenticated: Boolean = false
)

class AuthViewModel : ViewModel() {
    var state by mutableStateOf(AuthState())
        private set
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    fun onEmailChange(newEmail: String) {
        state = state.copy(email = newEmail)
    }

    fun onPasswordChange(newPassword: String) {
        state = state.copy(password = newPassword)
    }

    fun toggleMode() {
        state = state.copy(isLoginMode = !state.isLoginMode, error = "")
    }

    fun login(onAuthSuccess: () -> Unit = {}) {
        state = state.copy(isLoading = true, error = "")
        viewModelScope.launch {
            try {
                firebaseAuth.signInWithEmailAndPassword(
                    state.email.trim(),
                    state.password
                ).await()
                // On successful login, update state and trigger navigation callback.
                // and sending the token to the backend
                val idToken = firebaseAuth.currentUser?.getIdToken(true)?.await()?.token
                if (idToken != null) {
                    // Send the ID token to the backend for syncing
                    syncUserToBackend(idToken)
                    state = state.copy(isAuthenticated = true)
                    onAuthSuccess()
                }
            } catch (e: Exception) {
                state = state.copy(error = e.message ?: "Unknown error occurred")
            }
            state = state.copy(isLoading = false)
        }
    }

    fun register(onAuthSuccess: () -> Unit = {}) {
        state = state.copy(isLoading = true, error = "")
        viewModelScope.launch {
            try {
                firebaseAuth.createUserWithEmailAndPassword(
                    state.email.trim(),
                    state.password
                ).await()
                // On successful registration, update state and trigger navigation callback.
                // Get the ID token after registration
                val idToken = firebaseAuth.currentUser?.getIdToken(true)?.await()?.token
                if (idToken != null) {
                    // Send the ID token to the backend for syncing
                    syncUserToBackend(idToken)
                    state = state.copy(isAuthenticated = true)
                    onAuthSuccess()
                }
            } catch (e: Exception) {
                state = state.copy(error = e.message ?: "Unknown error occurred")
            }
            state = state.copy(isLoading = false)
        }
    }

    private suspend fun syncUserToBackend(idToken: String) {
        // Retrofit/HTTP call to your backend API
        try {
            val response = ApiClient.authService.syncUser(idToken)
            if (response.isSuccessful) {
                // Handle success (syncing complete)
            } else {
                // Handle failure (e.g., user not created in the backend)
            }
        } catch (e: Exception) {
            // Handle network errors
        }
    }

}