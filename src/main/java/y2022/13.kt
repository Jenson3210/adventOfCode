package y2022

import utils.*
import kotlin.math.abs
import kotlin.math.max

fun main() {
    printDay(1)
    println(compare())

    printDay(2)
    println(decoderKey())
}

private fun compare(): Long {
    return readFileToText(2022, 13).dropLast(1).split("\n\n")
        .asSequence()
        .map {
            it.split("\n").first() to it.split("\n").last()
        }
        .map { it.first.nestedReader { it.toInt() } to it.second.nestedReader { it.toInt() } }
        .mapIndexed { index, pair ->
//            println("Compare ${pair.first} with ${pair.second}: ${pair.compare()}")
            index + 1L to pair.compare()
        }
        .filter { it.second < 0 }
        .sumOf { it.first }
}

private fun decoderKey(): Long {
    return ("[[2]]\n[[6]]\n" + readFileToText(2022, 13).dropLast(1)).replace("\n\n", "\n").split("\n")
        .map { it to it.nestedReader { it.toInt() } }
        .sortedWith { o1, o2 -> (o1.second to o2.second).compare() }
        .mapIndexed { index, pair -> pair.first to index + 1L }
        .filter { listOf("[[2]]", "[[6]]").contains(it.first) }
        .multiplyLong { it.second }
}

private fun Pair<Nested<Int>?, Nested<Int>?>.compare(): Int {
    if (first == null) {
        return if (second == null) 0 else -1
    }
    if (second == null) return 1
    if (first is NestedObject && second is NestedObject) {
        if ((first as NestedObject<Int>).value == (second as NestedObject<Int>).value) {
            return 0
        }
        return ((first as NestedObject<Int>).value - (second as NestedObject<Int>).value) / abs((first as NestedObject<Int>).value - (second as NestedObject<Int>).value)
    }
    if (first is NestedList && second is NestedList) {
        (0..max((first as NestedList<Int>).list.indices.last, (second as NestedList<Int>).list.indices.last))
            .forEach { index ->
                val compared =
                    ((first as NestedList<Int>).list.getOrNull(index) to (second as NestedList<Int>).list.getOrNull(
                        index
                    )).compare()
                if (compared > 0) return 1
                if (compared < 0) return -1
            }
        return 0
    }
    if (first is NestedObject) {
        return (NestedList(mutableListOf(first!!)) to second).compare()
    }
    if (second is NestedObject) {
        return (first to NestedList(mutableListOf(second!!))).compare()
    }
    throw Exception()
}
