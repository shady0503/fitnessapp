package com.example.fitnessapp.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

val FancyShapes = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp)
)

// Improved wave shape with flatter bottom curve
object WaveShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            // Start top-left
            moveTo(0f, 0f)
            // Go down further - 75% of height
            lineTo(0f, size.height * 0.75f)
            // Deeper curve with control points reaching lower
            cubicTo(
                size.width * 0.25f, size.height * 0.95f,  // Control point 1 - deeper
                size.width * 0.75f, size.height * 0.95f,  // Control point 2 - deeper
                size.width, size.height * 0.75f           // End point - goes down further
            )
            // Up to top-right
            lineTo(size.width, 0f)
            close()
        }
        return Outline.Generic(path)
    }
}
// Additional shape for profile picture and circular elements
val CircleCornerShape = RoundedCornerShape(percent = 50)