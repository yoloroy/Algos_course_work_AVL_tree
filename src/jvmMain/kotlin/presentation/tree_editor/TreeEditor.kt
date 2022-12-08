package presentation.tree_editor

import androidx.compose.runtime.*
import androidx.compose.ui.window.singleWindowApplication
import com.yoloroy.algorithm_mod.BinaryGraphTree
import com.yoloroy.algorithm_mod.BinaryGraphTree.LeftRight
import com.yoloroy.algorithm_mod.BinaryGraphTree.Node
import com.yoloroy.algorithm_mod.MutableBinaryGraphTree
import presentation.component.tree_view.TreeView
import com.yoloroy.algorithm_mod.MutableBinaryGraphTree.Node as MutableNode

@Composable
fun TreeEditor(
    tree: BinaryGraphTree<Int>,
    dialogData: NodeEditDialogData,
    addNode: (parent: Node<Int>, leftRight: LeftRight) -> Unit,
    deleteNode: (node: Node<Int>) -> Unit,
    editNode: (node: Node<Int>) -> Unit,
    swapNodeWithParent: (node: Node<Int>) -> Unit,
    swapNodeWithLeftChild: (node: Node<Int>) -> Unit,
    swapNodeWithRightChild: (node: Node<Int>) -> Unit,
    swapNodeChildren: (node: Node<Int>) -> Unit,
    moveNodeRight: (node: Node<Int>) -> Unit,
    moveNodeLeft: (node: Node<Int>) -> Unit
) {
    var editNodeMenuState by remember { mutableStateOf<EditNodeMenuState>(EditNodeMenuState.Hidden) }
    val node = (editNodeMenuState as? EditNodeMenuState.Expanded)?.node
    EditNodeMenu(
        state = editNodeMenuState,
        onDismissRequest = { editNodeMenuState = EditNodeMenuState.Hidden },
        onClickDelete = { deleteNode(node!!) },
        onClickEdit = { editNode(node!!) },
        onClickSwapWithParent = { swapNodeWithParent(node!!) },
        onClickSwapWithLeftChild = { swapNodeWithLeftChild(node!!) },
        onClickSwapWithRightChild = { swapNodeWithRightChild(node!!) },
        onClickSwapChildren = { swapNodeChildren(node!!) },
        onClickMoveRight = { moveNodeRight(node!!) },
        onClickMoveLeft = { moveNodeLeft(node!!) }
    )
    TreeView(
        tree = tree,
        addNode = addNode,
        onClickNode = { editNodeMenuState = EditNodeMenuState.Expanded(node = it) }
    )
    NodeEditDialog(dialogData = dialogData)
}

fun main() = singleWindowApplication {
    var tree by remember { mutableStateOf(
        MutableBinaryGraphTree<Int>().apply {
            root = MutableNode(
                value = 4,
                left = MutableNode(
                    value = 2,
                    left = MutableNode(value = 1),
                    right = MutableNode(value = 3)
                ),
                right = MutableNode(
                    value = 6,
                    left = MutableNode(value = 5)
                )
            )
        }
    )}
    var dialogData by remember { mutableStateOf(NodeEditDialogData(false, "", "", "", null, {}, {}, {})) }
    remember {
        dialogData = dialogData.copy(
            onClickDismiss = { dialogData = dialogData.copy(open = false) },
            updateValue = { text ->
                dialogData = dialogData.copy(
                    inputText = text,
                    isOk = text.takeIf(String::isNotEmpty)?.all(Char::isDigit)
                )
            }
        )
    }
    val updateTree = { tree = MutableBinaryGraphTree<Int>().apply { root = tree.root } }
    TreeEditor(
        tree = tree,
        dialogData = dialogData,
        addNode = { parent, leftRight ->
            parent as MutableNode
            dialogData = dialogData.copy(
                open = true,
                title = "Добавить узел дерева",
                label = "Значение в узле",
                onClickOk = {
                    when (leftRight) {
                        LeftRight.Left -> parent.setLeftAndAssignParent(MutableNode(dialogData.inputText.toInt()))
                        LeftRight.Right -> parent.setRightAndAssignParent(MutableNode(dialogData.inputText.toInt()))
                    }
                    updateTree()
                    dialogData = dialogData.copy(open = false)
                }
            )
        },
        deleteNode = { node -> (node as MutableNode).removeFromParent(); updateTree() },
        editNode = { node ->
            node as MutableNode
            dialogData = dialogData.copy(
                open = true,
                title = "Изменить значение в узле дерева",
                label = "Значение в узле",
                onClickOk = {
                    node.value = dialogData.inputText.toInt()
                    updateTree()
                    dialogData = dialogData.copy(open = false)
                }
            )
        },
        swapNodeWithParent = { node ->
            node as MutableNode
            node.parent?.value = node.value.also { node.value = node.parent!!.value }
            updateTree()
        },
        swapNodeWithLeftChild = { node ->
            node as MutableNode
            node.left?.value = node.value.also { node.value = node.left!!.value }
            updateTree()
        },
        swapNodeWithRightChild = { node ->
            node as MutableNode
            node.right?.value = node.value.also { node.value = node.right!!.value }
            updateTree()
        },
        swapNodeChildren = { node ->
            node as MutableNode
            node.left = node.right.also { node.right = node.left }
            updateTree()
        },
        moveNodeRight = { node ->
            node as MutableNode
            node.parent?.replaceChildAndAssignParent(node, node.left)
            node.left!!.right = node
            node.parent = node.left!!
            node.left = null
            updateTree()
        },
        moveNodeLeft = { node ->
            node as MutableNode
            node.parent?.replaceChildAndAssignParent(node, node.right)
            node.right!!.left = node
            node.parent = node.right!!
            node.right = null
            updateTree()
        }
    )
}
