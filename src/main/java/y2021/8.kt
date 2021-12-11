package y2021

import mostCommonElements
import utils.printDay
import utils.readFileLineByLineToText
import utils.sumByLong
import kotlin.streams.toList

fun main() {
    printDay(1)
    println(get1Or4Or7Or8Count(readFileLineByLineToText(2021, 8).toList()))

    printDay(2)
    println(sum(readFileLineByLineToText(2021, 8).toList()))
}

private fun get1Or4Or7Or8Count(input: List<String>): Int {
    return input
        .map { it.substringAfter(" | ") }
        .flatMap { it.split(" ") }
        .count { listOf(2, 3, 4, 7).contains(it.length) }
}

private fun sum(input: List<String>): Long {
    return input.sumByLong { inputLine ->
        val wiringMap = inputLine.substringBefore(" | ").toWiringMap()
        val after = inputLine.substringAfter(" | ")

        after.split(" ")
            .map { number -> wiringMap.filter { entry -> entry.value.length == number.length }.filter { entry -> entry.value.all { c ->  number.contains(c) } }.firstNotNullOfOrNull { entry -> entry.key } }
            .joinToString(separator = "")
            .toLong()
    }
}

private fun String.toWiringMap(): Map<Int, String> {
    val chars = mutableMapOf<Int, String>()
    chars[1] = this.split(" ").first { it.length == 2 }
    chars[7] = this.split(" ").first { it.length == 3 }
    chars[4] = this.split(" ").first { it.length == 4 }
    chars[8] = this.split(" ").first { it.length == 7 }
    chars[3] = this.split(" ").filter { it.length == 5 }.first { chars[1]!!.all { c -> it.contains(c) } }
    chars[9] = this.split(" ").filter { it.length == 6 }.first { it.all { c -> (chars[3] + chars[4]).contains(c) } }
    chars[2] = this.split(" ").filter { it.length == 5 }.first { !it.all { c -> chars[9]!!.contains(c) } }
    chars[5] = this.split(" ").filter { it.length == 5 }.first { !it.contains((chars[1] + chars[2] + chars[3] + chars[4]).toList().mostCommonElements().first()) }
    chars[6] = this.split(" ").filter { it.length == 6 }.first { !it.contains((chars[1] + chars[2] + chars[3] + chars[4]).toList().mostCommonElements().first()) }
    chars[0] = this.split(" ").filter { it.length == 6 }.first { it != chars[9] && it != chars[6] }

    return chars.toMap()
}