package com.example.fitnessapp.auth

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class SignupState(
    // Current step
    val currentStep: Int = 1,

    // Step 1: Account Information
    val email: String = "",
    val emailError: String = "",
    val password: String = "",
    val passwordError: String = "",
    val confirmPassword: String = "",
    val confirmPasswordError: String = "",

    // Step 2: Personal Details
    val name: String = "",
    val nameError: String = "",
    val age: String = "",
    val ageError: String = "",
    val height: String = "",
    val heightError: String = "",
    val weight: String = "",
    val weightError: String = "",

    // Step 3: Fitness Goals
    val fitnessLevel: String = "Beginner",
    val goals: Set<String> = setOf(),
    val weeklyWorkoutTarget: Int = 3,

    // General state
    val isLoading: Boolean = false,
    val error: String = "",
    val isAuthenticated: Boolean = false
) {
    // Validation flags for enabling/disabling next buttons
    val isAccountStepValid: Boolean get() =
        email.isNotEmpty() && emailError.isEmpty() &&
                password.isNotEmpty() && passwordError.isEmpty() &&
                confirmPassword.isNotEmpty() && confirmPasswordError.isEmpty()

    val isPersonalStepValid: Boolean get() =
        name.isNotEmpty() && nameError.isEmpty() &&
                age.isNotEmpty() && ageError.isEmpty() &&
                height.isNotEmpty() && heightError.isEmpty() &&
                weight.isNotEmpty() && weightError.isEmpty()

    val isFitnessStepValid: Boolean get() =
        fitnessLevel.isNotEmpty() && goals.isNotEmpty() && weeklyWorkoutTarget > 0
}

class SignupViewModel : ViewModel() {
    var state by mutableStateOf(SignupState())
        private set

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    // Navigation methods
    fun nextStep() {
        if (state.currentStep < 3) {
            state = state.copy(currentStep = state.currentStep + 1)
        }
    }

    fun previousStep() {
        if (state.currentStep > 1) {
            state = state.copy(currentStep = state.currentStep - 1)
        }
    }

    // Step 1: Account Information methods
    fun updateEmail(email: String) {
        state = state.copy(email = email)
        validateEmail()
    }

    fun updatePassword(password: String) {
        state = state.copy(password = password)
        validatePassword()
    }

    fun updateConfirmPassword(confirmPassword: String) {
        state = state.copy(confirmPassword = confirmPassword)
        validateConfirmPassword()
    }

    private fun validateEmail() {
        val emailRegex = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        state = if (state.email.isEmpty()) {
            state.copy(emailError = "Email is required")
        } else if (!state.email.matches(emailRegex.toRegex())) {
            state.copy(emailError = "Please enter a valid email address")
        } else {
            state.copy(emailError = "")
        }
    }

    private fun validatePassword() {
        state = if (state.password.isEmpty()) {
            state.copy(passwordError = "Password is required")
        } else if (state.password.length < 8) {
            state.copy(passwordError = "Password must be at least 8 characters")
        } else {
            state.copy(passwordError = "")
        }

        // Also validate confirm password as it depends on password
        if (state.confirmPassword.isNotEmpty()) {
            validateConfirmPassword()
        }
    }

    private fun validateConfirmPassword() {
        state = if (state.confirmPassword.isEmpty()) {
            state.copy(confirmPasswordError = "Please confirm your password")
        } else if (state.confirmPassword != state.password) {
            state.copy(confirmPasswordError = "Passwords do not match")
        } else {
            state.copy(confirmPasswordError = "")
        }
    }

    // Step 2: Personal Details methods
    fun updateName(name: String) {
        state = state.copy(name = name)
        validateName()
    }

    fun updateAge(age: String) {
        if (age.isEmpty() || age.matches("\\d*".toRegex())) {
            state = state.copy(age = age)
            validateAge()
        }
    }

    fun updateHeight(height: String) {
        if (height.isEmpty() || height.matches("\\d*".toRegex())) {
            state = state.copy(height = height)
            validateHeight()
        }
    }

    fun updateWeight(weight: String) {
        if (weight.isEmpty() || weight.matches("\\d*\\.?\\d*".toRegex())) {
            state = state.copy(weight = weight)
            validateWeight()
        }
    }

    private fun validateName() {
        state = if (state.name.isEmpty()) {
            state.copy(nameError = "Name is required")
        } else {
            state.copy(nameError = "")
        }
    }

    private fun validateAge() {
        state = when {
            state.age.isEmpty() -> {
                state.copy(ageError = "Age is required")
            }
            state.age.toIntOrNull() == null -> {
                state.copy(ageError = "Please enter a valid age")
            }
            state.age.toInt() < 13 -> {
                state.copy(ageError = "You must be at least 13 years old")
            }
            state.age.toInt() > 100 -> {
                state.copy(ageError = "Please enter a valid age")
            }
            else -> {
                state.copy(ageError = "")
            }
        }
    }

