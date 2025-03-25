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
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp)
)

// Optional wave shape
object WaveShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            // Start top-left
            moveTo(0f, 0f)
            // Down to 80% of height
            lineTo(0f, size.height * 0.8f)
            // Curve across
            quadraticBezierTo(
                size.width * 0.5f,
                size.height,
                size.width,
                size.height * 0.8f
            )
            // Up to top-right
            lineTo(size.width, 0f)
            close()
        }
        return Outline.Generic(path)
    }
}
