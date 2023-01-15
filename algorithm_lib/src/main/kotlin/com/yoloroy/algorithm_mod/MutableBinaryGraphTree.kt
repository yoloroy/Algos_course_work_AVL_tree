package com.yoloroy.algorithm_mod

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
class MutableBinaryGraphTree<T>(values: List<T> = emptyList()) : BinaryGraphTree<T>() {

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
     *  LL case rotation (z)
     *
     *  does:
     *  ```
     *        z              y
     *       / \           /   \
     *      y   zr  =>    x     z
     *     / \      =>   / \   / \
     *    x   yr    =>  xl xr yr zr
     *   / \
     *  xl   xr
     *  ```
     *
     *  @param nodeZ node 'z' from the description
     */
    fun rotateLeftLeft(nodeZ: Node<T>) {
        val nodeY = nodeZ.left ?: throw IllegalStateException()
        val nodeYR = nodeY.right
        nodeZ.replaceChildAndAssignParent(nodeY, nodeYR)
        nodeZ.parent
            ?.replaceChildAndAssignParent(nodeZ, nodeY)
            ?: run { root = nodeY }
        nodeY.setRightAndAssignParent(nodeZ)
    }

    /**
     *  RR case rotation (z)
     *
     *  does:
     *  ```
     *    z                   y
     *   /  \               /   \
     *  zr   y       =>    z      x
     *      /  \     =>   / \    / \
     *     yl   x    =>  zl yl  xl xr
     *         / \
     *        xl xr
     *  ```
     *
     *  @param nodeZ node 'z' from the description
     */
    fun rotateRightRight(nodeZ: Node<T>) {
        val nodeY = nodeZ.right ?: throw IllegalStateException()
        val nodeYL = nodeY.left
        nodeZ.replaceChildAndAssignParent(nodeY, nodeYL)
        nodeZ.parent
            ?.replaceChildAndAssignParent(nodeZ, nodeY)
            ?: run { root = nodeY }
        nodeY.setLeftAndAssignParent(nodeZ)
    }

    /**
     *  RL Rotation, big left turn
     *  https://www.javatpoint.com/rl-rotation-in-avl-tree
     */
    fun rotateRightLeft(nodeZ: Node<T>) {
        rotateLeftLeft(nodeZ.right ?: throw IllegalStateException())
        rotateRightRight(nodeZ)
    }

    /**
     *  LR Rotation, big right turn
     */
    fun rotateLeftRight(nodeZ: Node<T>) {
        rotateRightRight(nodeZ.left ?: throw IllegalStateException())
        rotateLeftLeft(nodeZ)
    }

    open class Node<T>(
        override var value: T,
        override var parent: Node<T>? = null,
        left: Node<T>? = null,
        right: Node<T>? = null
    ) : BinaryGraphTree.Node<T>(parent, left, right, value) {

        override var left: Node<T>? = left?.apply { this.parent = this@Node }
        override var right: Node<T>? = right?.apply { this.parent = this@Node }

        override val children get() = listOfNotNull(left, right)

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

        fun createChildOnLeft(value: T): Node<T> =
            left?.createChildOnLeft(value)
                ?: Node(value, this).also { left = it }

        fun createChildOnRight(value: T): Node<T> =
            right?.createChildOnRight(value)
                ?: Node(value, this).also { right = it }

        fun removeFromParent() =
            parent?.replaceChildAndAssignParent(this, null)
                ?: throw IllegalStateException("There no parent to replace")

        private fun setNodeAndAssignParent(node: Node<T>?, property: KMutableProperty0<Node<T>?>) {
            var child by property
            child?.takeIf { it.parent == this }?.parent = null
            child = node
            node?.parent = this
        }

        override operator fun get(leftRight: LeftRight) = when (leftRight) {
            LeftRight.Left -> left
            LeftRight.Right -> right
        }

        override fun toString() = "Node(value: $value parent: ${parent?.value} left: ${left?.value} right: ${right?.value})"
    }
}