    private fun validateHeight() {
        state = when {
            state.height.isEmpty() -> {
                state.copy(heightError = "Height is required")
            }
            state.height.toIntOrNull() == null -> {
                state.copy(heightError = "Please enter a valid height")
            }
            state.height.toInt() < 50 || state.height.toInt() > 250 -> {
                state.copy(heightError = "Please enter a valid height (50-250 cm)")
            }
            else -> {
                state.copy(heightError = "")
            }
        }
    }

    private fun validateWeight() {
        state = when {
            state.weight.isEmpty() -> {
                state.copy(weightError = "Weight is required")
            }
            state.weight.toFloatOrNull() == null -> {
                state.copy(weightError = "Please enter a valid weight")
            }
            state.weight.toFloat() < 20 || state.weight.toFloat() > 300 -> {
                state.copy(weightError = "Please enter a valid weight (20-300 kg)")
            }
            else -> {
                state.copy(weightError = "")
            }
        }
    }

    // Step 3: Fitness Goals methods
    fun updateFitnessLevel(level: String) {
        state = state.copy(fitnessLevel = level)
        Log.d("SignupViewModel", "Fitness level updated to: $level")
    }

    fun toggleGoal(goal: String) {
        val currentGoals = state.goals.toMutableSet()
        if (currentGoals.contains(goal)) {
            currentGoals.remove(goal)
            Log.d("SignupViewModel", "Goal removed: $goal")
        } else {
            currentGoals.add(goal)
            Log.d("SignupViewModel", "Goal added: $goal")
        }
        state = state.copy(goals = currentGoals)
    }

    fun updateWeeklyWorkoutTarget(days: Int) {
        state = state.copy(weeklyWorkoutTarget = days)
        Log.d("SignupViewModel", "Weekly workout target updated to: $days days")
    }

    // Signup process
    fun completeSignup(onAuthSuccess: (String) -> Unit) {
        Log.d("SignupViewModel", "Starting completeSignup process")

        // Run validation
        validateEmail()
        validatePassword()
        validateConfirmPassword()
        validateName()
        validateAge()
        validateHeight()
        validateWeight()

        // Check if all validation passed
        if (!state.isAccountStepValid || !state.isPersonalStepValid || !state.isFitnessStepValid) {
            state = state.copy(error = "Please fix all validation errors before proceeding")
            return
        }

        state = state.copy(isLoading = true, error = "")
        Log.d("SignupViewModel", "Set loading state to true")

        viewModelScope.launch {
            try {
                Log.d("SignupViewModel", "Creating Firebase Auth account with email: ${state.email}")
                // 1. Create Firebase Auth account
                val authResult = firebaseAuth.createUserWithEmailAndPassword(
                    state.email.trim(),
                    state.password
                ).await()

                Log.d("SignupViewModel", "Firebase Auth account created successfully")

                // On successful registration, update state and trigger navigation callback.
                state = state.copy(isAuthenticated = true)

                // Extract username (first name) from the full name
                val username = state.name.split(" ").first()
                Log.d("SignupViewModel", "Extracted username: $username")

                // Call navigation callback
                onAuthSuccess(username)

                // Store additional user data in Firestore after navigation has been initiated
                val userId = authResult.user?.uid
                if (userId != null) {
                    try {
                        Log.d("SignupViewModel", "Storing user profile data in Firestore")
                        // 2. Store user profile data in Firestore
                        val userProfile = hashMapOf(
                            "name" to state.name,
                            "email" to state.email,
                            "age" to (state.age.toIntOrNull() ?: 0),
                            "height" to (state.height.toIntOrNull() ?: 0),
                            "weight" to (state.weight.toFloatOrNull() ?: 0f),
                            "fitnessLevel" to state.fitnessLevel,
                            "goals" to state.goals.toList(),
                            "weeklyWorkoutTarget" to state.weeklyWorkoutTarget,
                            "createdAt" to com.google.firebase.Timestamp.now()
                        )

                        firestore.collection("users")
                            .document(userId)
                            .set(userProfile)
                            .await()

                        Log.d("SignupViewModel", "User profile stored successfully")
                    } catch (e: Exception) {
                        Log.e("SignupViewModel", "Error storing user profile", e)
                        // Non-critical error - navigation already happened
                    }
                }

                // Finally clear loading state
                state = state.copy(isLoading = false)

            } catch (e: Exception) {
                Log.e("SignupViewModel", "Error in signup process", e)
                state = state.copy(error = e.message ?: "Unknown error occurred", isLoading = false)
            }
        }
    }
}