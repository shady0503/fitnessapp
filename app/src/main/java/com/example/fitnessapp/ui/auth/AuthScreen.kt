package com.example.fitnessapp.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AuthScreen(
    initialMode: String = "login",
    authViewModel: AuthViewModel = viewModel(),
    onAuthSuccess: (username: String) -> Unit
) {
    // Decide if this screen is for login or registration
    LaunchedEffect(initialMode) {
        if (initialMode == "signup" && authViewModel.state.isLoginMode) {
            authViewModel.toggleMode() // switch to registration
        }
        if (initialMode == "login" && !authViewModel.state.isLoginMode) {
            authViewModel.toggleMode() // switch to login
        }
    }

    val state = authViewModel.state
    val isLoginMode = state.isLoginMode

// Observe authentication success and navigate.
    LaunchedEffect(state.isAuthenticated) {
        if (state.isAuthenticated) {
            // Here, we derive a username. In this simple example, we take the part of the email before '@'.
            val username = state.email.substringBefore("@")
            onAuthSuccess(username)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (isLoginMode) "Login" else "Register",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.email,
            onValueChange = { authViewModel.onEmailChange(it) },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = state.password,
            onValueChange = { authViewModel.onPasswordChange(it) },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (isLoginMode) authViewModel.login { /* Callback on success */ }
                else authViewModel.register { /* Callback on success */ }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = if (isLoginMode) "Login" else "Register")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = { authViewModel.toggleMode() }) {
            Text(
                text = if (isLoginMode) "Don't have an account? Register"
                else "Already have an account? Login"
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (state.error.isNotEmpty()) {
            Text(text = state.error, color = MaterialTheme.colorScheme.error)
        }

        if (state.isLoading) {
            CircularProgressIndicator()
        }
    }
}
