package presentation.util

import androidx.compose.ui.geometry.Offset

operator fun Offset.times(other: Offset) = Offset(x * other.x, y * other.y)
