package `2021`.`09`

import util.multiplyLong
import util.printDay
import util.readFileLineByLineToText
import kotlin.streams.toList

fun main() {
    printDay(1)
    println(getRiskLevel(readFileLineByLineToText("2021_09.txt").toList()))

    printDay(2)
    println(getBasins(readFileLineByLineToText("2021_09.txt").toList()))
}

private fun getRiskLevel(input: List<String>): Long {
    var riskLevel = 0L

    input.forEachIndexed { rowIndex, rowData ->
        rowData.toList().map { Character.getNumericValue(it) }.forEachIndexed { colIndex, colData ->
            if ((rowIndex == 0 || Character.getNumericValue(input[rowIndex-1][colIndex]) > colData) &&
                (rowIndex + 1 == input.size || Character.getNumericValue(input[rowIndex+1][colIndex]) > colData) &&
                (colIndex == 0 || Character.getNumericValue(input[rowIndex][colIndex-1]) > colData) &&
                (colIndex + 1 == rowData.length || Character.getNumericValue(input[rowIndex][colIndex+1]) > colData)
            ) {
                riskLevel += colData + 1
            }
        } }

    return riskLevel
}

private fun getBasins(input: List<String>): Long {
    val visitedPoints = mutableSetOf<Pair<Int, Int>>()
    val basins = mutableListOf<List<Pair<Int, Int>>>()
    input.forEachIndexed { rowIndex, rowData ->
        rowData.toList().map { Character.getNumericValue(it) }.forEachIndexed { colIndex, colData ->
            if (!visitedPoints.contains(Pair(rowIndex, colIndex))) {
                if (colData == 9) {
                    visitedPoints.add(Pair(rowIndex, colIndex))
                } else {
                    val basin = getBasin(input, rowIndex, colIndex, visitedPoints)
                    basins.add(basin)
                }
            }
        } }

    return basins.map { it.size }.sortedDescending().take(3).multiplyLong { it * 1L }
}

private fun getBasin(input: List<String>, rowIndex: Int, colIndex: Int, visitedPoints: MutableSet<Pair<Int, Int>>) : List<Pair<Int, Int>> {
    visitedPoints.add(Pair(rowIndex, colIndex))
    return getSurroundingPoints(input, rowIndex, colIndex, visitedPoints)
        .filterNot { Character.getNumericValue(input[it.first][it.second]) == 9 }
        .fold(listOf(Pair(rowIndex, colIndex))) { basin, (row, col) ->
            basin + getBasin(input, row, col, visitedPoints) }
        .distinct()
}

private fun getSurroundingPoints(input: List<String>, rowIndex: Int, colIndex: Int, visitedPoints: MutableSet<Pair<Int, Int>>): List<Pair<Int, Int>> {
    return listOf(Pair(-1, 0), Pair(0, -1), Pair(1, 0), Pair(0, 1))
        .map { Pair(rowIndex + it.first, colIndex + it.second) }
        .filterNot { visitedPoints.contains(it) }
        .filter { input.indices.contains(it.first) && input[0].indices.contains(it.second) }
}

