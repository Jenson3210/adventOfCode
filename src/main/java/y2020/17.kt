package y2020

import utils.printDay
import utils.readFileLineByLineToText
import kotlin.math.ceil
import kotlin.streams.toList

fun main() {
    printDay(1)
    print("Active particles after 6 runs: ")
    println(Cube(readFileLineByLineToText(2020, 17).toList(), 6).applyRules(6).countActive())

    printDay(2)
    println("Brute-forcing it :)")
    print("Active particles after 6 runs: ")
    println(HyperCube(readFileLineByLineToText(2020, 17).toList(), 6).applyRules(6).countActive())
}

private class HyperCube(rows: List<String>, maxRuns: Int) {
    var cubes: Array<Array<Array<Array<Boolean>>>>
    var size: Int = rows.size
    val arraySize = (ceil(size/2.0).toInt()*4) + (maxRuns * 2)

    init {
        this.cubes =
            Array(arraySize) {
                Array(arraySize) {
                    Array(arraySize) {
                        Array(arraySize) { false }
                    }
                }
            }
        val startIndex = arraySize - (maxRuns * 2) - 2
        rows.withIndex().forEach { row ->
            row.value.forEachIndexed { columnIndex, column ->
                if (column.toUpperCase() == '#') {
                    this.cubes[startIndex][startIndex][startIndex + row.index][startIndex + columnIndex] = true
                }
            }
        }
    }

    fun countActive() : Int {
        return cubes.flatMap { it.flatMap { slice -> slice.flatMap { row -> row.map { col -> col } } } }.count { it }
    }

    fun applyRules(times: Int) : HyperCube {
        for (i in 1..times) {
            val tempCubes =
                Array(arraySize) {
                    Array(arraySize) {
                        Array(arraySize) {
                            Array(arraySize) { false }
                        }
                    }
                }
            cubes.forEachIndexed { cubeIndex, cube ->
                cube.forEachIndexed { sliceIndex, slice ->
                    slice.forEachIndexed { rowIndex, row ->
                        row.forEachIndexed { colIndex, col ->
                            tempCubes[cubeIndex][sliceIndex][rowIndex][colIndex] = getNewState(cubeIndex, sliceIndex, rowIndex, colIndex)
                        }
                    }
                }
            }
            cubes = tempCubes
            size++
        }

        return this
    }

    private fun getNewState(cube: Int, slice: Int, row: Int, col: Int) : Boolean {
        if (cubes[cube][slice][row][col]) {
            if (getNeighbors(cube, slice, row, col).filter { it }.count() !in 2..3) {
                return false
            }
            return true
        }
        if (getNeighbors(cube, slice, row, col).filter { it }.count() == 3) {
            return true
        }
        return false
    }

    private fun getNeighbors(cube: Int, slice: Int, row: Int, col: Int) : List<Boolean> {
        val list = mutableListOf<Boolean>()
        for (cubeIndex in cubes.indices.intersect(cube-1..cube+1)) {
            for (sliceIndex in cubes[cubeIndex].indices.intersect(slice - 1..slice + 1)) {
                for (rowIndex in cubes[cubeIndex][sliceIndex].indices.intersect(row - 1..row + 1)) {
                    for (colIndex in cubes[cubeIndex][sliceIndex][rowIndex].indices.intersect(col - 1..col + 1)) {
                        if (cubeIndex != cube || sliceIndex != slice || rowIndex != row || colIndex != col) {
                            list.add(cubes[cubeIndex][sliceIndex][rowIndex][colIndex])
                        }
                    }
                }
            }
        }

        return list
    }
}

private class Cube(rows: List<String>, maxRuns: Int) {
    var slices: Array<Array<Array<Boolean>>>
    var size: Int = rows.size
    val arraySize = (ceil(size/2.0).toInt()*4) + (maxRuns * 2)

    init {
        this.slices =
            Array(arraySize) {
                Array(arraySize) {
                    Array(arraySize) {false}
                }
            }
        val startIndex = arraySize - (maxRuns * 2) - 2
        rows.withIndex().forEach { row ->
            row.value.forEachIndexed { columnIndex, column ->
                if (column.toUpperCase() == '#') {
                    this.slices[startIndex][startIndex + row.index][startIndex + columnIndex] = true
                }
            }
        }
    }

    fun countActive() : Int {
        return slices.flatMap { slice -> slice.flatMap { row -> row.map { col -> col } } }.count { it }
    }

    fun applyRules(times: Int, debug: Boolean = false) : Cube {
        for (i in 1..times) {
            val tempSlices =
                Array(arraySize) {
                    Array(arraySize) {
                        Array(arraySize) {false}
                    }
                }
            slices.forEachIndexed { sliceIndex, slice ->
                slice.forEachIndexed { rowIndex, row ->
                    row.forEachIndexed { colIndex, col ->
                        tempSlices[sliceIndex][rowIndex][colIndex] = getNewState(sliceIndex, rowIndex, colIndex)
                    }
                }
            }
            slices = tempSlices

            if(debug) {
                println("After $i cycle(s):")
                slices.forEachIndexed { sliceIndex, slice ->
                    println()
                    println("Z=$sliceIndex")
                    slice.forEach { println(String(it.map { if (it) '#' else '.' }.toCharArray())) }
                    println()
                }
            }
            size++
        }

        return this
    }

    private fun getNewState(slice: Int, row: Int, col: Int) : Boolean {
        if (slices[slice][row][col]) {
            if (getNeighbors(slice, row, col).filter { it }.count() !in 2..3) {
                return false
            }
            return true
        }
        if (getNeighbors(slice, row, col).filter { it }.count() == 3) {
            return true
        }
        return false
    }

    private fun getNeighbors(slice: Int, row: Int, col: Int) : List<Boolean> {
        val list = mutableListOf<Boolean>()
        for (sliceIndex in slices.indices.intersect(slice-1..slice+1)) {
            for (rowIndex in slices[sliceIndex].indices.intersect(row-1..row+1)) {
                for (colIndex in slices[sliceIndex][rowIndex].indices.intersect(col-1..col+1)) {
                    if (sliceIndex != slice || rowIndex != row || colIndex != col) {
                        list.add(slices[sliceIndex][rowIndex][colIndex])
                    }
                }
            }
        }

        return list
    }
}