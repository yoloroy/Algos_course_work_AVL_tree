import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import presentation.main_screen.MainScreen
import presentation.theme.ProjectTheme

context(ApplicationScope)
@Composable
@Preview
fun App() {
    Window(
        onCloseRequest = ::exitApplication,
        title = "AVL Tree Simulator",
        icon = painterResource("icons/binary-search-tree.svg")
    ) {
        Global.window = window
        ProjectTheme {
            MainScreen()
        }
    }
}
