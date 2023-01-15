import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import presentation.main_screen.MainScreen
import presentation.theme.ProjectTheme

object Global {
    lateinit var window: ComposeWindow
}

@Composable
@Preview
fun App() {
    ProjectTheme {
        MainScreen()
    }
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "AVL Tree Simulator",
        icon = painterResource("icons/binary-search-tree.svg")
    ) {
        App()
        Global.window = window
    }
}
