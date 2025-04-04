package com.example.fitnessapp.ui.exerciselibrary

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

data class Exercise(
    val id: String,
    val name: String,
    val category: String,
    val difficulty: String,
    val description: String,
    val instructions: List<String>,
    val muscles: List<String>,
    val equipment: List<String>,
    val imageResId: Int? = null
)

class ExerciseLibraryViewModel : ViewModel() {

    // Available categories
    val categories = listOf("All", "Strength", "Cardio", "Flexibility", "Balance", "Yoga")

    // Currently selected category
    var selectedCategory by mutableStateOf("All")
        private set

    // Sample exercises data
    val exercises = listOf(
        Exercise(
            id = "1",
            name = "Push-Up",
            category = "Strength",
            difficulty = "Beginner",
            description = "A classic exercise that targets the chest, shoulders, and triceps.",
            instructions = listOf(
                "Start in a plank position with hands slightly wider than shoulder-width apart.",
                "Lower your body until your chest nearly touches the floor.",
                "Push yourself back up to the starting position.",
                "Keep your body in a straight line throughout the movement."
            ),
            muscles = listOf("Chest", "Shoulders", "Triceps", "Core"),
            equipment = listOf("None")
        ),
        Exercise(
            id = "2",
            name = "Squats",
            category = "Strength",
            difficulty = "Beginner",
            description = "A fundamental lower body exercise that targets the quadriceps, hamstrings, and glutes.",
            instructions = listOf(
                "Stand with feet shoulder-width apart.",
                "Push your hips back and bend your knees as if sitting in a chair.",
                "Lower until your thighs are parallel to the ground or as low as you can comfortably go.",
                "Push through your heels to return to the starting position."
            ),
            muscles = listOf("Quadriceps", "Hamstrings", "Glutes", "Core"),
            equipment = listOf("None")
        ),
        Exercise(
            id = "3",
            name = "Running",
            category = "Cardio",
            difficulty = "Intermediate",
            description = "Improve cardiovascular health, burn calories, and build endurance.",
            instructions = listOf(
                "Start with a 5-minute warm-up walk.",
                "Begin running at a comfortable pace.",
                "Maintain an upright posture with relaxed shoulders.",
                "Cool down with a 5-minute walk at the end."
            ),
            muscles = listOf("Quadriceps", "Hamstrings", "Calves", "Core"),
            equipment = listOf("Running shoes", "Optional: Treadmill")
        ),
        Exercise(
            id = "4",
            name = "Plank",
            category = "Strength",
            difficulty = "Beginner",
            description = "A core strengthening exercise that also engages the shoulders and back.",
            instructions = listOf(
                "Start in a push-up position, then bend your elbows 90 degrees and rest your weight on your forearms.",
                "Keep your body in a straight line from head to feet.",
                "Engage your core and do not allow your hips to sag or rise.",
                "Hold the position for the desired duration."
            ),
            muscles = listOf("Core", "Shoulders", "Back", "Glutes"),
            equipment = listOf("None")
        ),
        Exercise(
            id = "5",
            name = "Jumping Jacks",
            category = "Cardio",
            difficulty = "Beginner",
            description = "A full-body cardio exercise that increases heart rate and improves coordination.",
            instructions = listOf(
                "Start with your feet together and arms at your sides.",
                "Jump up, spreading your feet beyond shoulder width and raising your arms above your head.",
                "Jump again, returning to the starting position.",
                "Repeat at a fast pace."
            ),
            muscles = listOf("Full body"),
            equipment = listOf("None")
        ),
        Exercise(
            id = "6",
            name = "Downward Dog",
            category = "Yoga",
            difficulty = "Beginner",
            description = "A yoga pose that stretches the hamstrings, calves, and shoulders while strengthening the arms and legs.",
            instructions = listOf(
                "Start on your hands and knees with wrists under shoulders and knees under hips.",
                "Lift your hips up and back to form an inverted V shape.",
                "Straighten your legs as much as comfortable, pressing your heels toward the floor.",
                "Keep your head between your arms, looking at your legs or navel."
            ),
            muscles = listOf("Shoulders", "Hamstrings", "Calves", "Arms"),
            equipment = listOf("Yoga mat")
        ),
        Exercise(
            id = "7",
            name = "Pull-Ups",
            category = "Strength",
            difficulty = "Advanced",
            description = "An upper body compound exercise that targets the back, biceps, and shoulders.",
            instructions = listOf(
                "Hang from a pull-up bar with palms facing away from you and hands slightly wider than shoulder-width apart.",
                "Pull yourself up until your chin is above the bar.",
                "Lower yourself back down with control until arms are fully extended.",
                "Repeat for desired reps."
            ),
            muscles = listOf("Back", "Biceps", "Shoulders", "Core"),
            equipment = listOf("Pull-up bar")
        ),
        Exercise(
            id = "8",
            name = "Burpees",
            category = "Cardio",
            difficulty = "Advanced",
            description = "A high-intensity full body exercise that combines strength, cardio, and core training.",
            instructions = listOf(
                "Start in a standing position.",
                "Drop into a squat position and place your hands on the ground.",
                "Kick your feet back into a plank position and perform a push-up.",
                "Return your feet to the squat position, then jump up explosively with arms overhead."
            ),
            muscles = listOf("Full body"),
            equipment = listOf("None")
        ),
        Exercise(
            id = "9",
            name = "Seated Forward Bend",
            category = "Flexibility",
            difficulty = "Beginner",
            description = "A seated forward fold that deeply stretches the hamstrings and lower back.",
            instructions = listOf(
                "Sit on the floor with legs extended straight in front of you.",
                "Hinge at the hips and reach forward toward your toes.",
                "Go as far as feels comfortable without straining.",
                "Hold the position, breathing deeply."
            ),
            muscles = listOf("Hamstrings", "Lower back"),
            equipment = listOf("None", "Optional: Yoga mat")
        ),
        Exercise(
            id = "10",
            name = "Single-Leg Balance",
            category = "Balance",
            difficulty = "Intermediate",
            description = "Improves stability, coordination, and strengthens the standing leg.",
            instructions = listOf(
                "Stand tall with feet together.",
                "Shift your weight to one leg and lift the other foot off the ground.",
                "Hold the position, keeping your torso upright.",
                "For an added challenge, close your eyes or move the raised leg into different positions."
            ),
            muscles = listOf("Ankles", "Legs", "Core"),
            equipment = listOf("None")
        )
    )

    // Function to select a category
    fun selectCategory(category: String) {
        selectedCategory = category
    }
}