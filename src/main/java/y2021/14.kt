package y2021

import utils.printDay
import utils.readFileLineByLineToText
import kotlin.streams.toList

fun main() {
    printDay(1)
    println(applyPolymerPairs(10))

    printDay(2)
    println(applyPolymerPairs(40))
}

private fun applyPolymerPairs(times: Int): Long {
    val polymer = readFileLineByLineToText(2021, 14).toList().first()
    var polymerPairs = mutableMapOf<String, Long>()
    (0 until  polymer.indices.last).forEach {
        val part: String = polymer.substring(it).take(2)
        val count = polymerPairs.getOrDefault(part, 0L)
        polymerPairs[part] = count + 1
    }

    var workingPolymerPairs: MutableMap<String, Long>
    val pairs = readFileLineByLineToText(2021, 14).skip(2).toList().map { it.substringBefore(" -> ") to it.substringAfter(" -> ") }.toMap()

    (0 until times).forEach {
        workingPolymerPairs = mutableMapOf()
        polymerPairs.forEach { (key, count) ->
            if (pairs.containsKey(key)) {
                workingPolymerPairs[key[0] + pairs[key]!!] = workingPolymerPairs.getOrDefault(key[0] + pairs[key]!!, 0L) + count
                workingPolymerPairs[pairs[key]!! + key[1]] = workingPolymerPairs.getOrDefault(pairs[key]!! + key[1], 0L) + count
            } else {
                workingPolymerPairs[key] = count
            }
        }
        polymerPairs = workingPolymerPairs
    }

    val charCount = mutableMapOf<Char, Long>()
    polymerPairs.forEach { (k, v) -> k.toList().forEach { charCount[it] = charCount.getOrDefault(it, 0L) + v } }

    return charCount.maxOf { if (it.value % 2 == 0L) it.value/2 else (it.value + 1)/2 } - charCount.minOf { if (it.value % 2 == 0L) it.value/2 else (it.value + 1)/2 }
}