package presentation.util

import androidx.compose.ui.awt.ComposeWindow
import java.awt.FileDialog
import java.io.File

fun openLoadFileDialog(
    window: ComposeWindow,
    title: String,
    allowedExtensions: List<String>,
    allowMultiSelection: Boolean = true
) = launchDialog(window, title, allowedExtensions, allowMultiSelection, FileDialog.LOAD)

fun openSaveFileDialog(
    window: ComposeWindow,
    title: String,
    allowedExtensions: List<String>,
    allowMultiSelection: Boolean = true
) = launchDialog(window, title, allowedExtensions, allowMultiSelection, FileDialog.SAVE)

private fun launchDialog(
    window: ComposeWindow,
    title: String,
    allowedExtensions: List<String>,
    allowMultiSelection: Boolean = true,
    mode: Int
): List<File> = FileDialog(window, title, mode).apply {
    isMultipleMode = allowMultiSelection

    // windows
    file = allowedExtensions.joinToString(";") { "*$it" } // e.g. '*.jpg'

    // linux
    setFilenameFilter { _, name ->
        allowedExtensions.any {
            name.endsWith(it)
        }
    }

    isVisible = true
}.files.toList()
