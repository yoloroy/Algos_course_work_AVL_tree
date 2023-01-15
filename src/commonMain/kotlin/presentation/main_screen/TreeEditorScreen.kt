package presentation.main_screen

import androidx.compose.runtime.*
import com.yoloroy.algorithm_mod.BinaryGraphTree
import com.yoloroy.algorithm_mod.BinaryGraphTree.Node
import com.yoloroy.algorithm_mod.MutableBinaryGraphTree
import presentation.tree_editor.NodeEditDialogData
import presentation.tree_editor.TreeEditor
import presentation.util.once
import com.yoloroy.algorithm_mod.MutableBinaryGraphTree.Node as MutableNode

@Composable
fun TreeEditorScreen(tree: MutableBinaryGraphTree<Int>, reloadTree: () -> Unit) {
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
    TreeEditor(
        tree = tree,
        dialogData = dialogData,
        // TODO move logic to interactor
        addNode = { parent, leftRight ->
            parent as MutableNode
            dialogData = dialogData.copy(
                open = true,
                title = "Добавить узел дерева",
                label = "Значение в узле",
                onClickOk = {
                    when (leftRight) {
                        BinaryGraphTree.LeftRight.Left -> parent.setLeftAndAssignParent(MutableNode(dialogData.inputText.toInt()))
                        BinaryGraphTree.LeftRight.Right -> parent.setRightAndAssignParent(MutableNode(dialogData.inputText.toInt()))
                    }
                    reloadTree()
                    dialogData = dialogData.copy(open = false)
                }
            )
        },
        deleteNode = mutableNodeLambda { node ->
            node.removeFromParent()
            reloadTree()
        },
        editNode = mutableNodeLambda { node ->
            dialogData = dialogData.copy(
                open = true,
                title = "Изменить значение в узле дерева",
                label = "Значение в узле",
                onClickOk = {
                    node.value = dialogData.inputText.toInt()
                    reloadTree()
                    dialogData = dialogData.copy(open = false)
                }
            )
        },
        rotateLeftLeft = mutableNodeLambda { node ->
            tree.rotateLeftLeft(node)
            reloadTree()
        },
        rotateLeftRight = mutableNodeLambda { node ->
            tree.rotateLeftRight(node)
            reloadTree()
        },
        rotateRightLeft = mutableNodeLambda { node ->
            tree.rotateRightLeft(node)
            reloadTree()
        },
        rotateRightRight = mutableNodeLambda { node ->
            tree.rotateRightRight(node)
            reloadTree()
        }
    )
}

private inline fun <T> mutableNodeLambda(crossinline block: (MutableNode<T>) -> Unit) = { node: Node<T> -> block(node as MutableNode<T>) }
