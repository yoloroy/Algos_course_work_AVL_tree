package com.yoloroy.algorithm_mod

import java.util.Stack

interface BinaryGraphTree<T> {

    val root: Node<T>?

    fun findValue(predicate: (T) -> Boolean): ValueWithPath<T>

    operator fun get(path: Path): T

    fun getNode(path: Path): Node<T>

    fun forEachNode(block: (Node<T>) -> Unit) {
        val history = Stack<Node<T>>()
        var node = root
        while (node != null || history.isNotEmpty()) { // while we have something to traverse
            while (node != null) { // go to deeper left while we can
                history.push(node)
                node = node.left
            }
            node = history.pop() // leftest element of subtree
            block(node)
            if (!history.empty()) {
                node = history.pop() // parent of leftest element
                block(node)
            }
            node = node.right
        }
    }

    interface Node<T> {
        val parent: Node<T>?
        val left: Node<T>?
        val right: Node<T>?
        val value: T

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
    }

    data class ValueWithPath<T>(val path: Path, val value: T)

    @JvmInline
    value class Path(private val path: List<LeftRight>) : Iterable<LeftRight> {

        operator fun plus(other: Path) = Path(path + other.path)

        override fun iterator() = path.iterator()
    }

    enum class LeftRight { Left, Right }
}

inline fun <T> BinaryGraphTree<T>.forEach(crossinline block: (T) -> Unit) = forEachNode { block(it.value) }

fun buildPath(block: MutableList<BinaryGraphTree.LeftRight>.() -> Unit) = BinaryGraphTree.Path(buildList(block))

fun <T> buildBinaryGraphTree(block: MutableBinaryGraphTree<T>.() -> Unit): BinaryGraphTree<T> = MutableBinaryGraphTree<T>().apply(block)

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
