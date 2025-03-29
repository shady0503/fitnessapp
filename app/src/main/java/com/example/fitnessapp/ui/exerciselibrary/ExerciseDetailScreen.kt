package com.example.fitnessapp.ui.exerciselibrary

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.fitnessapp.R
import com.example.fitnessapp.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseDetailScreen(
    exercise: Exercise,
    onBackPressed: () -> Unit
) {
    var isFavorite by remember { mutableStateOf(false) }
    var showContent by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        delay(300)
        showContent = true
    }

    Scaffold(
        topBar = {
            DetailTopBar(
                title = exercise.name,
                onBackPressed = onBackPressed,
                onFavoriteToggle = {
                    isFavorite = !isFavorite
                    val message = if (isFavorite) "Added to favorites" else "Removed from favorites"
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(message)
                    }
                },
                isFavorite = isFavorite
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            // Exercise Header
            item {
                ExerciseHeader(exercise = exercise, showContent = showContent)
            }

            // Exercise Details
            item {
                AnimatedVisibility(
                    visible = showContent,
                    enter = fadeIn() + slideInVertically(initialOffsetY = { 50 })
                ) {
                    ExerciseDetailsSection(exercise = exercise)
                }
            }

            // Exercise Instructions
            item {
                AnimatedVisibility(
                    visible = showContent,
                    enter = fadeIn() + slideInVertically(initialOffsetY = { 50 })
                ) {
                    ExerciseInstructionsSection(exercise = exercise)
                }
            }

            // Target Muscles
            item {
                AnimatedVisibility(
                    visible = showContent,
                    enter = fadeIn() + slideInVertically(initialOffsetY = { 50 })
                ) {
                    TargetMusclesSection(exercise = exercise)
                }
            }

            // Equipment
            item {
                AnimatedVisibility(
                    visible = showContent,
                    enter = fadeIn() + slideInVertically(initialOffsetY = { 50 })
                ) {
                    EquipmentSection(exercise = exercise)
                }
            }

            // Add to Workout Button
            item {
                AnimatedVisibility(
                    visible = showContent,
                    enter = fadeIn() + slideInVertically(initialOffsetY = { 50 })
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Added to your workout")
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                text = "Add to Workout",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailTopBar(
    title: String,
    onBackPressed: () -> Unit,
    onFavoriteToggle: () -> Unit,
    isFavorite: Boolean
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        actions = {
            IconButton(onClick = onFavoriteToggle) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                    tint = if (isFavorite) Color.Red else MaterialTheme.colorScheme.onSurface
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    )
}

@Composable
fun ExerciseHeader(exercise: Exercise, showContent: Boolean) {
    val categoryColors = when (exercise.category) {
        "Strength" -> listOf(FancyPurple, FancyPurple.copy(alpha = 0.8f))
        "Cardio" -> listOf(FancyBlue, FancyBlue.copy(alpha = 0.8f))
        "Flexibility" -> listOf(FancyPink, FancyPink.copy(alpha = 0.8f))
        "Yoga" -> listOf(FancyPurple, FancyBlue)
        "Balance" -> listOf(FancyOrange, FancyOrange.copy(alpha = 0.8f))
        else -> listOf(FancyPurple, FancyBlue)
    }

    AnimatedVisibility(
        visible = showContent,
        enter = fadeIn() + slideInVertically(initialOffsetY = { -50 })
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .background(
                    brush = Brush.verticalGradient(categoryColors)
                ),
            contentAlignment = Alignment.Center
        ) {
            // Placeholder for exercise image
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(
                        id = R.drawable.ic_fitness
                    ),
                    contentDescription = exercise.name,
                    modifier = Modifier.size(80.dp),
                    tint = Color.White
                )
            }

            // Difficulty label
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.Black.copy(alpha = 0.4f))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = exercise.difficulty,
                    color = Color.White,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@Composable
fun ExerciseDetailsSection(exercise: Exercise) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Description",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = exercise.description,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Quick info row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                InfoItem(
                    icon = Icons.Outlined.Bolt,
                    label = "Level",
                    value = exercise.difficulty
                )

                Divider(
                    modifier = Modifier
                        .height(40.dp)
                        .width(1.dp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                )

                InfoItem(
                    icon = Icons.Outlined.Category,
                    label = "Type",
                    value = exercise.category
                )

                Divider(
                    modifier = Modifier
                        .height(40.dp)
                        .width(1.dp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                )

                InfoItem(
                    icon = Icons.Outlined.Layers,
                    label = "Muscles",
                    value = "${exercise.muscles.size} groups"
                )
            }
        }
    }
}

@Composable
fun InfoItem(
    icon: ImageVector,
    label: String,
    value: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun ExerciseInstructionsSection(exercise: Exercise) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Instructions",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        exercise.instructions.forEachIndexed { index, instruction ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${index + 1}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = instruction,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun TargetMusclesSection(exercise: Exercise) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Text(
            text = "Target Muscles",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            exercise.muscles.forEach { muscle ->
                MuscleChip(muscle = muscle)
            }
        }
    }
}

@Composable
fun FlowRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        val horizontalGapPx = 0  // Gap between items
        val verticalGapPx = 0    // Gap between rows
        val rowWidths = mutableListOf<Int>()
        val rowHeights = mutableListOf<Int>()
        val placeables = measurables.map { measurable ->
            val placeable = measurable.measure(constraints)

            // Start a new row if we've run out of space
            val rowIndex = if (rowWidths.isEmpty()) {
                0
            } else {
                val lastRowWidth = rowWidths.last() + horizontalGapPx
                if (lastRowWidth + placeable.width <= constraints.maxWidth) {
                    rowWidths.size - 1
                } else {
                    rowWidths.size
                }
            }

            if (rowIndex >= rowWidths.size) {
                rowWidths.add(placeable.width)
                rowHeights.add(placeable.height)
            } else {
                rowWidths[rowIndex] += placeable.width + horizontalGapPx
                rowHeights[rowIndex] = maxOf(rowHeights[rowIndex], placeable.height)
            }

            placeable to rowIndex
        }

        val width = rowWidths.maxOrNull()?.coerceIn(constraints.minWidth, constraints.maxWidth) ?: constraints.minWidth
        val height = (rowHeights.sum() + (rowHeights.size - 1) * verticalGapPx).coerceIn(constraints.minHeight, constraints.maxHeight)

        val rowY = mutableListOf<Int>()
        var currentY = 0
        rowHeights.forEach { rowHeight ->
            rowY.add(currentY)
            currentY += rowHeight + verticalGapPx
        }

        layout(width, height) {
            val rowX = IntArray(rowWidths.size) { 0 }
            placeables.forEach { (placeable, rowIndex) ->
                placeable.place(
                    x = rowX[rowIndex],
                    y = rowY[rowIndex]
                )
                rowX[rowIndex] += placeable.width + horizontalGapPx
            }
        }
    }
}

@Composable
fun EquipmentSection(exercise: Exercise) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Equipment Needed",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        exercise.equipment.forEach { equipment ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(end = 8.dp)
                )

                Text(
                    text = equipment,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MuscleChip(muscle: String) {
    SuggestionChip(
        onClick = { /* No action */ },
        label = { Text(muscle) },
        colors = SuggestionChipDefaults.suggestionChipColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            labelColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
    )
}