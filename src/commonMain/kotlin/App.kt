import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import presentation.main_screen.MainScreen
import presentation.theme.ProjectTheme
import presentation.util.once
import resources.strings.StringsRes

context(ApplicationScope)
@Composable
@Preview
fun App() {
    once {
        Global.stringsRes = StringsRes.load(Locale.current)
    }

    Window(
        onCloseRequest = ::exitApplication,
        title = "AVL Tree Simulator",
        icon = painterResource("icons/avl-tree-ru.svg")
    ) {
        Global.window = window
        ProjectTheme {
            MainScreen()
        }
    }
}
