package com.yoloroy.algorithm_mod

import com.yoloroy.algorithm_mod.dig
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class UtilKtTest {

    @Test
    fun dig() {
        assertEquals(6765, (0 to 1).dig { (a, b) -> (b to (a + b)).takeIf { (_, b) -> b < 10000 }}.second, "Fibonacci series")
    }
}
