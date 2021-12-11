package y2021

import utils.printDay
import utils.readFileLineByLineToText
import utils.sumByLong
import kotlin.streams.toList

fun main() {
    printDay(1)
    println(readFileLineByLineToText(2021, 10).toList().sumByLong { it.findUnclosedDelimiterScore() })

    printDay(2)
    val sortedScores = readFileLineByLineToText(2021, 10).toList().map { it.findUnfinishedDelimiterScore() }.filterNot { it == 0L }.sorted()
    println(sortedScores[(sortedScores.size / 2)])
}

private fun String.findUnclosedDelimiterScore(print: Boolean = false): Long {
    val openDelimiters = mutableListOf<Char>()
    this.forEach {
        if (it.isOpeningChar()) {
            openDelimiters.add(it)
        } else if (it.isClosingChar()) {
            val lastOpened = openDelimiters.removeLast()
            if (lastOpened.toClosingChar() != it) {
                if (print) println("Expected $lastOpened but found $it instead.")
                return it.toDayOneScore()
            }
        } else {
            throw Exception("Should not come here for $it")
        }
    }

    return 0
}

private fun String.findUnfinishedDelimiterScore(print: Boolean = false): Long {
    val openDelimiters = mutableListOf<Char>()
    this.forEach {
        if (it.isOpeningChar()) {
            openDelimiters.add(it)
        } else if (it.isClosingChar()) {
            val lastOpened = openDelimiters.removeLast()
            if (lastOpened.toClosingChar() != it) {
                return 0
            }
        } else {
            throw Exception("Should not come here for $it")
        }
    }

    if (print) println("Complete by adding ${openDelimiters.reversed().map { it.toClosingChar() }.joinToString(separator = "")}.")
    return openDelimiters.reversed().map { it.toClosingChar() }.fold(0L) { score, c -> (score * 5) + c.toDayTwoScore() }
}

private fun Char.toClosingChar(): Char {
    when (this) {
        '(' -> return ')'
        '[' -> return ']'
        '{' -> return '}'
        '<' -> return '>'
    }

    return this
}

private fun Char.toDayOneScore(): Long {
    when (this) {
        ')' -> return 3
        ']' -> return 57
        '}' -> return 1197
        '>' -> return 25137
    }

    return 0
}

private fun Char.toDayTwoScore(): Long {
    when (this) {
        ')' -> return 1
        ']' -> return 2
        '}' -> return 3
        '>' -> return 4
    }

    return 0
}

private fun Char.isOpeningChar(): Boolean = listOf('{', '[', '<', '(').contains(this)
private fun Char.isClosingChar(): Boolean = listOf('}', ']', '>', ')').contains(this)