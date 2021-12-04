package `2021`.`04`

import util.Table
import util.printDay
import util.readFileLineByLineToText
import util.sumByLong

fun main() {
    printDay(1)
    println(winBingo())

    printDay(2)
    println(loseBingo())
}

private fun winBingo(): Long {
    val bingoInput = "2021_04.txt".toBingoInput()
    val bingoBoards = "2021_04.txt".toBingoBoards()

    var playing = true
    var round = 0
    var drawnNumber = 0
    var winningBoards: List<Table<BingoCell>> = emptyList()
    while (playing) {
        drawnNumber = bingoInput[round]
        bingoBoards.forEach { board ->
            board.getCellsMatchingPredicate { bingoCell -> bingoCell.number == drawnNumber }
                .forEach { it.checked = true }
        }
        winningBoards =
            bingoBoards.filter { it.hasColumnOrRowMatchingPredicateCompletely { bingoCell -> bingoCell.checked } }
        if (winningBoards.isNotEmpty()) {
            playing = false
        } else {
            round++
        }
    }

    return winningBoards[0].getCellsMatchingPredicate { bingoCell -> !bingoCell.checked }
        .sumByLong { bingoCell -> bingoCell.number * 1L } * drawnNumber
}

private fun loseBingo(): Long {
    val bingoInput = "2021_04.txt".toBingoInput()
    val bingoBoards = "2021_04.txt".toBingoBoards()

    var playing = true
    var round = 0
    var drawnNumber = 0
    while (playing) {
        drawnNumber = bingoInput[round]
        bingoBoards.forEach { board ->
            board.getCellsMatchingPredicate { bingoCell -> bingoCell.number == drawnNumber }
                .forEach { it.checked = true }
        }
        bingoBoards.removeIf { bingoBoards.size > 1 && it.hasColumnOrRowMatchingPredicateCompletely { bingoCell -> bingoCell.checked } }
        if (bingoBoards.size == 1 && bingoBoards[0].hasColumnOrRowMatchingPredicateCompletely { bingoCell -> bingoCell.checked }) {
            playing = false
        }
        round++
    }

    return bingoBoards[0].getCellsMatchingPredicate { bingoCell -> !bingoCell.checked }
        .sumByLong { bingoCell -> bingoCell.number * 1L } * drawnNumber
}

private data class BingoCell(val number: Int, var checked: Boolean = false)

private fun String.toBingoInput() =
    readFileLineByLineToText(this).limit(1).map { it.split(",") }.findFirst().get().map { it.toInt() }

private fun String.toBingoBoards(): MutableList<Table<BingoCell>> {
    val bingoBoards: MutableList<Table<BingoCell>> = mutableListOf()
    var board: Table<BingoCell> = Table()
    readFileLineByLineToText(this)
        .skip(1)
        .forEach {
            if (it.isBlank()) {
                board = Table()
                bingoBoards.add(board)
            } else {
                board.addRow(it.trim().split("\\s+".toRegex()).map { BingoCell(it.toInt()) })
            }
        }

    return bingoBoards
}