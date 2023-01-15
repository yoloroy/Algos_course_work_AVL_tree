import androidx.compose.ui.awt.ComposeWindow
import resources.strings.StringsRes

object Global { // TODO refactor
    lateinit var window: ComposeWindow
        internal set
    lateinit var stringsRes: StringsRes
        internal set
}

object StringRes {
    val ui get() = Global.stringsRes.ui
    val text get() = Global.stringsRes.text
}
