package y2022

import utils.multiplyLong
import utils.printDay
import utils.readFileToText

fun main() {
    printDay(1)
    play(readFileToText(2022, 11), 20) { value -> value / 3 }

    printDay(2)
    play(readFileToText(2022, 11), 10000) { value -> value }
}

private fun play(input: String, rounds: Int, worryLevelCalculator: (Long) -> Long) {
    val monkeyMap = input.split("\n\n")
        .mapIndexed { index, s -> index to s.toMonkey() }
        .toMap()
    val commonDivisor = input.split("\n\n").multiplyLong { 1L * it.toDivisibleBy() }

    (1..rounds).forEach { _ ->
        monkeyMap.forEach {
            it.value.play(monkeyMap, commonDivisor, worryLevelCalculator)
        }
    }
    monkeyMap.forEach {println("${it.key}: ${it.value.inspectionCount}")}
}

private data class Monkey(val items: MutableList<Long>, val operation: (Long) -> Long, val throwTo: (Long) -> Int) {
    var inspectionCount = 0L;
    fun play(monkeyMap: Map<Int, Monkey>, commonDivisible: Long, worryLevelCalculator: (Long) -> Long) {
        items.forEach {
            inspectionCount ++
            var newWorryLevel = worryLevelCalculator.invoke(operation.invoke(it))
            while (newWorryLevel > commonDivisible) {
                newWorryLevel -= commonDivisible
            }
            monkeyMap[throwTo.invoke(newWorryLevel)]!!.toss(newWorryLevel)
        }
        items.clear()
    }

    private fun toss(item: Long) {
        items.add(item)
    }
}

private fun String.toDivisibleBy() = this.substringAfter("Test: divisible by ")
    .substringBefore("\n")
    .toInt();

private fun String.toMonkey(): Monkey {
    val items: MutableList<Long> = this.substringAfter("Starting items: ")
        .substringBefore("\n")
        .split(", ")
        .map { it.toLong() }
        .toMutableList()
    val operation = { value: Long ->
        val operation = this.substringAfter("Operation: new = ")
            .substringBefore("\n")
            .replace("old", value.toString())
        if (operation.contains("+")) {
            operation.substringBefore(" + ").toLong() + operation.substringAfter(" + ").toLong()
        } else {
            operation.substringBefore(" * ").toLong() * operation.substringAfter(" * ").toLong()
        }
    }
    val throwTo = { value: Long ->
        val divisibleBy = this.toDivisibleBy()
        if (value % divisibleBy == 0L) {
            this.substringAfter("If true: throw to monkey ")
                .substringBefore("\n")
                .toInt()
        } else {
            this.substringAfter("If false: throw to monkey ")
                .substringBefore("\n")
                .toInt()
        }
    }
    return Monkey(items, operation, throwTo)
}
