package presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
fun once(block: () -> Unit) { remember(block) }
