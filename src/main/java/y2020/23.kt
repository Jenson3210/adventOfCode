package y2020

import utils.CircleLinkedSet
import utils.multiplyLong
import utils.printDay
import utils.readFileToText

fun main() {
    printDay(1)
    print("End score: ")
    println(CupsGame(readFileToText(2020, 23)).play(100).getScore())

    printDay(2)
    print("Massive game: ")
    println(CupsGame(readFileToText(2020, 23)).playLargeGame(10000000).cups.takeFrom(1, 3).drop(1).multiplyLong { it.toLong() })
}

private class CupsGame(input: String) {
    var cups = CircleLinkedSet(input.map { Character.getNumericValue(it) })

    fun playLargeGame(times: Int): CupsGame {
        cups = CircleLinkedSet(cups.toList() + (10..1000000).toList())
        play(times)
        return this
    }

    fun play(times: Int): CupsGame {
        val max = cups.size

        (0 until times).forEach {
            val current = cups.first()
            val pickedUpCups = cups.rotate().pop(3)

            var destinationCup = current - 1
            while (destinationCup in pickedUpCups || destinationCup < 1) {
                if (destinationCup < 1) {
                    destinationCup = max
                } else {
                    destinationCup --
                }
            }
            cups.insertAfter(destinationCup, pickedUpCups)
        }
        return this
    }

    fun getScore(): String {
        return cups.toList().plus(cups.toList()).joinToString("").substringAfter("1").substringBefore("1")
    }
}