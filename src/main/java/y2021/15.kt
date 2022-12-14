package y2021

import utils.*

fun main() {
    printDay(1)
    println(getLowestRiskLevel())

    printDay(2)
    println(getLowestRiskLevelByExpanding())
}

private fun getLowestRiskLevel(): Int {
    val map = readFileLineByLineToText(2021, 15).toList()
        .map { it.toList().map { Character.getNumericValue(it) } }.toNavigateableMap()

    val graph = map.toDijkstraGraph()
    graph.calculateShortestPathFromSource(graph.nodes[0 to 0]!!)

    return graph.nodes[map.rowIndices().last to map.colIndices().last]!!.distance
}

private fun getLowestRiskLevelByExpanding(): Int {
    val map = readFileLineByLineToText(2021, 15).toList()
        .map { it.toList().map { Character.getNumericValue(it) } }
        .expandToNavigateableMap()

    val graph = map.toDijkstraGraph()
    graph.calculateShortestPathFromSource(graph.nodes[0 to 0]!!)

    return graph.nodes[map.rowIndices().last to map.colIndices().last]!!.distance
}

fun List<List<Int>>.expandToNavigateableMap(): NavigateableMap<Int> {
    val map = mutableMapOf<Int, Map<Int,Int>>()
    this.forEachIndexed { rowIndex, row ->
        val newRow = mutableMapOf<Int, Int>()
        row.forEachIndexed { colIndex, col ->
            (0 until 5).forEach { newRow[colIndex + (row.size * it)] = if (col + it > 9) col + it - 9 else col + it }
        }
        map[rowIndex] = newRow
    }

    map.toMap().forEach { (rowIndex, row) ->
        (1 until 5).forEach {
        map[rowIndex + (this.size * it)] = row.map { (colIndex, col) -> colIndex to if (col + it > 9) col + it - 9 else col + it }.toMap() }
    }

    return NavigateableMap(map)
}
