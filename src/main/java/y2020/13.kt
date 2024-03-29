package y2020

import utils.multiplyLong
import utils.printDay
import utils.readFileLineByLineToText

fun main() {
    printDay(1)
    print("Bus ID multiplied by waiting time: ")
    val arrivalTime = readFileLineByLineToText(2020, 13).findFirst().get().toInt()
    val busId = readFileLineByLineToText(2020, 13)
        .skip(1)
        .findFirst().get()
        .split(",")
        .filter { it != "x" }
        .map { it.toInt() }
        .minWithOrNull(Comparator.comparingInt { (((arrivalTime / it) + 1) * it - arrivalTime) })
    val waitingtime = (((arrivalTime / busId!!) + 1) * busId - arrivalTime)
    println(waitingtime * busId)

    printDay(2)
    println("Brute-forcing does not work =(")
    print("Let's get the golden coin: ")
    val busses = readFileLineByLineToText(2020, 13)
        .skip(1)
        .findFirst().get()
        .split(",")
        .withIndex()
        .filter { it.value != "x" }

    val allNumbersMultiplied = busses.multiplyLong { it.value.toLong() }
    var timestamp = busses.first().value.toLong()
    var toAdd: Long = timestamp
    for (i in 1 until busses.size) {
        timestamp = (timestamp..allNumbersMultiplied step toAdd).first { (it + busses[i].index) % busses[i].value.toLong() == 0L }
        toAdd *= busses[i].value.toLong()
    }
    println(timestamp)
}