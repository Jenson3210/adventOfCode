package `2020`.`10`

import util.Counter
import util.printDay
import util.readFileLineByLineToInt
import kotlin.math.pow
import kotlin.streams.toList

fun main() {
    printDay(1)
    print("Joltage adapter differences of multiplied by the ones of 3 : ")
    val differencesCounts: Map<Int, Counter> = JoltageAdapterList(
        readFileLineByLineToInt("2020_10.txt").toList().map { JoltageAdapter(it) }).getDifferencesCounts()
    println((differencesCounts[1] ?: Counter()).value * (differencesCounts[3] ?: Counter()).value)

    printDay(2)
    print("Result at the end of the program : ")
    println( JoltageAdapterList(
        readFileLineByLineToInt("2020_10.txt").toList().map { JoltageAdapter(it) }).getPossibilitiesCount())
}

private class JoltageAdapterList(joltageAdapters: List<JoltageAdapter>) {

    val joltageAdapters: List<JoltageAdapter>

    init {
        val adapters = joltageAdapters.toMutableList()
        adapters.add(JoltageAdapter((joltageAdapters.maxBy { it.joltage }?.joltage ?: 0) + 3))
        adapters.add(JoltageAdapter(0))
        this.joltageAdapters = adapters.sortedBy { it.joltage }
    }

    fun getDifferencesCounts(): Map<Int, Counter> {
        val counts = mutableMapOf<Int, Counter>()
        var diffCount: Int;
        for (index in IntRange(0, joltageAdapters.lastIndex - 1)) {
            diffCount=joltageAdapters[index+1].joltage-joltageAdapters[index].joltage
            counts.putIfAbsent(diffCount, Counter())
            counts[diffCount]!!.increment();
        }

        return counts
    }

    fun getPossibilitiesCount() : Long {
        val paths = Array(size = joltageAdapters.maxBy { it.joltage }!!.joltage + 1) {0L}

        paths[0] = 1L
        joltageAdapters.forEach {
            if (it.joltage != 0) {
                paths[it.joltage] =
                      paths.getOrElse(it.joltage-1) {0L} +
                              paths.getOrElse(it.joltage-2) {0L} +
                              paths.getOrElse(it.joltage-3) {0L}
            }
        }
        return paths.last()
    }
}

private class JoltageAdapter(val joltage: Int)