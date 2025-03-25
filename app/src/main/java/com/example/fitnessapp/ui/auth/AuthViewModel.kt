package com.example.fitnessapp.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
                state = state.copy(isAuthenticated = true)
                onAuthSuccess()
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
                state = state.copy(isAuthenticated = true)
                onAuthSuccess()
            } catch (e: Exception) {
                state = state.copy(error = e.message ?: "Unknown error occurred")
            }
            state = state.copy(isLoading = false)
        }
    }

}
