package `2021`.`06`

import util.printDay
import util.readFileLineCsvToText
import util.sumByLong
import kotlin.streams.toList

fun main() {
    printDay(1)
    println(getLanternFishCount(80))

    printDay(2)
    println(getLanternFishCount(256))
}

private fun getLanternFishCount(days: Int): Long {
    var lanternfish = readFileLineCsvToText("2021_06.txt")
        .map { it.toInt() }
        .toList()
        .groupingBy { it }
        .eachCount()
        .map { it.key to it.value * 1L }
        .toMap()
    val mutableLanternfish: MutableMap<Int, Long> = mutableMapOf()

    (0 until days).map {
        lanternfish.entries.forEach {
            if (it.key == 0) {
                mutableLanternfish[6] = mutableLanternfish.getOrDefault(6, 0) + it.value
                mutableLanternfish[8] = it.value
            } else {
                mutableLanternfish[it.key - 1] = mutableLanternfish.getOrDefault(it.key - 1, 0) + it.value
            }
        }

        lanternfish = mutableLanternfish.toMap()
        mutableLanternfish.clear()
    }

    return lanternfish.values.sumByLong { it * 1L }
}