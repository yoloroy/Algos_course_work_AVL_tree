package com.yoloroy.algorithm_mod

import java.util.Stack

abstract class BinaryGraphTree<T> {

    abstract val root: Node<T>?

    fun findValue(predicate: (T) -> Boolean): ValueWithPath<T>? = root?.findValue(predicate)

    operator fun get(path: Path): T? = getNode(path)?.value

    fun getNode(path: Path): Node<T>? = path.fold(root) { currentNode, leftRight ->
        if (currentNode == null) {
            throw IllegalStateException("There no such move here")
        }
        currentNode[leftRight]
    }

    fun forEachNode(block: (Node<T>) -> Unit) {
        fun Node<T>.forEachNode() {
            left?.forEachNode()
            block(this)
            right?.forEachNode()
        }
        root?.forEachNode()
    }

    abstract class Node<T>(
        open val parent: Node<T>?,
        open val left: Node<T>?,
        open val right: Node<T>?,
        open val value: T
    ) {

        fun isLeaf() = left == null && right == null

        /**
         * recursive search, see loop variant in Binary graph tree default implementation in forEach
         */
        fun findValue(predicate: (T) -> Boolean): ValueWithPath<T>? {
            var result: T? = null
            val path = buildPath {
                if (predicate(value)) return@buildPath

                fun onFind(side: LeftRight) = { (path, value): ValueWithPath<T> ->
                    add(side)
                    addAll(path)
                    result = value
                }

                left?.findValue(predicate)?.let(onFind(LeftRight.Left))
                right?.findValue(predicate)?.let(onFind(LeftRight.Right))
            }
            return result?.let { ValueWithPath(path, it) }
        }

        open operator fun get(leftRight: LeftRight) = when (leftRight) {
            LeftRight.Left -> left
            LeftRight.Right -> right
        }

        open val children get() = listOfNotNull(left, right)

        fun directionOf(node: Node<T>) = directionOfOrNull(node) ?: throw IllegalArgumentException()

        fun directionOfOrNull(node: Node<T>) = when (node) {
            left -> LeftRight.Left
            right -> LeftRight.Right
            else -> null
        }
    }

    data class ValueWithPath<T>(val path: Path, val value: T)

    @JvmInline
    value class Path(private val path: List<LeftRight>) : Iterable<LeftRight> {

        companion object {
            private val EMPTY = Path(emptyList())

            fun empty() = EMPTY
        }

        fun endsWith(vararg path: LeftRight) = this.path.takeLast(path.size) == path.asList()

        fun popped() = Path(path.dropLast(1))

        operator fun plus(other: Path) = Path(path + other.path)

        operator fun plus(item: LeftRight) = Path(path + item)

        override fun iterator() = path.iterator()

        fun last() = path.last()
    }

    enum class LeftRight { Left, Right }
}

inline fun <T> BinaryGraphTree<T>.forEach(crossinline block: (T) -> Unit) = forEachNode { block(it.value) }

inline fun buildPath(block: MutableList<BinaryGraphTree.LeftRight>.() -> Unit) = BinaryGraphTree.Path(buildList(block))

inline fun <T> buildBinaryGraphTree(block: MutableBinaryGraphTree<T>.() -> Unit): BinaryGraphTree<T> = MutableBinaryGraphTree<T>().apply(block)

val BinaryGraphTree<*>.sizeWithPotentialChildren: Int get() {
    fun traversalWithNulls(node: BinaryGraphTree.Node<*>, block: () -> Unit) {
        block()
        node.left?.let { traversalWithNulls(it, block) } ?: block()
        node.right?.let { traversalWithNulls(it, block) } ?: block()
    }

    var result = 0
    root?.let { traversalWithNulls(it) { result++ } }
    return result
}

val BinaryGraphTree<*>.size: Int get() {
    var result = 0
    forEach { result++ }
    return result
}
