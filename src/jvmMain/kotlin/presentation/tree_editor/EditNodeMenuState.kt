package presentation.tree_editor

import com.yoloroy.algorithm_mod.BinaryGraphTree

sealed class EditNodeMenuState {

    data class Expanded(
        val node: BinaryGraphTree.Node<Int>
    ) : EditNodeMenuState()

    object Hidden : EditNodeMenuState()
}
