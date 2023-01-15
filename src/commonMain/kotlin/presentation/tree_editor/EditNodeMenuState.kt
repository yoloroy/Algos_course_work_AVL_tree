package presentation.tree_editor

import com.yoloroy.algorithm_mod.BinaryGraphTree

sealed class EditNodeMenuState<T> {

    data class Expanded<T>(
        val node: BinaryGraphTree.Node<T>,
        val nodeName: String
    ) : EditNodeMenuState<T>()

    data class ExpandedWithNoNode<T>(val label: String) : EditNodeMenuState<T>()

    class Hidden<T> : EditNodeMenuState<T>()
}
