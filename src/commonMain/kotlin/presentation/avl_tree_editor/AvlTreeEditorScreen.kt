package presentation.avl_tree_editor

import androidx.compose.runtime.*
import com.yoloroy.algorithm_mod.AvlTree
import presentation.tree_editor.NodeEditDialogData
import presentation.util.once

@Composable
fun AvlTreeEditorScreen(tree: AvlTree<Int>, reloadTree: () -> Unit) {
    var dialogData by remember { mutableStateOf(NodeEditDialogData(false, "", "", "", null, {}, {}, {})) }
    once {
        dialogData = dialogData.copy(
            onClickDismiss = { dialogData = dialogData.copy(open = false) },
            updateValue = { text ->
                dialogData = dialogData.copy(
                    inputText = text,
                    isOk = text
                        .takeIf(String::isNotEmpty)
                        ?.let { if (it[0] == '-') it.drop(1) else it }
                        ?.all(Char::isDigit)
                )
            }
        )
    }
    AvlTreeEditor(
        tree = tree.tree,
        dialogData = dialogData,
        // TODO move logic to interactor
        insertNode = {
            dialogData = dialogData.copy(
                open = true,
                title = "Добавить значение в дерево",
                label = "Значение в узле",
                onClickOk = {
                    tree.insertValue(dialogData.inputText.toInt())
                    reloadTree()
                    dialogData = dialogData.copy(open = false)
                }
            )
        },
        deleteNode = { node ->
            tree.removeValue(node.value.value)
            reloadTree()
        },
        editNode = { node ->
            dialogData = dialogData.copy(
                open = true,
                title = "Изменить значение в узле дерева",
                label = "Значение в узле",
                onClickOk = {
                    tree.replaceValue(node.value.value, dialogData.inputText.toInt())
                    reloadTree()
                    dialogData = dialogData.copy(open = false)
                }
            )
        }
    )
}
