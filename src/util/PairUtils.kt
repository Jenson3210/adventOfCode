fun Collection<Pair<Int, Int>>.printPairOfOrColsAndRows() {
    val rows = this.maxOf{ it.second }
    val cols = this.maxOf{ it.first }

    (0..rows).forEach { row ->
        val rowString = (0..cols).joinToString("") { col ->
            if (this.contains(Pair(col, row))) {
                "#"
            } else {
                " "
            }
        }

        println(rowString)
    }
}

fun Collection<Pair<Int, Int>>.printPairOfOrRowsAndCols() {
    this.print(Pair<Int, Int>::first, Pair<Int, Int>::second )
}

fun Collection<Pair<Int, Int>>.print(rowGetter: (Pair<Int, Int>) -> Int, colGetter: (Pair<Int, Int>) -> Int) {
    val rows = this.maxOf{ rowGetter.invoke(it) }
    val cols = this.maxOf{ colGetter.invoke(it) }

    (0..rows).forEach { row ->
        val rowString = (0..cols).joinToString("") { col ->
            if (this.any { rowGetter.invoke(it) == row && colGetter.invoke(it) == col }) {
                "#"
            } else {
                " "
            }
        }

        println(rowString)
    }
}