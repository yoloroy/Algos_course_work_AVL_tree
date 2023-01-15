package com.yoloroy.algorithm_mod

import com.yoloroy.algorithm_mod.BinaryGraphTree
import com.yoloroy.algorithm_mod.indentationTreePrint
import org.junit.jupiter.api.Assertions.assertEquals

fun <T> assertTreeEquals(expected: BinaryGraphTree<T>, actual: BinaryGraphTree<T>) =
    assertEquals(
        expected.root?.let { indentationTreePrint(it, 1) } ?: "",
        actual.root?.let { indentationTreePrint(it, 1) } ?: ""
    )
