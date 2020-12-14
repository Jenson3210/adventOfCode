package `2020`.`14`

import util.cartesianProduct
import util.printDay
import util.readFileLineByLineToText
import util.sumByLong
import kotlin.math.pow
import kotlin.streams.toList

fun main() {
    printDay(1)
    print("Sum of memory values after computing: ")
    println(PortComputer(readFileLineByLineToText("2020_14.txt").toList()).compute())

    printDay(2)
    print("Sum of memory values after decoding: ")
    println(PortComputer(readFileLineByLineToText("2020_14.txt").toList()).decode())

}

private class PortComputer(val memoryLines: List<String>) {
    lateinit var mask: String
    val memory = Array((memoryLines.filter { !it.contains("mask") }.map { it.substringAfter("[").substringBefore("]").toInt() }.max() ?: 0) + 1) {0L}
    val memoryDecoder = mutableMapOf<Long, Long>()

    fun compute() : Long {
        for (line in memoryLines) {
            when (line.substringBefore(" = ")) {
                "mask" -> mask = line.substringAfter(" = ")
                else -> memory[line.substringAfter("[").substringBefore("]").toInt()] = line.substringAfter(" = ").toLong().maskVersion1(mask)
            }
        }

        return memory.toList().sumByLong { it }
    }

    fun decode(): Long {
        for (line in memoryLines) {
            when (line.substringBefore(" = ")) {
                "mask" -> mask = line.substringAfter(" = ")
                else -> {
                    line.substringAfter("[").substringBefore("]").toLong().maskVersion2(mask).forEach {
                        memoryDecoder[it] = line.substringAfter(" = ").toLong()
                    }
                }
            }
        }

        return memoryDecoder.values.sumByLong { it }
    }
}

private fun Long.maskVersion1(mask:String): Long {
    return this.toString(2).padStart(mask.length, '0').mapIndexed { index: Int, c: Char ->
        when (mask[index]) {
            '1' -> '1'
            '0' -> '0'
            else -> c
        }
    }.toLongFromBinary()
}
private fun Long.maskVersion2(mask:String): List<Long> {
    val chars = Array(mask.length) { listOf<Char>() }
    this.toString(2).padStart(mask.length, '0')
        .forEachIndexed { index: Int, c: Char ->
            when (mask[index]) {
                '1' -> chars[index] = listOf('1')
                '0' -> chars[index] = listOf(c)
                else -> chars[index] = listOf('0', '1')
            }
        }

    val cartesianProduct: Set<List<Char>> = cartesianProduct(*chars) as Set<List<Char>>
    return cartesianProduct.map { it.toLongFromBinary() }
}

private fun Iterable<Char>.toLongFromBinary() : Long {
    var result = 0.0
    this.toList().reversed().forEachIndexed { index, c -> if (c == '1') result += 2.0.pow(index) }

    return result.toLong()
}