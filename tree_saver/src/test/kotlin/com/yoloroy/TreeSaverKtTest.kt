package com.yoloroy

import com.yoloroy.algorithm_mod.MutableBinaryGraphTree
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import com.yoloroy.algorithm_mod.MutableBinaryGraphTree.Node as MutableNode

internal class TreeSaverKtTest {

    @Test
    fun toTreeData() {
        run {
            val tree = MutableBinaryGraphTree<Int>().apply {
                root = MutableNode(1).apply {
                    left = MutableNode(0)
                    right = MutableNode(2).apply {
                        right = MutableNode(3)
                    }
                }
            }
            val expectedTreeData = TreeData(
                namesValues = mapOf(
                    "n0" to 0,
                    "n1" to 1,
                    "n2" to 2,
                    "n3" to 3
                ),
                associations = mapOf(
                    "n0" to listOf(null, null),
                    "n1" to listOf("n0", "n2"),
                    "n2" to listOf(null, "n3"),
                    "n3" to listOf(null, null)
                )
            )
            assertEquals(expectedTreeData, tree.toTreeData())
        }
        run {
            val tree = MutableBinaryGraphTree<Int>()
            val expectedTreeData = TreeData(emptyMap(), emptyMap())
            assertEquals(expectedTreeData, tree.toTreeData())
        }
    }

    @Test
    fun toTree() {
        run {
            val treeData = TreeData(
                namesValues = mapOf(
                    "n0" to 0,
                    "n1" to 1,
                    "n2" to 2,
                    "n3" to 3
                ),
                associations = mapOf(
                    "n0" to listOf(null, null),
                    "n1" to listOf("n0", "n2"),
                    "n2" to listOf(null, "n3"),
                    "n3" to listOf(null, null)
                )
            )
            assertEquals(treeData, treeData.toTree().toTreeData())
        }
        run {
            val treeData = TreeData(emptyMap(), emptyMap())
            val expectedTree = MutableBinaryGraphTree<Int>()
            assertEquals(expectedTree.toTreeData(), treeData.toTree().toTreeData())
        }
    }
}
