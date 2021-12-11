package utils

import java.util.function.Predicate

data class Table<T>(private val rows: MutableList<TableRow<T>> = mutableListOf()) {
    fun getRow(index: Int) = rows[index].columns
    fun getColumn(index: Int) = rows.map { it.getCell(index) }
    fun getCell(rowIndex: Int, colIndex: Int) = rows[rowIndex].getCell(colIndex)
    fun addRow(row: List<T>) = rows.add(TableRow(row))
    private fun getColumns() = (0 until rows[0].columns.size).map { getColumn(it) }

    fun hasColumnOrRowMatchingPredicateCompletely(predicate: Predicate<T>) = hasRowMatchingPredicateCompletely(predicate) || hasColumnMatchingPredicateCompletely(predicate)
    fun hasRowMatchingPredicateCompletely(predicate: Predicate<T>) = rows.any { it.matchesPredicateCompletely(predicate) }
    fun hasColumnMatchingPredicateCompletely(predicate: Predicate<T>) = getColumns().any { it.all { predicate.test(it) } }
    fun getCellsMatchingPredicate(predicate: Predicate<T>) = rows.flatMap { it.columns }.filter { predicate.test(it) }
}

data class TableRow<T>(val columns: List<T>) {
    fun getCell(index: Int) = columns[index]

    fun matchesPredicateCompletely(predicate: Predicate<T>) = columns.all { predicate.test(it) }
}
