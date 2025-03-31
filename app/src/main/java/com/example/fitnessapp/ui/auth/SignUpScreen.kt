package com.example.fitnessapp.auth

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fitnessapp.theme.FancyBlue
import com.example.fitnessapp.theme.FancyPurple
import com.example.fitnessapp.theme.WaveShape

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun SignupScreen(
    onSignupComplete: (username: String) -> Unit,
    onBackToLogin: () -> Unit,
    viewModel: SignupViewModel = viewModel()
) {
    val state = viewModel.state
    val scrollState = rememberScrollState()

    // Add debugging to track state changes
    LaunchedEffect(state.isAuthenticated) {
        Log.d("SignupScreen", "Authentication state changed: ${state.isAuthenticated}")
        if (state.isAuthenticated) {
            Log.d("SignupScreen", "User authenticated, navigating with username: ${state.name.split(" ").first()}")
            onSignupComplete(state.name.split(" ").first())
        }
    }

    // Track if isLoading state changes
    LaunchedEffect(state.isLoading) {
        Log.d("SignupScreen", "Loading state changed: ${state.isLoading}")
    }

    // Track if error state changes
    LaunchedEffect(state.error) {
        if (state.error.isNotEmpty()) {
            Log.d("SignupScreen", "Error occurred: ${state.error}")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top wave decoration
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .clip(WaveShape)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            FancyPurple,
                            FancyBlue
                        )
                    )
                )
        ) {
            Text(
                text = "Create Account",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(bottom = 24.dp)
            )
        }

        // Progress indicator
        Spacer(modifier = Modifier.height(24.dp))

        StepProgressIndicator(
            currentStep = state.currentStep,
            totalSteps = 3
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Step title
        Text(
            text = when (state.currentStep) {
                1 -> "Account Information"
                2 -> "Personal Details"
                3 -> "Fitness Goals"
                else -> ""
            },
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Main content area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            // Step content
            AnimatedContent(
                targetState = state.currentStep,
                transitionSpec = {
                    fadeIn(animationSpec = tween(300)) togetherWith
                            fadeOut(animationSpec = tween(300))
                }
            ) { step ->
                when (step) {
                    1 -> AccountInfoStep(viewModel)
                    2 -> PersonalDetailsStep(viewModel)
                    3 -> FitnessGoalsStep(viewModel)
                    else -> Box {}
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Navigation buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Back button
            if (state.currentStep > 1) {
                OutlinedButton(
                    onClick = { viewModel.previousStep() },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Back")
                }
                Spacer(modifier = Modifier.width(16.dp))
            }

            // Next/Finish button
            Button(
                onClick = {
                    if (state.currentStep < 3) {
                        viewModel.nextStep()
                    } else {
                        Log.d("SignupScreen", "Finish button clicked, starting signup process")
                        viewModel.completeSignup(onSignupComplete)
                    }
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                enabled = when (state.currentStep) {
                    1 -> state.isAccountStepValid
                    2 -> state.isPersonalStepValid
                    3 -> state.isFitnessStepValid
                    else -> false
                }
            ) {
                Text(if (state.currentStep < 3) "Next" else "Finish")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Back to login option
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Already have an account?",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )
            TextButton(onClick = onBackToLogin) {
                Text(
                    "Login",
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Error message
        if (state.error.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Surface(
                color = MaterialTheme.colorScheme.errorContainer,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = state.error,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

    // Loading overlay - Modified to prevent interaction with the UI when loading
    if (state.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable(enabled = false) { /* Intercept all clicks */ },
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(color = Color.White)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Loading...",
                    color = Color.White
                )
            }
        }
    }
}

// Rest of the composables remain unchanged
@Composable
fun StepProgressIndicator(currentStep: Int, totalSteps: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        for (i in 1..totalSteps) {
            val isActive = i <= currentStep
            val color = if (isActive)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)

            // Step circle
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(color),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = i.toString(),
                    color = if (isActive) Color.White else Color.Black,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            // Connector line
            if (i < totalSteps) {
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(2.dp)
                        .align(Alignment.CenterVertically)
                        .background(
                            if (i < currentStep) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
                        )
                )
            }
        }
    }
}

@Composable
fun AccountInfoStep(viewModel: SignupViewModel) {
    val state = viewModel.state
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Email
        OutlinedTextField(
            value = state.email,
            onValueChange = { viewModel.updateEmail(it) },
            label = { Text("Email") },
            placeholder = { Text("Enter your email") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Email"
                )
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            shape = RoundedCornerShape(16.dp),
            isError = state.emailError.isNotEmpty(),
            supportingText = {
                if (state.emailError.isNotEmpty()) {
                    Text(state.emailError)
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password
        OutlinedTextField(
            value = state.password,
            onValueChange = { viewModel.updatePassword(it) },
            label = { Text("Password") },
            placeholder = { Text("Create a password") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Password"
                )
            },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password"
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            shape = RoundedCornerShape(16.dp),
            isError = state.passwordError.isNotEmpty(),
            supportingText = {
                if (state.passwordError.isNotEmpty()) {
                    Text(state.passwordError)
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Confirm Password
        OutlinedTextField(
            value = state.confirmPassword,
            onValueChange = { viewModel.updateConfirmPassword(it) },
            label = { Text("Confirm Password") },
            placeholder = { Text("Confirm your password") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Confirm Password"
                )
            },
            trailingIcon = {
                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Icon(
                        imageVector = if (confirmPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = if (confirmPasswordVisible) "Hide password" else "Show password"
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            shape = RoundedCornerShape(16.dp),
            isError = state.confirmPasswordError.isNotEmpty(),
            supportingText = {
                if (state.confirmPasswordError.isNotEmpty()) {
                    Text(state.confirmPasswordError)
                }
            }
        )
    }
}

@Composable
fun PersonalDetailsStep(viewModel: SignupViewModel) {
    val state = viewModel.state

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Name
        OutlinedTextField(
            value = state.name,
            onValueChange = { viewModel.updateName(it) },
            label = { Text("Full Name") },
            placeholder = { Text("Enter your name") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Name"
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            isError = state.nameError.isNotEmpty(),
            supportingText = {
                if (state.nameError.isNotEmpty()) {
                    Text(state.nameError)
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Age
        OutlinedTextField(
            value = state.age,
            onValueChange = { viewModel.updateAge(it) },
            label = { Text("Age") },
            placeholder = { Text("Enter your age") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.DateRange,
                    contentDescription = "Age"
                )
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = RoundedCornerShape(16.dp),
            isError = state.ageError.isNotEmpty(),
            supportingText = {
                if (state.ageError.isNotEmpty()) {
                    Text(state.ageError)
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Height
        OutlinedTextField(
            value = state.height,
            onValueChange = { viewModel.updateHeight(it) },
            label = { Text("Height (cm)") },
            placeholder = { Text("Enter your height") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Height,
                    contentDescription = "Height"
                )
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = RoundedCornerShape(16.dp),
            isError = state.heightError.isNotEmpty(),
            supportingText = {
                if (state.heightError.isNotEmpty()) {
                    Text(state.heightError)
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Weight
        OutlinedTextField(
            value = state.weight,
            onValueChange = { viewModel.updateWeight(it) },
            label = { Text("Weight (kg)") },
            placeholder = { Text("Enter your weight") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.MonitorWeight,
                    contentDescription = "Weight"
                )
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = RoundedCornerShape(16.dp),
            isError = state.weightError.isNotEmpty(),
            supportingText = {
                if (state.weightError.isNotEmpty()) {
                    Text(state.weightError)
                }
            }
        )
    }
}

@Composable
fun FitnessGoalsStep(viewModel: SignupViewModel) {
    val state = viewModel.state

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Fitness level selection
        Text(
            text = "Your Fitness Level",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        FitnessLevelSelector(
            selectedLevel = state.fitnessLevel,
            onLevelSelected = { viewModel.updateFitnessLevel(it) }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Fitness goals
        Text(
            text = "Your Fitness Goals",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        Column(modifier = Modifier.fillMaxWidth()) {
            FitnessGoalItem(
                goal = "Lose Weight",
                icon = Icons.Outlined.TrendingDown,
                isSelected = state.goals.contains("Lose Weight"),
                onToggle = { viewModel.toggleGoal("Lose Weight") }
            )

            FitnessGoalItem(
                goal = "Build Muscle",
                icon = Icons.Outlined.FitnessCenter,
                isSelected = state.goals.contains("Build Muscle"),
                onToggle = { viewModel.toggleGoal("Build Muscle") }
            )

            FitnessGoalItem(
                goal = "Improve Cardio",
                icon = Icons.Outlined.DirectionsRun,
                isSelected = state.goals.contains("Improve Cardio"),
                onToggle = { viewModel.toggleGoal("Improve Cardio") }
            )

            FitnessGoalItem(
                goal = "Increase Flexibility",
                icon = Icons.Outlined.AccessibilityNew,
                isSelected = state.goals.contains("Increase Flexibility"),
                onToggle = { viewModel.toggleGoal("Increase Flexibility") }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Weekly workout target
        Text(
            text = "Weekly Workout Target",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            for (i in 1..7) {
                DaySelector(
                    day = i,
                    isSelected = state.weeklyWorkoutTarget == i,
                    onSelect = { viewModel.updateWeeklyWorkoutTarget(i) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FitnessLevelSelector(
    selectedLevel: String,
    onLevelSelected: (String) -> Unit
) {
    val levels = listOf("Beginner", "Intermediate", "Advanced")

    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
        levels.forEachIndexed { index, level ->
            SegmentedButton(
                selected = selectedLevel == level,
                onClick = { onLevelSelected(level) },
                shape = when (index) {
                    0 -> MaterialTheme.shapes.small.copy(
                        topEnd = ZeroCornerSize,
                        bottomEnd = ZeroCornerSize
                    )
                    levels.size - 1 -> MaterialTheme.shapes.small.copy(
                        topStart = ZeroCornerSize,
                        bottomStart = ZeroCornerSize
                    )
                    else -> MaterialTheme.shapes.small.copy(
                        topStart = ZeroCornerSize,
                        bottomStart = ZeroCornerSize,
                        topEnd = ZeroCornerSize,
                        bottomEnd = ZeroCornerSize
                    )
                }
            ) {
                Text(level)
            }
        }
    }
}

@Composable
fun FitnessGoalItem(
    goal: String,
    icon: ImageVector,
    isSelected: Boolean,
    onToggle: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onToggle),
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected)
            MaterialTheme.colorScheme.primaryContainer
        else
            MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = goal,
                tint = if (isSelected)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = goal,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.weight(1f))

            Checkbox(
                checked = isSelected,
                onCheckedChange = { onToggle() }
            )
        }
    }
}

@Composable
fun DaySelector(
    day: Int,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(
                if (isSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.surfaceVariant
            )
            .clickable(onClick = onSelect),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.toString(),
            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}