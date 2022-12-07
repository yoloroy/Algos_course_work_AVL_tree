package presentation.component.tree_view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yoloroy.algorithm_mod.BinaryGraphTree
import com.yoloroy.algorithm_mod.size
import com.yoloroy.algorithm_mod.sizeWithPotentialChildren
import presentation.util.toPx

@Composable
fun TreeView(
    tree: BinaryGraphTree<Int>,
    addNode: ((parent: BinaryGraphTree.Node<Int>, leftRight: BinaryGraphTree.LeftRight) -> Unit)?,
    onClickNode: (BinaryGraphTree.Node<Int>) -> Unit
) {
    val showNodeAdding = remember(tree, addNode) { addNode != null }
    val shownChildrenCount by remember(tree) {
        derivedStateOf { if (showNodeAdding) tree.sizeWithPotentialChildren else tree.size }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .horizontalScroll(rememberScrollState())
    ) {
        var nodeOffsets by remember(tree) { mutableStateOf(listOf<Offset?>()) }
        val isNodesPlacingFinished by remember(tree) {
            derivedStateOf { shownChildrenCount == nodeOffsets.filterNotNull().size }
        }

        Lines(
            isNodesPlacingFinished = isNodesPlacingFinished,
            nodeOffsets = nodeOffsets
        )
        Subtree(
            node = tree.root,
            addNode = addNode,
            onClickNode = onClickNode,
            nodeOffsetCallback = { if (!isNodesPlacingFinished) nodeOffsets += it },
            showNodeAdding = showNodeAdding
        )
    }
}

@Composable
private fun Lines(
    isNodesPlacingFinished: Boolean,
    nodeOffsets: List<Offset?>
) {
    if (!isNodesPlacingFinished) return

    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {
        val parentsStack = mutableListOf(nodeOffsets.first()!!)
        for (offset in nodeOffsets) {
            val parentOffset = parentsStack.removeLast()
            drawLine(
                color = Color.Black.copy(alpha = 0.4f),
                start = parentOffset,
                end = offset ?: continue,
                strokeWidth = 3f
            )
            parentsStack += offset // parent's offset for left child
            parentsStack += offset // parent's offset for right child
        }
    }
}

// TODO custom layout for tree, this nodes placement is slow
@Composable
private fun Subtree(
    node: BinaryGraphTree.Node<Int>?,
    addNode: ((parent: BinaryGraphTree.Node<Int>, leftRight: BinaryGraphTree.LeftRight) -> Unit)?,
    onClickAddNode: (() -> Unit)? = null,
    onClickNode: (node: BinaryGraphTree.Node<Int>) -> Unit = {},
    nodeOffsetCallback: (Offset?) -> Unit,
    showNodeAdding: Boolean
) {
    val nodeSize = 42.dp
    val nodeSizePx = nodeSize.toPx()

    fun onNodePlaced(coordinates: LayoutCoordinates) {
        if (node == null && !showNodeAdding) {
            nodeOffsetCallback(null)
            return
        }
        nodeOffsetCallback(coordinates.positionInWindow() + Offset(x = coordinates.size.width / 2f, y = nodeSizePx / 2))
        if (node == null) {
            nodeOffsetCallback(null)
            nodeOffsetCallback(null)
        }
    }

    Column(
        modifier = Modifier
            .padding(top = 48.dp)
            .widthIn(min = 96.dp)
            .width(IntrinsicSize.Max)
            .onPlaced(::onNodePlaced),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (node != null) {
            Node(onClickNode, node, nodeSize)
            Children(node, addNode, onClickNode, nodeOffsetCallback, showNodeAdding)
        }
        else if (showNodeAdding) {
            NodeAddingLeaf(onClickAddNode, nodeSize)
        }
    }
}

@Composable
private fun Node(
    onClickNode: (node: BinaryGraphTree.Node<Int>) -> Unit,
    node: BinaryGraphTree.Node<Int>,
    nodeSize: Dp
) {
    Button(
        onClick = { onClickNode(node) },
        modifier = Modifier.defaultMinSize(minWidth = nodeSize, minHeight = nodeSize),
        shape = CircleShape,
        colors = buttonColors(
            backgroundColor = MaterialTheme.colors.surface,
            contentColor = MaterialTheme.colors.primary
        )
    ) {
        Text(text = node.value.toString())
    }
}

@Composable
private fun Children(
    node: BinaryGraphTree.Node<Int>,
    addNode: ((parent: BinaryGraphTree.Node<Int>, leftRight: BinaryGraphTree.LeftRight) -> Unit)?,
    onClickNode: (node: BinaryGraphTree.Node<Int>) -> Unit,
    nodeOffsetCallback: (Offset?) -> Unit,
    showNodeAdding: Boolean
) {
    Row(
        modifier = Modifier.wrapContentWidth().padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Subtree(
            node = node.left,
            addNode = addNode,
            onClickAddNode = addNode?.let { { addNode(node, BinaryGraphTree.LeftRight.Left) } },
            onClickNode = onClickNode,
            nodeOffsetCallback = nodeOffsetCallback,
            showNodeAdding = showNodeAdding
        )
        Subtree(
            node = node.right,
            addNode = addNode,
            onClickAddNode = addNode?.let { { addNode(node, BinaryGraphTree.LeftRight.Right) } },
            onClickNode = onClickNode,
            nodeOffsetCallback = nodeOffsetCallback,
            showNodeAdding = showNodeAdding
        )
    }
}

@Composable
private fun NodeAddingLeaf(onClickAddNode: (() -> Unit)?, nodeSize: Dp) {
    Button(
        onClick = onClickAddNode!!,
        modifier = Modifier.defaultMinSize(minWidth = nodeSize, minHeight = nodeSize),
        shape = CircleShape,
        colors = buttonColors(
            backgroundColor = MaterialTheme.colors.surface,
            contentColor = MaterialTheme.colors.primary
        )
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add"
        )
    }
}
