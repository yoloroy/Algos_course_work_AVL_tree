@file:Suppress("LocalVariableName")

package com.yoloroy.algorithm_mod

import com.yoloroy.algorithm_mod.buildBinaryGraphTree
import com.yoloroy.algorithm_mod.forEach
import com.yoloroy.algorithm_mod.MutableBinaryGraphTree.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class BinaryGraphTreeTest {

    @Test
    fun forEachNode() {
        run {
            val tree = buildBinaryGraphTree {
                root = Node(
                    value = 4,
                    left = Node(
                        value = 2,
                        left = Node(value = 1),
                        right = Node(value = 3)
                    ),
                    right = Node(
                        value = 6,
                        left = Node(value = 5),
                        right = Node(value = 7)
                    )
                )
            }

            val list = mutableListOf<Int>()
            tree.forEach { value ->
                list += value
            }

            assertEquals("1,2,3,4,5,6,7", list.joinToString(","))
        }
        run {
            val tree = buildBinaryGraphTree {
                root = Node(
                    value = 67,
                    left = Node(value = 39),
                    right = Node(
                        value = 90,
                        left = Node(value = 85),
                        right = Node(value = 100)
                    )
                )
            }

            val list = mutableListOf<Int>()
            tree.forEach { value ->
                list += value
            }

            assertEquals("39,67,85,90,100", list.joinToString(","))
        }
    }
}
