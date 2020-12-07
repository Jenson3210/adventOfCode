package `2020`.`06`

import util.printDay
import util.readFile
import java.io.File

fun main() {
    printDay(1)
    print("Total amount of questions answered positively: ")
    println(getGroups(readFile("2020_06.txt")).map { it.allPositiveAnswers().size }.sum())

    printDay(2)
    print("Total amount of common questions answered positively: ")
    println(getGroups(readFile("2020_06.txt")).map { it.allCommonPositiveAnswers().size }.sum())
}

private class Group(val answers: List<String>) {
    fun allPositiveAnswers(): Set<Char> {
        return answers.flatMap { it.toList() }.toSet()
    }

    fun allCommonPositiveAnswers(): Set<Char> {
        return answers.flatMap { it.toList() }.groupBy { it }.entries.filter { it.value.size == answers.size }.map { it.key }.toSet()
    }
}

private fun getGroups(inputFile : File): List<Group> {
    val groups: MutableList<Group> = mutableListOf()
    var answers: MutableList<String> = mutableListOf()

    inputFile.forEachLine {
        if (it.isNotBlank()) {
            answers.add(it)
        } else {
            groups.add(Group(answers))
            answers = mutableListOf()
        }
    }
    groups.add(Group(answers))

    return groups
}