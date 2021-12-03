package `2021`.`03`

import getElementsWithTheirCount
import leastCommonElements
import mostCommonElements
import util.printDay
import util.readFileLineByLineToText
import util.readNthCharsOfEquallyLongLines
import java.util.function.BiPredicate
import kotlin.streams.toList

fun main() {
    printDay(1)
    println(getPowerConsumption(readNthCharsOfEquallyLongLines("2021_03.txt")))

    printDay(2)
    println(getLifeSupportRating(readFileLineByLineToText("2021_03.txt").toList()))
}

private fun getPowerConsumption(bits: List<List<Char>>): Long {
    val gammaRate = bits.map { it.mostCommonElements()[0] }.joinToString(separator = "").toLong(2)
    val epsilonRate = bits.map { it.leastCommonElements()[0] }.joinToString(separator = "").toLong(2)

    return gammaRate * epsilonRate
}

private fun getLifeSupportRating(allBinaries: List<String>): Long {
    val oxygen = getMostMatchingBinaryAsLong(allBinaries) {
            binaries: List<String>, i: Int -> binaries.map { it[i] }.getElementsWithTheirCount().getOrDefault('0', 0) > binaries.map { it[i] }.getElementsWithTheirCount().getOrDefault('1', 0)
    }
    val co2 = getMostMatchingBinaryAsLong(allBinaries) {
            binaries: List<String>, i: Int -> binaries.map { it[i] }.getElementsWithTheirCount().getOrDefault('0', 0) <= binaries.map { it[i] }.getElementsWithTheirCount().getOrDefault('1', 0)
    }

    return oxygen * co2
}

private fun getMostMatchingBinaryAsLong(allBinaries: List<String>, predicate: BiPredicate<List<String>, Int>): Long {
    var allBinariesWorking = allBinaries
    var i = 0

    while (allBinariesWorking.size > 1) {
        allBinariesWorking = if (predicate.test(allBinariesWorking, i)) {
            allBinariesWorking.filter { it[i] == '0' }
        } else {
            allBinariesWorking.filter { it[i] == '1' }
        }
        i++
    }

    return allBinariesWorking[0].toLong(2)
}