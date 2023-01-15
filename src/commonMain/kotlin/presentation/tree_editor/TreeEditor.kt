package presentation.tree_editor

import androidx.compose.runtime.*
import com.yoloroy.algorithm_mod.BinaryGraphTree
import com.yoloroy.algorithm_mod.BinaryGraphTree.LeftRight
import com.yoloroy.algorithm_mod.BinaryGraphTree.Node
import presentation.component.tree_view.TreeView

@Composable
fun TreeEditor(
    tree: BinaryGraphTree<Int>,
    dialogData: NodeEditDialogData,
    addNode: (parent: Node<Int>, leftRight: LeftRight) -> Unit,
    deleteNode: (node: Node<Int>) -> Unit,
    editNode: (node: Node<Int>) -> Unit,
    rotateLeftLeft: (node: Node<Int>) -> Unit,
    rotateLeftRight: (node: Node<Int>) -> Unit,
    rotateRightLeft: (node: Node<Int>) -> Unit,
    rotateRightRight: (node: Node<Int>) -> Unit,
) {
    var editNodeMenuState: EditNodeMenuState<Int> by remember { mutableStateOf(EditNodeMenuState.Hidden()) }
    val node = (editNodeMenuState as? EditNodeMenuState.Expanded)?.node
    EditNodeMenu(
        state = editNodeMenuState,
        onDismissRequest = { editNodeMenuState = EditNodeMenuState.Hidden() },
        menuItems = listOfNotNull(
            EditNodeMenuItem(
                name = "Удалить узел",
                onClick = { deleteNode(node!!) }
            ).takeIf {
                 node?.parent != null
            },
            EditNodeMenuItem(
                name = "Изменить значение",
                onClick = { editNode(node!!) }
            ),
            EditNodeMenuItem(
                name = "Осуществить LL-Поворот",
                onClick = { rotateLeftLeft(node!!) }
            ).takeIf {
                node?.left?.left != null
            },
            EditNodeMenuItem(
                name = "Осуществить LR-Поворот",
                onClick = { rotateLeftRight(node!!) }
            ).takeIf {
                node?.left?.right != null
            },
            EditNodeMenuItem(
                name = "Осуществить RL-Поворот",
                onClick = { rotateRightLeft(node!!) }
            ).takeIf {
                node?.right?.left != null
            },
            EditNodeMenuItem(
                name = "Осуществить RR-Поворот",
                onClick = { rotateRightRight(node!!) }
            ).takeIf {
                node?.right?.right != null
            }
        )
    )
    TreeView(
        tree = tree,
        addNode = { parent, leftRight -> addNode(parent!!, leftRight!!) },
        onClickNode = { editNodeMenuState = EditNodeMenuState.Expanded(node = it, nodeName = it.value.toString()) },
        nodeValueToString = Int::toString
    )
    NodeEditDialog(dialogData = dialogData)
}
