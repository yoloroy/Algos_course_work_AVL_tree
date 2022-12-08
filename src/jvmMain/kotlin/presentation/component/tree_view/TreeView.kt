package presentation.component.tree_view

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yoloroy.algorithm_mod.BinaryGraphTree
import com.yoloroy.algorithm_mod.BinaryGraphTree.*
import presentation.util.toPx

@Composable
fun TreeView(
    tree: BinaryGraphTree<Int>,
    addNode: ((parent: Node<Int>, leftRight: LeftRight) -> Unit)?,
    onClickNode: (Node<Int>) -> Unit
) {
    val nodeSize = 42.dp
    val nodeSizePx = nodeSize.toPx()

    val nodesInRows = formIntoRows(tree.root ?: TODO(), addNode != null)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .horizontalScroll(rememberScrollState())
    ) {
        var boxWidth by remember { mutableStateOf(0f) }
        Box(
            modifier = Modifier
                .wrapContentSize()
                .onPlaced { boxWidth = it.size.width.toFloat() }
        ) {
            Lines(nodesInRows = nodesInRows, nodeSizePx = nodeSizePx, width = boxWidth)
            Nodes(
                addNode = addNode,
                onClickNode = onClickNode,
                nodeSize = nodeSize,
                nodesInRows = nodesInRows
            )
        }
    }
}

@Composable
private fun Lines(nodesInRows: List<List<FormingNode>>, nodeSizePx: Float, width: Float) {

    fun nextRowOffsets(lastRowOffsets: List<Offset>) = lastRowOffsets
        .flatMap { offset ->
            val childXOffset = width / lastRowOffsets.size / 4
            val y = offset.y + nodeSizePx * 2.125f
            listOf(
                offset.copy(offset.x - childXOffset, y),
                offset.copy(offset.x + childXOffset, y)
            )
        }

    fun combineParentOffsetsAndChildrenData(
        parentOffsets: List<Offset>,
        nextRow: List<FormingNode>,
        nextOffsets: List<Offset>
    ) = parentOffsets.asSequence() zip nextRow.asSequence().zip(nextOffsets.asSequence()).windowed(2, 2)

    fun DrawScope.drawLinesToChildren(
        parentOffset: Offset,
        children: List<Pair<FormingNode, Offset>>
    ) {
        for ((formingNode, childOffset) in children) {
            if (formingNode is FormingNode.Nothing) continue
            drawLine(
                color = Color.Black.copy(alpha = 0.4f),
                start = parentOffset,
                end = childOffset,
                strokeWidth = 3f
            )
        }
    }

    fun DrawScope.drawRowAndGetNextOffsets(
        lastRowOffsets: List<Offset>,
        formingRow: List<FormingNode>
    ): List<Offset> {
        val nextOffsets = nextRowOffsets(lastRowOffsets)
        val parentOffsetsAndChildrenData = combineParentOffsetsAndChildrenData(lastRowOffsets, formingRow, nextOffsets)
        for ((parentOffset, children) in parentOffsetsAndChildrenData) {
            drawLinesToChildren(parentOffset, children)
        }
        return nextOffsets
    }

    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {
        var lastRowOffsets = listOf(Offset(width / 2, nodeSizePx / 2))
        for (formingRow in nodesInRows.drop(1)) {
            lastRowOffsets = drawRowAndGetNextOffsets(lastRowOffsets, formingRow)
        }
    }
}

@Composable
private fun Nodes(
    addNode: ((parent: Node<Int>, leftRight: LeftRight) -> Unit)?,
    onClickNode: (node: Node<Int>) -> Unit = {},
    nodeSize: Dp,
    nodesInRows: List<List<FormingNode>>
) {
    Column(
        modifier = Modifier
            .widthIn(min = 96.dp)
            .width(IntrinsicSize.Max)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(nodeSize)
    ) {
        for (formingNodes in nodesInRows) {
            Row(horizontalArrangement = Arrangement.Center) {
                for (formingNode in formingNodes) {
                    FormedNode(nodeSize, formingNode, onClickNode, addNode)
                }
            }
        }
    }
}

@Composable
private fun RowScope.FormedNode(
    nodeSize: Dp,
    formingNode: FormingNode,
    onClickNode: (node: Node<Int>) -> Unit,
    addNode: ((parent: Node<Int>, leftRight: LeftRight) -> Unit)?
) {
    Box(
        modifier = Modifier.Companion.weight(1f).padding(horizontal = nodeSize / 2),
        contentAlignment = Alignment.Center
    ) {
        when (formingNode) {
            is FormingNode.Presented -> Node(
                onClickNode = onClickNode,
                node = formingNode.node,
                nodeSize = nodeSize
            )

            is FormingNode.Nothing -> Box(modifier = Modifier.size(nodeSize))
            is FormingNode.AddingNodeLeaf -> when (addNode) {
                null -> Box(modifier = Modifier.size(nodeSize))
                else -> NodeAddingLeaf(
                    onClickAddNode = { addNode(formingNode.parent, formingNode.leftRight) },
                    nodeSize = nodeSize
                )
            }
        }
    }
}

private fun formIntoRows(root: Node<Int>, nodeAdding: Boolean): List<List<FormingNode>> {

    fun formingChild(formingNode: FormingNode.Presented, leftRight: LeftRight) =
        formingNode.node[leftRight]
            ?.let(FormingNode::Presented)
            ?: FormingNode.AddingNodeLeaf(formingNode.node, leftRight).takeIf { nodeAdding }
            ?: FormingNode.Nothing

    fun formingChildren(formingNode: FormingNode) = when (formingNode) {
        is FormingNode.Presented -> listOf(
            formingChild(formingNode, LeftRight.Left),
            formingChild(formingNode, LeftRight.Right)
        )
        is FormingNode.AddingNodeLeaf,
        is FormingNode.Nothing -> listOf(FormingNode.Nothing, FormingNode.Nothing)
    }

    val treeAsRows = mutableListOf(listOf<FormingNode>(FormingNode.Presented(root)))

    while (treeAsRows.last().any { it is FormingNode.Presented }) {
        treeAsRows += treeAsRows.last().flatMap(::formingChildren)
    }

    return treeAsRows.toList()
}

sealed class FormingNode {

    class Presented(val node: Node<Int>) : FormingNode()

    class AddingNodeLeaf(
        val parent: Node<Int>,
        val leftRight: LeftRight
    ) : FormingNode()

    object Nothing : FormingNode()
}

@Composable
private fun Node(
    onClickNode: (node: Node<Int>) -> Unit,
    node: Node<Int>,
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
