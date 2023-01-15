package com.yoloroy.algorithm_mod

import kotlin.math.abs

fun diff(a: Int, b: Int) = abs(a - b)

// what a lovely peace of code
// fun <T> T.dig(next: (T) -> T?): T = next(this)?.dig(next) ?: this

fun <T> T.dig(next: (T) -> T?): T {
    var t = this
    while (true) t = next(t) ?: break
    return t
}
