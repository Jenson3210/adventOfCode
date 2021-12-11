package y2020

import utils.printDay
import utils.readFileLineByLineToText
import utils.sumByLong
import java.util.*
import java.util.stream.Stream

fun main() {
    printDay(1)
    print("Winning score: ")
    println(Game(readFileLineByLineToText(2020, 22)).play().getScore())

    printDay(2)
    print("Winning score recursive: ")
    println(Game(readFileLineByLineToText(2020, 22)).playRecursively().getScore())
}

private class Game {
    val decks: List<MutableList<Int>>
    var winner: Int? = null

    constructor(input: Stream<String>) {
        val decks = mutableListOf<MutableList<Int>>()
        var deck = mutableListOf<Int>()

        input.forEach {
            when {
                it.contains(":") -> deck = mutableListOf()
                it.isBlank() -> decks.add(deck)
                else -> deck.add(it.toInt())
            }
        }
        decks.add(deck)

        this.decks = decks
    }

    constructor(decks: List<MutableList<Int>>) {
        this.decks = decks
    }

    fun play() : Game {
        while (decks.none { it.isEmpty() }) {
            val goTo = decks.maxByOrNull { it[0] }!!
            val list = decks.map { it.removeAt(0) }.sortedDescending()
            goTo.addAll(list)
        }
        winner = decks.withIndex().first { it.value.isNotEmpty() }.index
        return this
    }

    fun getScore() : Long {
        return decks[winner!!].reversed().withIndex().sumByLong { it.value.toLong() * (it.index + 1).toLong() }
    }

    fun playRecursively() : Game {
        val history: MutableSet<Int> = mutableSetOf()
        while (decks.none { it.isEmpty() }) {
            if (!history.add(Objects.hash(decks[0], decks[1]))) {
                winner = 0
                return this
            } else {
                var goTo: MutableList<Int>
                val list = decks.map { it[0] }.let { it[0] to it[1] }
                if (list.first < decks[0].size && list.second < decks[1].size) {
                    val deckA = decks[0].take(list.first + 1).drop(1).toMutableList()
                    val deckB = decks[1].take(list.second + 1).drop(1).toMutableList()
                    val winner = Game(listOf(deckA, deckB)).playRecursively().winner!!
                    goTo = decks[winner]
                    if (winner == 1) {
                        goTo.add(list.second)
                        goTo.add(list.first)
                    }
                    else {
                        goTo.add(list.first)
                        goTo.add(list.second)
                    }
                } else {
                    goTo = decks.maxByOrNull { it[0] }!!
                    goTo.addAll(list.toList().sortedDescending())
                }
                decks.forEach { it.removeAt(0) }
            }
        }
        winner = decks.withIndex().first { it.value.isNotEmpty() }.index
        return this
    }
}