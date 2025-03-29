package com.example.fitnessapp.ui.exerciselibrary

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fitnessapp.R
import com.example.fitnessapp.theme.*
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseLibraryScreen(
    onExerciseSelected: (Exercise) -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: ExerciseLibraryViewModel = viewModel()
) {
    val exercises = viewModel.exercises
    val selectedCategory = viewModel.selectedCategory
    var searchQuery by remember { mutableStateOf("") }
    var showContent by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(300)
        showContent = true
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Exercise Library") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Search Bar
            AnimatedVisibility(
                visible = showContent,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    placeholder = { Text("Search exercises...") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    singleLine = true
                )
            }

            // Categories
            AnimatedVisibility(
                visible = showContent,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                CategorySelector(
                    categories = viewModel.categories,
                    selectedCategory = selectedCategory,
                    onCategorySelected = { viewModel.selectCategory(it) }
                )
            }

            // Exercises List
            AnimatedVisibility(
                visible = showContent,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                val filteredExercises = if (searchQuery.isBlank()) {
                    if (selectedCategory == "All") exercises else exercises.filter { it.category == selectedCategory }
                } else {
                    exercises.filter {
                        it.name.contains(searchQuery, ignoreCase = true) &&
                                (selectedCategory == "All" || it.category == selectedCategory)
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    items(filteredExercises) { exercise ->
                        ExerciseItem(
                            exercise = exercise,
                            onClick = { onExerciseSelected(exercise) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CategorySelector(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    val categoryIcons = mapOf(
        "All" to Icons.Outlined.FitnessCenter,
        "Strength" to Icons.Outlined.FitnessCenter,
        "Cardio" to Icons.Outlined.DirectionsRun,
        "Flexibility" to Icons.Outlined.AccessibilityNew,
        "Balance" to Icons.Outlined.Balance,
        "Yoga" to Icons.Outlined.SelfImprovement
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Categories",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
        )

        // Horizontal scrollable categories
        androidx.compose.foundation.lazy.LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(categories) { category ->
                CategoryChip(
                    category = category,
                    icon = categoryIcons[category] ?: Icons.Outlined.FitnessCenter,
                    isSelected = category == selectedCategory,
                    onClick = { onCategorySelected(category) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryChip(
    category: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
    val contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant

    SuggestionChip(
        onClick = onClick,
        label = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = category,
                    color = contentColor
                )
            }
        },
        shape = RoundedCornerShape(8.dp),
        colors = SuggestionChipDefaults.suggestionChipColors(
            containerColor = containerColor,
            labelColor = contentColor
        ),
        border = null
    )
}

@Composable
fun ExerciseItem(
    exercise: Exercise,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Exercise Image/Icon
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(
                        Brush.linearGradient(
                            colors = when (exercise.category) {
                                "Strength" -> listOf(FancyPurple, FancyPurple.copy(alpha = 0.7f))
                                "Cardio" -> listOf(FancyBlue, FancyBlue.copy(alpha = 0.7f))
                                "Flexibility" -> listOf(FancyPink, FancyPink.copy(alpha = 0.7f))
                                else -> listOf(FancyOrange, FancyOrange.copy(alpha = 0.7f))
                            }
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(
                        id = when (exercise.category) {
                            "Strength" -> R.drawable.ic_fitness
                            "Cardio" -> R.drawable.ic_fitness
                            "Flexibility" -> R.drawable.ic_fitness
                            else -> R.drawable.ic_fitness
                        }
                    ),
                    contentDescription = exercise.category,
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Exercise Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = exercise.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = exercise.category,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = exercise.description,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
            }

            // Difficulty indicator
            DifficultyIndicator(difficulty = exercise.difficulty)
        }
    }
}

@Composable
fun DifficultyIndicator(difficulty: String) {
    val (color, text) = when (difficulty.lowercase()) {
        "beginner" -> Pair(Color(0xFF4CAF50), "B")
        "intermediate" -> Pair(Color(0xFFFFA726), "I")
        "advanced" -> Pair(Color(0xFFE53935), "A")
        else -> Pair(FancyPurple, "?")
    }

    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(color),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.labelLarge
        )
    }
}