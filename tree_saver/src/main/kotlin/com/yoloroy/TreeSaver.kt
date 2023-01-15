package com.yoloroy

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.yoloroy.algorithm_mod.AvlTree
import com.yoloroy.algorithm_mod.BinaryGraphTree
import com.yoloroy.algorithm_mod.BinaryGraphTree.Node
import com.yoloroy.algorithm_mod.MutableBinaryGraphTree
import java.io.File
import java.io.InputStream
import com.yoloroy.algorithm_mod.MutableBinaryGraphTree.Node as MutableNode

private val mapper = jacksonObjectMapper()

fun saveAvlTree(tree: AvlTree<Int>, file: File) = mapper.writeValue(file, tree.toString())

fun <T : Comparable<T>> loadAvlTree(file: File, stringToValue: (String) -> T) = loadAvlTree(file.inputStream(), stringToValue)

fun <T : Comparable<T>> loadAvlTree(inputStream: InputStream, stringToValue: (String) -> T) = mapper
    .readValue(inputStream, String::class.java)
    .let { AvlTree.fromString(it, stringToValue) }

fun saveTree(tree: BinaryGraphTree<Int>, file: File) = mapper.writeValue(file, tree.toTreeData())

fun loadTree(file: File) = loadTree(file.inputStream())

fun loadTree(inputStream: InputStream) = mapper.readValue(inputStream, TreeData::class.java).toTree()

internal fun BinaryGraphTree<Int>.toTreeData(): TreeData {

    fun associationsOf(
        nodes: List<Node<Int>>,
        nodesNames: Map<Node<Int>, String>
    ) = nodes.associate { nodesNames[it]!! to listOf(nodesNames[it.left], nodesNames[it.right]) }

    fun nodesValuesOf(
        nodes: List<Node<Int>>,
        nodesNames: Map<Node<Int>, String>
    ) = nodes.associate { nodesNames[it]!! to it.value }

    fun nodesNamesOf(nodes: List<Node<Int>>) = nodes
        .withIndex()
        .associate { (i, node) -> node to "n$i" }

    fun nodesOf(tree: BinaryGraphTree<Int>) = buildList { tree.forEachNode { add(it) } }

    val nodes = nodesOf(this)
    val nodesNames = nodesNamesOf(nodes)
    val namesValues = nodesValuesOf(nodes, nodesNames)
    val associations = associationsOf(nodes, nodesNames)
    return TreeData(namesValues, associations)
}

internal fun TreeData.toTree(): MutableBinaryGraphTree<Int> {

    fun namesNodes(treeData: TreeData): Map<String, MutableBinaryGraphTree.Node<Int>> {
        val namesNodes = treeData.namesValues.mapValues { MutableNode(it.value) }
        for ((parentName, childrenNames) in treeData.associations) {
            namesNodes[parentName]?.setLeftAndAssignParent(namesNodes[childrenNames[0]])
            namesNodes[parentName]?.setRightAndAssignParent(namesNodes[childrenNames[1]])
        }
        return namesNodes
    }

    fun allParent(namesNodes: Map<String, MutableBinaryGraphTree.Node<Int>>): MutableBinaryGraphTree.Node<Int>? {
        if (namesNodes.isEmpty()) return null
        var allParent = namesNodes.values.iterator().next()
        while (allParent.parent != null) allParent = allParent.parent!!
        return allParent
    }

    val namesNodes = namesNodes(this)
    val allParent = allParent(namesNodes)
    return MutableBinaryGraphTree<Int>().apply { root = allParent }
}

internal data class TreeData(val namesValues: Map<String, Int>, val associations: Map<String, List<String?>>)
