package com.yoloroy.algorithm_mod

import com.yoloroy.algorithm_mod.AvlTree.AvlData
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import kotlin.test.assertContentEquals
import com.yoloroy.algorithm_mod.MutableBinaryGraphTree.Node as MutableNode

internal class AvlTreeTest {

    @Test
    fun constructor() {
        run {
            val tree = AvlTree(listOf(1, 2, 3, 4))
            println(indentationTreePrint(tree.tree.root!!))
            assertEquals("/1, 2, 3, 4\\", tree.toString())
            assertEquals(3, tree.tree.root!!.height)
            assertTreeHeightsCorrectness(tree)
        }
        run {
            val tree = AvlTree(listOf(5, 4, 3, 2))
            println(indentationTreePrint(tree.tree.root!!))
            assertEquals("/2, 3, 4, 5\\", tree.toString())
            assertEquals(3, tree.tree.root!!.height)
            assertTreeHeightsCorrectness(tree)
        }
        run {
            val tree = AvlTree(listOf(2, 3, 3, 4, 1, 5, 5, 1, 5))
            println(indentationTreePrint(tree.tree.root!!))
            assertEquals("/1, 2, 3, 4, 5\\", tree.toString())
            assertEquals(3, tree.tree.root!!.height)
            assertTreeHeightsCorrectness(tree)
        }
        run {
            val tree = AvlTree(listOf(10, 3, 2, 6, 7, 11, 38, 34, 26, 27, 29, 34, 38, 14, 13, 17, 28, 24, 1, 6, 4, 29, 7, 21, 23, 28, 35, 19, 10, 1, 5, 15, 34, 36, 0, 17, 1, 1, 1, 15, 35, 10, 31, 24, 35, 30, 3, 24, 31, 4, 4, 37, 36, 3, 24, 12, 11, 33, 4, 26, 27, 6, 24, 16, 5, 37, 20, 27, 16, 4, 28, 6, 23, 17, 30, 3, 35, 27, 17, 23, 31, 17, 28, 12, 9, 17, 25, 26, 29, 14, 36, 6, 7, 31, 5, 15, 23, 28, 12, 31, 23, 12, 29, 21, 38, 26, 16, 24, 27, 7, 28, 37, 7, 18, 10, 32, 39, 21, 3, 35, 8, 10, 1, 22, 26, 10, 8, 38, 37, 9, 36, 29, 9, 13, 9, 18, 4, 21, 3, 36, 15, 20, 12, 20, 29, 26, 32, 34, 12, 14, 8, 7, 34, 32))
            println(indentationTreePrint(tree.tree.root!!))
            assertEquals("/0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39\\", tree.toString())
            assertEquals(6, tree.tree.root!!.height)
            assertTreeHeightsCorrectness(tree)
        }
    }

    @Test
    fun insertValue() {
        val tree = AvlTree(listOf(1, 2, 5))
        tree.insertValue(0)
        assertEquals("/0, 1, 2, 5\\", tree.toString())
        assertTreeHeightsCorrectness(tree)
        tree.insertValue(3)
        assertEquals("/0, 1, 2, 3, 5\\", tree.toString())
        assertTreeHeightsCorrectness(tree)
        tree.insertValue(5)
        assertEquals("/0, 1, 2, 3, 5\\", tree.toString())
        assertTreeHeightsCorrectness(tree)
        tree.insertValue(4)
        assertEquals("/0, 1, 2, 3, 4, 5\\", tree.toString())
        assertTreeHeightsCorrectness(tree)
        tree.insertValue(6)
        assertEquals("/0, 1, 2, 3, 4, 5, 6\\", tree.toString())
        assertTreeHeightsCorrectness(tree)
    }

