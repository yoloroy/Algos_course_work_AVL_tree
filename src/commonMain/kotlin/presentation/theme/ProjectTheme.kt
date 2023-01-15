package presentation.theme

import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import presentation.util.argb

@Composable
private fun projectColors() = MaterialTheme.colors.copy(
    primary = 0xffaa3366.argb,
    background = 0xfff6f5f7.argb,
    surface = 0xffffffff.argb,
    onSurface = 0xdd000000.argb
)

@Composable
fun ProjectTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = projectColors(),
        content = content
    )
}
