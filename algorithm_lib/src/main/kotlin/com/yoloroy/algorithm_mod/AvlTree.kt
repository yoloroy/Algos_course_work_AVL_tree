package com.yoloroy.algorithm_mod

import com.yoloroy.algorithm_mod.AvlTree.AvlData
import com.yoloroy.algorithm_mod.BinaryGraphTree.*
import com.yoloroy.algorithm_mod.BinaryGraphTree.LeftRight.Left
import com.yoloroy.algorithm_mod.BinaryGraphTree.LeftRight.Right
import kotlin.math.max
import com.yoloroy.algorithm_mod.MutableBinaryGraphTree.Node as MutableNode

private typealias MutableAvlNode<T> = MutableNode<AvlData<T>>
private typealias AvlNode<T> = Node<AvlData<T>>

class AvlTree<T: Comparable<T>>(values: Iterable<T>) : Iterable<T> {

    private val mutableTree = MutableBinaryGraphTree<AvlData<T>>()
    private var root
        get() = mutableTree.root
        set(value) { mutableTree.root = value }

    init {
        for (value in values) insertValue(value)
    }

    val tree: BinaryGraphTree<AvlData<T>> get() = mutableTree

    fun insertValue(valueToInsert: T) {
        fun MutableAvlNode<T>.insertValue(previousPath: Path = Path.empty()): Path? {
            val leftRight = leftRightOrNullFor(valueToInsert) ?: return null
            var child by if (leftRight == Left) this::left else this::right
            if (child == null) {
                child = MutableNode(AvlData(valueToInsert), parent = this)
                updateHeight()
                return previousPath + leftRight
            }
            val path = child!!.insertValue(previousPath + leftRight) ?: return null
            updateHeight()
            if (height >= 2 && diff(left?.height ?: 0, right?.height ?: 0) > 1) {
                when {
                    path.endsWith(Left, Left) -> mutableTree.rotateLeftLeft(this)
                    path.endsWith(Left, Right) -> mutableTree.rotateLeftRight(this)
                    path.endsWith(Right, Left) -> mutableTree.rotateRightLeft(this)
                    path.endsWith(Right, Right) -> mutableTree.rotateRightRight(this)
                }
                updateHeight()
            }
            return path.popped()
        }

        if (root == null) {
            root = MutableNode(AvlData(valueToInsert))
            return
        }
        root!!.insertValue()
    }

    fun removeValue(valueToRemove: T) {
        fun MutableAvlNode<T>.balanceIfNecessary() {
            println("diff: ${diff(left?.height ?: 0, right?.height ?: 0) <= 1}")
            if (diff(left?.height ?: 0, right?.height ?: 0) <= 1) return
            when {
                left?.left != null -> mutableTree.rotateLeftLeft(this)
                left?.right != null -> mutableTree.rotateLeftRight(this)
                right?.left != null -> mutableTree.rotateRightLeft(this)
                right?.right != null -> mutableTree.rotateRightRight(this)
            }
            updateHeight()
        }

        /**
         * @return parent of removed node
         */
        fun MutableAvlNode<T>.removeValue() {
            val leftRight = when {
                value < valueToRemove -> Right
                value > valueToRemove -> Left
                else -> {
                    removeFromParentAndReplaceByChild()
                        ?.also { node -> node.children.forEach { it.updateHeight() } }
                        ?.dig { node ->
                            node.updateHeight()
                            println(node.height)
                            println(node.children.map { it.height })
                            println()
                            node.balanceIfNecessary()
                            node.parent
                        }
                    return
                }
            }
            this[leftRight]!!.removeValue()
            updateHeight()
        }

        root?.removeValue()
    }

    fun replaceValue(oldValue: T, newValue: T) {
        removeValue(oldValue)
        insertValue(newValue)
    }

    /**
     * Find path to value or null
     *
     * @param predicate returns either we need to the [Right] child, [Left] child, or stop traversing by returning null
     * @return path to value or null if value was not found
     */
    fun findPathToValueOrNull(value: T): ValueWithPath<AvlData<T>>? {
        var node: MutableAvlNode<T>? = null
        val path = buildPath {
            node = root?.dig { node -> node
                .leftRightOrNullFor(value)
                ?.let {
                    add(it)
                    node[it]
                }
            }
        }
        return node
            ?.takeIf { it.value.value == value }
            ?.let { ValueWithPath(path, it.value) }
    }

    inline fun forEach(crossinline block: (AvlData<T>) -> Unit): Unit = tree.forEach(block)

    inline fun forEachNode(crossinline block: (Node<AvlData<T>>) -> Unit): Unit = tree.forEachNode { block(it) }

    override fun iterator() = iterator { forEach { yield(it) } }

    override fun toString(): String = buildString {
        append('/')
        val values = buildList {
            this@AvlTree.forEach { data: AvlData<T> ->
                add(data.value)
                println(data.value)
            }
        }
        append(values.joinToString(", "))
        append('\\')
    }

    companion object {
        fun <T : Comparable<T>> fromString(string: String, stringToValue: (String) -> T) = AvlTree(
            string
                .drop(1)
                .dropLast(1)
                .split(", ")
                .map(stringToValue)
        )
    }

    private fun MutableAvlNode<T>.updateHeight() {
        height = max(left?.height ?: 0, right?.height ?: 0) + 1
    }

    class AvlData<T : Comparable<T>>(val value: T, height: Int = 1) : Comparable<T> by value {
        var height = height
            internal set

        operator fun component1() = value

        operator fun component2() = height

        override fun toString() = "(value: $value, height: $height)"
    }

    private fun AvlNode<T>.leftRightOrNullFor(otherValue: T) = when {
        value > otherValue -> Left
        value < otherValue -> Right
        else -> null
    }

    /**
     * Remove from parent and replace by child, if child exists
     *
     * @return parent of removed node
     */
    private fun MutableAvlNode<T>.removeFromParentAndReplaceByChild(): MutableAvlNode<T>? {
        if (isLeaf()) return parent
            ?.also { removeFromParent() }
            ?: null.also { root = null }

        children.singleOrNull()?.let { child ->
            return parent
                ?.also { it.replaceChildAndAssignParent(this, child) }
                ?: null.also { root = child.also { it.parent = null } }
        }

        val replacement = when (children.maxBy { it.height }) {
            left -> left!!.dig { it.right }
            else -> right!!.dig { it.left }
        }
        value = AvlData(replacement.value.value, height)
        replacement.removeFromParentAndReplaceByChild()
        return replacement.parent
    }
}

var <T : Comparable<T>> Node<AvlData<T>>.height
    get() = value.height
    set(new) { value.height = new }

operator fun <T : Comparable<T>> AvlTree<T>.plusAssign(value: T) = insertValue(value)

operator fun <T : Comparable<T>> AvlTree<T>.minusAssign(value: T) = removeValue(value)