    @Test
    fun fancyInsertValue() {
        val tree = AvlTree(listOf(-75, -50, -25, 0, 25, 50, 75))
        assertTreeEquals(tree, buildBinaryGraphTree {
            root = MutableNode(
                value = AvlData(0, 3),
                left = MutableNode(
                    value = AvlData(-50, 2),
                    left = MutableNode(value = AvlData(-75, 1)),
                    right = MutableNode(value = AvlData(-25, 1))
                ),
                right = MutableNode(
                    value = AvlData(50, 2),
                    left = MutableNode(value = AvlData(25, 1)),
                    right = MutableNode(value = AvlData(75, 1))
                )
            )
        })
        assertTreeHeightsCorrectness(tree)

        tree.insertValue(40)
        assertTreeEquals(tree, buildBinaryGraphTree {
            root = MutableNode(
                value = AvlData(0, 4),
                left = MutableNode(
                    value = AvlData(-50, 2),
                    left = MutableNode(value = AvlData(-75, 1)),
                    right = MutableNode(value = AvlData(-25, 1))
                ),
                right = MutableNode(
                    value = AvlData(50, 3),
                    left = MutableNode(
                        value = AvlData(25, 2),
                        right = MutableNode(value = AvlData(40, 1))
                    ),
                    right = MutableNode(value = AvlData(75, 1))
                )
            )
        })
        assertTreeHeightsCorrectness(tree)

        tree.insertValue(35)
        assertTreeEquals(tree, buildBinaryGraphTree {
            root = MutableNode(
                value = AvlData(0, 4),
                left = MutableNode(
                    value = AvlData(-50, 2),
                    left = MutableNode(value = AvlData(-75, 1)),
                    right = MutableNode(value = AvlData(-25, 1))
                ),
                right = MutableNode(
                    value = AvlData(50, 3),
                    left = MutableNode(
                        value = AvlData(35, 2),
                        left = MutableNode(value = AvlData(25, 1)),
                        right = MutableNode(value = AvlData(40, 1))
                    ),
                    right = MutableNode(value = AvlData(75, 1))
                )
            )
        })
        assertTreeHeightsCorrectness(tree)
    }

    @Test
    fun removeValue() {
        val tree = AvlTree(listOf(1, 2, 3, 4, 5))
        tree.removeValue(4)
        assertEquals("/1, 2, 3, 5\\", tree.toString())
        assertEquals(3, tree.tree.root!!.height)
        assertTreeHeightsCorrectness(tree)
        tree.removeValue(2)
        assertTreeEquals(tree, buildBinaryGraphTree {
            root = MutableNode(
                value = AvlData(3, 2),
                left = MutableNode(AvlData(1, 1)),
                right = MutableNode(AvlData(5, 1))
            )
        })
        assertTreeHeightsCorrectness(tree)
        tree.removeValue(1)
        assertTreeHeightsCorrectness(tree)
        tree.removeValue(3)
        assertTreeHeightsCorrectness(tree)
        assertEquals(5, tree.tree.root!!.value.value)
        assertEquals(1, tree.tree.root!!.height)
    }

    @Test
    fun replaceValue() {
        // it is just inserting and removing of some value, there nothing specific here
    }

    private fun <T : Comparable<T>> assertTreeEquals(avlTree: AvlTree<T>, trueTree: BinaryGraphTree<AvlData<T>>) {
        val parallelNodes = avlTree.tree.preOrderNodeSequence() zip trueTree.preOrderNodeSequence()
        val errors = parallelNodes.filter { (actual, expected) ->
            val valueAreNotEqual = actual.value != expected.value
            val anyOfChildrenConnectionsAreNotTheSame = actual.children.zip(expected.children).any { (a, b) -> a.value != b.value }

            valueAreNotEqual || anyOfChildrenConnectionsAreNotTheSame
        }
        assertContentEquals(
            emptySequence(),
            errors,
            errors.joinToString(
                separator = ",\n",
                prefix = "bad nodes: [\n",
                postfix = "\n]\n"
            ) { (actual, expected) ->
                "\t(\n\t\tactual: $actual\n\t\texpected: $expected\n\t)"
            }
        )
        assertEquals(indentationTreePrint(trueTree.root!!), indentationTreePrint(avlTree.tree.root!!))
    }

    private fun <T : Comparable<T>> assertTreeHeightsCorrectness(avlTree: AvlTree<T>) {
        avlTree.tree.preOrderNodeSequence().forEach { node ->
            assertTrue(node.height == ((node.children.maxOfOrNull { it.height } ?: 0) + 1))
        }
    }
}
