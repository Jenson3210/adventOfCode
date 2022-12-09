package utils

fun Collection<Pair<Int, Int>>.printPairOfOrColsAndRows() {
    val minRows = this.minOf{ it.first }
    val maxRows = this.maxOf{ it.first }
    val minCols = this.minOf{ it.second }
    val maxCols = this.maxOf{ it.second }

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
