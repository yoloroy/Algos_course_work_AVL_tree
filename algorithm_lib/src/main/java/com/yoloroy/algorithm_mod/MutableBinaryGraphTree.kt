package com.yoloroy.algorithm_mod

import com.yoloroy.algorithm_mod.BinaryGraphTree.*
import kotlin.reflect.KMutableProperty0

/**
 * Binary Graph tree
 *
 * This class only provides operations for work with tree
 *
 * @param T
 * @constructor creates "linked list" from provided values and stores it's first element in [root]
 *
 * @param values
 */
class MutableBinaryGraphTree<T>(values: List<T> = emptyList()) : BinaryGraphTree<T> {

    constructor(vararg values: T) : this(values.toList())

    override var root: Node<T>? = null

    init {
        if (values.isNotEmpty()) {
            var tempRoot = Node(values.last())
            for (value in values.dropLast(1).asReversed()) {
                tempRoot = tempRoot.createChildOnLeft(value)
            }
            root = tempRoot
        }
    }

    /**
     *  LL Rotation
     *  https://www.javatpoint.com/ll-rotation-in-avl-tree
     */
    fun rotateLeftLeft(node: Node<T>) {
        val previousLeft = node.left ?: throw IllegalStateException("There no left child node for this critical node")
        node.left = previousLeft.right
        previousLeft.right = node
        if (node == root) root = previousLeft
        else node.parent!!.replaceChild(node, previousLeft)
    }

    /**
     *  RR Rotation
     *  https://www.javatpoint.com/rr-rotation-in-avl-tree
     */
    fun rotateRightRight(node: Node<T>) {
        val previousRight = node.right ?: throw IllegalStateException("There no right child node for this critical node")
        node.right = null
        previousRight.left = node
        if (node == root) root = previousRight
        else node.parent!!.replaceChild(node, previousRight)
    }

    /**
     *  RL Rotation
     *  https://www.javatpoint.com/rl-rotation-in-avl-tree
     */
    fun rotateRightLeft(node: Node<T>) {
        val previousRightLeft = node.right?.left ?: throw IllegalStateException()
        previousRightLeft.parent!!.left = null
        previousRightLeft.right = node.right
        node.right = previousRightLeft.left ?: throw IllegalStateException()
        previousRightLeft.left = node
        if (node == root) root = previousRightLeft
        else node.parent!!.replaceChild(node, previousRightLeft)
    }

    /**
     *  LR Rotation
     */
    fun rotateLeftRight(node: Node<T>) {
        val previousLeftRight = node.left?.right ?: throw IllegalStateException()
        previousLeftRight.parent!!.right = previousLeftRight.left ?: previousLeftRight.right
        previousLeftRight.left = node.left
        previousLeftRight.right = node
        node.left = null
        if (node == root) root = previousLeftRight
        else node.parent!!.replaceChild(node, previousLeftRight)
    }

    override fun findValue(predicate: (T) -> Boolean): ValueWithPath<T> = TODO()

    override operator fun get(path: Path): T = TODO()

    override fun getNode(path: Path): Node<T> = TODO()

    class Node<T>(
        override var value: T,
        override var parent: Node<T>? = null,
        left: Node<T>? = null,
        right: Node<T>? = null
    ) : BinaryGraphTree.Node<T> {
        override var left: Node<T>? = left?.apply { this.parent = this@Node }
        override var right: Node<T>? = right?.apply { this.parent = this@Node }

        fun setLeftAndAssignParent(node: Node<T>?) = setNodeAndAssignParent(node, ::left)

        fun setRightAndAssignParent(node: Node<T>?) = setNodeAndAssignParent(node, ::right)

        fun replaceChild(previousChild: Node<T>, newChild: Node<T>?) {
            if (previousChild == left) {
                left = newChild
            } else if (previousChild == right) {
                right = newChild
            }
        }

        fun replaceChildAndAssignParent(previousChild: Node<T>, newChild: Node<T>?) {
            val property = when (previousChild) {
                left -> ::left
                right -> ::right
                else -> throw IllegalArgumentException("node $previousChild is not a child for this node $this")
            }
            setNodeAndAssignParent(newChild, property)
        }

        fun createChildOnLeft(value: T): Node<T> {
            return left?.createChildOnLeft(value) ?: Node(value, this).also { left = it }
        }

        fun createChildOnRight(value: T): Node<T> {
            return right?.createChildOnRight(value) ?: Node(value, this).also { right = it }
        }

        fun removeFromParent() {
            parent?.replaceChildAndAssignParent(this, null) ?: throw IllegalStateException("There no parent to replace")
        }

        private fun setNodeAndAssignParent(node: Node<T>?, property: KMutableProperty0<Node<T>?>) {
            property.get()?.parent = null
            property.set(node)
            node?.parent = this
        }
    }
}
