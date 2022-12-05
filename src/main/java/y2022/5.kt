package y2022

import utils.printDay
import utils.readFileToText
import java.util.*

fun main() {
    printDay(1)
    println(getTopOfStackUsingCrateMover3000(readFileToText(2022, 5)))

    printDay(2)
    println(getTopOfStackUsingCrateMover3001(readFileToText(2022, 5)))
}

private fun getTopOfStackUsingCrateMover3000(input: String): String {
    val currentSituation = input.split("\n\n").first()
    val craneOperations = input.split("\n\n").last().split("\n").dropLast(1)

    val situation = currentSituation.toQueues()
    craneOperations.forEach {
        val from = it.getFrom()
        val to = it.getTo()
        val amount = it.getAmount()
        (1 .. amount).forEach { _ ->
            situation[to]!!.add(situation[from]!!.pollLast())
        }
    }
    return (1 .. 9).map { situation[it]!!.last }.joinToString("")
}

private fun getTopOfStackUsingCrateMover3001(input: String): String {
    val currentSituation = input.split("\n\n").first()
    val craneOperations = input.split("\n\n").last().split("\n").dropLast(1)

    val situation = currentSituation.toQueues()
    craneOperations.forEach {
        val from = it.getFrom()
        val to = it.getTo()
        val amount = it.getAmount()
        situation[to]!!.addAll(situation[from]!!.takeLast(amount))
        (1 .. amount).forEach { _ ->
            situation[from]!!.pollLast()
        }
    }
    return (1 .. 9).map { situation[it]!!.last }.joinToString("")
}

private fun String.toQueues(): Map<Int, LinkedList<Char>> {
    val initialMap = (1..9).associateWith { LinkedList<Char>() }
    return this.split("\n")
        .dropLast(1)
        .asReversed()
        .map { it.toContainers() }
        .fold(initialMap) { acc, row ->
            row.forEach { acc[it.first]!!.add(it.second) }
            acc
        }
}

private fun String.toContainers(): List<Pair<Int, Char>> {
    return (0 .. 8)
        .asSequence()
        .map { it + 1 to 1 + (it * 4) }
        .map { it.first to this.getOrNull(it.second) }
        .filter { it.second != null && it.second != ' ' }
        .map { it.first to it.second!! }
        .toList()
}

private fun String.getAmount(): Int {
    return this.parse().split(",")[0].toInt()
}

private fun String.getFrom(): Int {
    return this.parse().split(",")[1].toInt()
}

private fun String.getTo(): Int {
    return this.parse().split(",")[2].toInt()
}


private fun String.parse(): String {
    return this
        .replace("move ", "")
        .replace(" from ", ",")
        .replace(" to ", ",")
}
