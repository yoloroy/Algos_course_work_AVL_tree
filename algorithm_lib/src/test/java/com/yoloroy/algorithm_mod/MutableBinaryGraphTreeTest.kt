package com.yoloroy.algorithm_mod

import com.yoloroy.algorithm_mod.MutableBinaryGraphTree
import com.yoloroy.algorithm_mod.MutableBinaryGraphTree.Node
import org.junit.jupiter.api.Test

internal class MutableBinaryGraphTreeTest {

    @Test
    fun getRoot() {
    }

    @Test
    fun rotateLeftLeft() {
        val tree = MutableBinaryGraphTree(100).apply {
            root!!.apply {
                createChildOnLeft(50).apply {
                    createChildOnLeft(25).apply {
                        createChildOnLeft(12) // because of this we must rotate
                    }
                    createChildOnRight(75)
                }
                createChildOnRight(150)
            }
            rotateLeftLeft(root!!)
        }
        val expectedTree = MutableBinaryGraphTree(50).apply {
            root!!.apply {
                createChildOnLeft(25).createChildOnLeft(12)
                createChildOnRight(100).apply {
                    createChildOnLeft(75)
                    createChildOnRight(150)
                }
            }
        }
        assertTreeEquals(expectedTree, tree)
    }

    @Test
    fun rotateRightRight() {
        val tree = MutableBinaryGraphTree(67).apply {
            val critical: Node<Int>
            root!!.apply {
                createChildOnLeft(39)
                critical = createChildOnRight(85).apply {
                    createChildOnRight(90).apply {
                        createChildOnRight(100)
                    }
                }
            }
            rotateRightRight(critical)
        }
        val expectedTree = MutableBinaryGraphTree(67).apply {
            root!!.apply {
                createChildOnLeft(39)
                createChildOnRight(90).apply {
                    createChildOnLeft(85)
                    createChildOnRight(100)
                }
            }
        }
        assertTreeEquals(expectedTree, tree)
    }

    @Test
    fun rotateRightLeft() {
        val tree = MutableBinaryGraphTree(90).apply {
            val critical = root!!.apply {
                createChildOnLeft(68)
                createChildOnRight(105).apply {
                    createChildOnLeft(96).apply {
                        createChildOnLeft(92)
                    }
                    createChildOnRight(110)
                }
            }
            rotateRightLeft(critical)
        }
        val expectedTree = MutableBinaryGraphTree(96).apply {
            root!!.apply {
                createChildOnLeft(90).apply {
                    createChildOnLeft(68)
                    createChildOnRight(92)
                }
                createChildOnRight(105).apply {
                    createChildOnRight(110)
                }
            }
        }
        assertTreeEquals(expectedTree, tree)
    }

    @Test
    fun rotateLeftRight() {
        val tree = MutableBinaryGraphTree(78).apply {
            val critical = root!!.apply {
                createChildOnLeft(67).apply {
                    createChildOnLeft(50)
                    createChildOnRight(75).apply {
                        createChildOnLeft(70)
                    }
                }
                createChildOnRight(90)
            }
            rotateLeftRight(critical)
        }
        val expectedTree = MutableBinaryGraphTree(75).apply {
            root!!.apply {
                createChildOnLeft(67).apply {
                    createChildOnLeft(50)
                    createChildOnRight(70)
                }
                createChildOnRight(78).apply {
                    createChildOnRight(90)
                }
            }
        }
        assertTreeEquals(expectedTree, tree)
    }

    @Test
    fun findValue() {
    }

    @Test
    fun get() {
    }

    @Test
    fun getNode() {
    }
}