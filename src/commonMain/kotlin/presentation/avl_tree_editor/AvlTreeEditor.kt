package presentation.avl_tree_editor

import StringRes
import androidx.compose.runtime.*
import com.yoloroy.algorithm_mod.AvlTree.AvlData
import com.yoloroy.algorithm_mod.BinaryGraphTree
import com.yoloroy.algorithm_mod.BinaryGraphTree.LeftRight
import com.yoloroy.algorithm_mod.BinaryGraphTree.Node
import presentation.component.tree_view.TreeView
import presentation.tree_editor.*

@Composable
fun AvlTreeEditor(
    tree: BinaryGraphTree<AvlData<Int>>,
    dialogData: NodeEditDialogData,
    insertNode: () -> Unit,
    deleteNode: (node: Node<AvlData<Int>>) -> Unit,
    editNode: (node: Node<AvlData<Int>>) -> Unit,
) {
    var editNodeMenuState: EditNodeMenuState<AvlData<Int>> by remember { mutableStateOf(EditNodeMenuState.Hidden()) }
    val node = (editNodeMenuState as? EditNodeMenuState.Expanded<AvlData<Int>>)?.node

    EditNodeMenu(
        state = editNodeMenuState,
        onDismissRequest = { editNodeMenuState = EditNodeMenuState.Hidden() },
        menuItems = listOfNotNull(
            EditNodeMenuItem(
                name = StringRes.ui.treeActions.avlTreeEditor.delete,
                onClick = { deleteNode(node!!) }
            ),
            EditNodeMenuItem(
                name = StringRes.ui.treeActions.avlTreeEditor.replace,
                onClick = { editNode(node!!) }
            ),
            EditNodeMenuItem(
                name = StringRes.ui.treeActions.avlTreeEditor.insert,
                onClick = insertNode
            )
        )
    )
    TreeView(
        tree = tree,
        addNode = { _: Node<AvlData<Int>>?, _: LeftRight? -> insertNode() }.takeIf { tree.root == null },
        onClickNode = { editNodeMenuState = EditNodeMenuState.Expanded(node = it, nodeName = "${it.value.value} h:${it.value.height}") },
        nodeValueToString = { (value, height) -> "$value h:$height" } // TODO format string res
    )
    NodeEditDialog(dialogData = dialogData)
}
