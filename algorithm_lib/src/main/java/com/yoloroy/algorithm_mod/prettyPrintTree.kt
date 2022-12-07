package com.yoloroy.algorithm_mod

import com.yoloroy.algorithm_mod.BinaryGraphTree.Node

fun <T> prettyPrintTree(tree: Node<T>) = buildString {
    val indentStack = mutableListOf(0)

    fun appendNodeHeader(tree: Node<T>) {
        val isHasChildren = tree.children.isNotEmpty()

        val objectBraces = if (isHasChildren) "{" else ""
        val treeColon = if (isHasChildren) ": " else ""
        val header = "$objectBraces'${tree.value}'$treeColon"

        append(header)
        indentStack += header.length
    }

    fun nodeToString(tree: Node<T>) {
        appendNodeHeader(tree)

        tree.children.forEachIndexed { index, node ->
            nodeToString(node)

            append(
                if (index == tree.children.lastIndex) "}"
                else ",\n" + " ".repeat(indentStack.sum())
            )
        }
        indentStack.removeAt(indentStack.lastIndex)
    }

    nodeToString(tree)
}

fun <T> indentationTreePrint(tree: Node<T>, indentSize: Int = 4) = buildString {
    var indentStack = 0

    fun appendNodeHeader(tree: Node<T>) {
        val isHasChildren = tree.children.isNotEmpty()

        val treeColon = if (isHasChildren) " {" else ""
        val header = "${tree.value}$treeColon"

        append(header)
        indentStack += indentSize
    }

    fun nodeToString(tree: Node<T>) {
        appendNodeHeader(tree)

        tree.children.forEach { node ->
            append("\n" + " ".repeat(indentStack))
            nodeToString(node)
        }
        indentStack -= indentSize

        if (tree.children.isNotEmpty()) {
            append("\n" + " ".repeat(indentStack))
            append("}")
        }
    }

    nodeToString(tree)
}

val <T> Node<T>.children get() = listOfNotNull(left, right)
