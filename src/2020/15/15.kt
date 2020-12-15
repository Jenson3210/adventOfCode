package `2020`.`15`

import util.printDay
import util.readFileLineCsvToInt
import kotlin.streams.toList

fun main() {
    printDay(1)
    print("The 2020th number spoken is ")
    println(Game(readFileLineCsvToInt("2020_15.txt").toList()).play(2020))

    printDay(2)
    print("The 30000000th number spoken is  : ")
    println(Game(readFileLineCsvToInt("2020_15.txt").toList()).play(30000000))
}

private class Game(val inputNumbers: List<Int>) {

    val memory : MutableMap<Long, HistoryList> = mutableMapOf()
    var lastNumber = 0L

    fun play(turns: Long) : Long {
        for (i in 0 until turns) {
            lastNumber = if (i < inputNumbers.size) {
                inputNumbers[i.toInt()].toLong()
            } else {
                memory[lastNumber]!!.getDifference()
            }
            memory.putIfAbsent(lastNumber, HistoryList())
            memory[lastNumber]!!.add(i)
        }
        return lastNumber
    }
}

private class HistoryList {
    val list: MutableList<Long> = mutableListOf()
    val size = 2

    fun add(element: Long) {
        list.add(element)
        if (list.size > size) {
            list.removeAt(0)
        }
    }

    fun getDifference() : Long {
        if (list.size == 2) {
            return list[1] - list[0]
        }
        return 0
    }
}