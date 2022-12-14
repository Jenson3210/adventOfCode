package utils

fun Iterable<Pair<Int, Int>>.printPairOfOrColsAndRows() {
    val minRows = this.minOf { it.first }
    val maxRows = this.maxOf { it.first }
    val minCols = this.minOf { it.second }
    val maxCols = this.maxOf { it.second }

    (minRows..maxRows).forEach { row ->
        val rowString = (minCols..maxCols).joinToString("") { col ->
            val index = this.indexOf(Pair(row, col))
            if (index >= 0) {
                "#"
            } else {
                "."
            }
        }

        println(rowString)
    }
}

fun <T, R> Iterable<Pair<T, R>>.groupByFirst(): MutableMap<T, MutableList<R>> {
    val map = mutableMapOf<T, MutableList<R>>()
    this.forEach {
        map.getOrPut(it.first) { mutableListOf() }.add(it.second)
    }
    return map
}

fun Pair<Int, Int>.nonDiagonalLine(destinationCoordinate: Pair<Int, Int>): List<Pair<Int, Int>> {
    val minX = listOf(this.first, destinationCoordinate.first).min()
    val maxX = listOf(this.first, destinationCoordinate.first).max()
    val minY = listOf(this.second, destinationCoordinate.second).min()
    val maxY = listOf(this.second, destinationCoordinate.second).max()
    if (minX != maxX && minY != maxY) throw Exception("No diagonal lines supported for now")

    return (minX..maxX).flatMap { x ->
        (minY..maxY).map { y ->
            x to y
        }
    }
}
