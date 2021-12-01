package `2020`.`01`

import util.cartesianProduct
import util.printDay
import util.readFileLineByLineToInt
import kotlin.streams.toList


fun main() {
    printDay(1)
    ExpenseReport(readFileLineByLineToInt("2020_01.txt").mapToObj { Expense(it) }.toList())
        .getPairsThatSumTo(2020)
        .distinctBy { it.getMultiplied() }
        .forEach { println("${it.getMultiplied()}") }

    printDay(2)
    ExpenseReport(readFileLineByLineToInt("2020_01.txt").mapToObj { Expense(it) }.toList())
        .getTripletsThatSumTo(2020)
        .distinctBy { it.getMultiplied() }
        .forEach { println("${it.getMultiplied()}") }
}

private class ExpenseReport(val expenses: List<Expense>) {
    fun getPairsThatSumTo(sum: Int): List<ExpenseCombination> {
        return cartesianProduct(expenses, expenses).map { ExpenseCombination(it as List<Expense>) }
            .filter { it.getSum() == sum }.toList();
    }

    fun getTripletsThatSumTo(sum: Int): List<ExpenseCombination> {
        return cartesianProduct(expenses, expenses, expenses).map { ExpenseCombination(it as List<Expense>) }
            .filter { it.getSum() == sum }.toList();
    }
}

private class Expense(val value: Int)

private class ExpenseCombination(val expenses: List<Expense>) {
    fun getMultiplied(): Int = expenses.map { it.value }.reduce { acc, expense -> acc * expense }
    fun getSum(): Int = expenses.map { it.value }.reduce { acc, expense -> acc + expense }
}

