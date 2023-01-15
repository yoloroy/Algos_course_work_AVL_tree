package resources.strings

import androidx.compose.ui.text.intl.Locale
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

data class StringsRes(
    val text: Text,
    val ui: Ui
) {
    companion object {
        val default get() = StringsRes(
            Text(help = "Helper page"),
            Ui(
                BottomBar(
                    avlTreeEditor = "AVL-tree",
                    binaryTreeEditor = "Binary tree",
                    help = "Help",
                    loadTree = "Load tree",
                    saveTree = "Save tree"
                ),
                treeActions = TreeActions(
                    avlTreeEditor = AvlTreeEditor(
                        delete = "Remove value",
                        insert = "Insert value",
                        replace = "Replace value"
                    ),
                    binaryTreeEditor = BinaryTreeEditor(
                        add = "Add node",
                        delete = "Delete node",
                        edit = "Edit value",
                        rotateLL = "Rotate for LL case",
                        rotateLR = "Rotate for LR case",
                        rotateRL = "Rotate for RL case",
                        rotateRR = "Rotate for RR case"
                    ),
                    dialogLabel = DialogLabel(
                        value = "Value",
                        valueForNode = "Value for node",
                        cancel = "Cancel",
                        ok = "Ok"
                    )
                )
            )
        )

        private val mapper = jacksonObjectMapper()

        fun load(locale: Locale): StringsRes = ClassLoader
            .getSystemResourceAsStream("strings/${locale.language}.json")
            ?.let { mapper.readValue(it, StringsRes::class.java) }
            ?: null.also {
                System.err.println("String resources was not loaded successfully, language: ${locale.language}")
            }
            ?: ClassLoader
                .getSystemResourceAsStream("strings/en.json")
                ?.let { mapper.readValue(it, StringsRes::class.java) }
            ?: default
    }
}
