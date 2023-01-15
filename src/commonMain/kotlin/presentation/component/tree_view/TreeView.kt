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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import com.yoloroy.algorithm_mod.BinaryGraphTree
import com.yoloroy.algorithm_mod.BinaryGraphTree.*
import presentation.util.toPx

@Composable
fun <T> TreeView(
    tree: BinaryGraphTree<T>,
    addNode: ((parent: Node<T>?, leftRight: LeftRight?) -> Unit)?,
    onClickNode: (Node<T>) -> Unit,
    nodeValueToString: (T) -> String
) {
    val nodeSize = 48.dp
    val nodeSizePx = with (LocalDensity.current) { nodeSize.toPx() }

    val nodesInRows = tree.root
        ?.let { formIntoRows(it, addNode != null) }
        ?: listOf(listOf(FormingNode.AddingNodeLeaf(null, null)))

    var scrollableBoxWidthPx by remember { mutableStateOf(0) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .onPlaced { scrollableBoxWidthPx = it.size.width }
            .verticalScroll(rememberScrollState())
            .horizontalScroll(rememberScrollState())
    ) {
        var boxWidth by remember { mutableStateOf(0f) }
        Box(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.Center)
                .onPlaced { boxWidth = it.size.width.toFloat() }
        ) {
            Lines(nodesInRows = nodesInRows, nodeSizePx = nodeSizePx, width = boxWidth)
            Nodes(
                addNode = addNode,
                onClickNode = onClickNode,
                nodeSize = nodeSize,
                nodesInRows = nodesInRows,
                nodeValueToString = nodeValueToString,
                scrollableBoxWidth = with (LocalDensity.current) { scrollableBoxWidthPx.toDp() }
            )
        }
    }
}

@Composable
private fun <T> Lines(nodesInRows: List<List<FormingNode<T>>>, nodeSizePx: Float, width: Float) {

    fun nextRowOffsets(lastRowOffsets: List<Offset>) = lastRowOffsets
        .flatMap { offset ->
            val childXOffset = width / lastRowOffsets.size / 4
            val y = offset.y + nodeSizePx * 2f
            listOf(
                offset.copy(offset.x - childXOffset, y),
                offset.copy(offset.x + childXOffset, y)
            )
        }

    fun combineParentOffsetsAndChildrenData(
        parentOffsets: List<Offset>,
        nextRow: List<FormingNode<T>>,
        nextOffsets: List<Offset>
    ) = parentOffsets.asSequence() zip nextRow.asSequence().zip(nextOffsets.asSequence()).windowed(2, 2)

    fun DrawScope.drawLinesToChildren(
        parentOffset: Offset,
        children: List<Pair<FormingNode<T>, Offset>>
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
        formingRow: List<FormingNode<T>>
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
        var lastRowOffsets = listOf(Offset(width / 2, nodeSizePx))
        for (formingRow in nodesInRows.drop(1)) {
            lastRowOffsets = drawRowAndGetNextOffsets(lastRowOffsets, formingRow)
        }
    }
}

@Composable
private fun <T> Nodes(
    addNode: ((parent: Node<T>?, leftRight: LeftRight?) -> Unit)?,
    onClickNode: (node: Node<T>) -> Unit = {},
    nodeSize: Dp,
    nodesInRows: List<List<FormingNode<T>>>,
    nodeValueToString: (T) -> String,
    scrollableBoxWidth: Dp
) {
    Column(
        modifier = Modifier
            .widthIn(min = max(scrollableBoxWidth, 96.dp))
            .offset(y = nodeSize / 2)
            .width(IntrinsicSize.Max)
            .wrapContentHeight()
            .padding(bottom = nodeSize),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(nodeSize, Alignment.CenterVertically)
    ) {
        for (formingNodes in nodesInRows) {
            Row(horizontalArrangement = Arrangement.Center) {
                for (formingNode in formingNodes) {
                    FormedNode(nodeSize, formingNode, onClickNode, addNode, nodeValueToString)
                }
            }
        }
    }
}

@Composable
private fun <T> RowScope.FormedNode(
    nodeSize: Dp,
    formingNode: FormingNode<T>,
    onClickNode: (node: Node<T>) -> Unit,
    addNode: ((parent: Node<T>?, leftRight: LeftRight?) -> Unit)?,
    nodeValueToString: (T) -> String
) {
    Box(
        modifier = Modifier.weight(1f).padding(horizontal = nodeSize / 2),
        contentAlignment = Alignment.Center
    ) {
        when (formingNode) {
            is FormingNode.Presented -> Node(
                onClickNode = onClickNode,
                node = formingNode.node,
                nodeSize = nodeSize,
                nodeValueToString = nodeValueToString
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

private fun <T> formIntoRows(root: Node<T>, nodeAdding: Boolean): List<List<FormingNode<T>>> {

    fun formingChild(formingNode: FormingNode.Presented<T>, leftRight: LeftRight) =
        formingNode.node[leftRight]
            ?.let { FormingNode.Presented(it) }
            ?: FormingNode.AddingNodeLeaf(formingNode.node, leftRight).takeIf { nodeAdding }
            ?: FormingNode.Nothing()

    fun formingChildren(formingNode: FormingNode<T>): List<FormingNode<T>> = when (formingNode) {
        is FormingNode.Presented -> listOf(
            formingChild(formingNode, LeftRight.Left),
            formingChild(formingNode, LeftRight.Right)
        )
        is FormingNode.AddingNodeLeaf,
        is FormingNode.Nothing -> listOf(FormingNode.Nothing(), FormingNode.Nothing())
    }

    val treeAsRows = mutableListOf(listOf<FormingNode<T>>(FormingNode.Presented(root)))

    while (treeAsRows.last().any { it is FormingNode.Presented<T> }) {
        treeAsRows += treeAsRows.last().flatMap(::formingChildren)
    }

    return treeAsRows.toList()
}

sealed class FormingNode<T> {

    class Presented<T>(val node: Node<T>) : FormingNode<T>()

    class AddingNodeLeaf<T>(
        val parent: Node<T>?,
        val leftRight: LeftRight?
    ) : FormingNode<T>()

    class Nothing<T> : FormingNode<T>()
}

@Composable
private fun <T> Node(
    onClickNode: (node: Node<T>) -> Unit,
    node: Node<T>,
    nodeSize: Dp,
    nodeValueToString: (T) -> String
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
        Text(text = nodeValueToString(node.value), fontSize = 16.sp)
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
