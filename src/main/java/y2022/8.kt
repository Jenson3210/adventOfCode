package y2022

import utils.*
import java.util.stream.Stream

fun main() {
    printDay(1)
    println(countVisibleTrees(readNthCharsOfEquallyLongLines(2022, 8).toTreeSights()))

    printDay(2)
    println(calculateMaxScenicScore(readFileLineByLineToText(2022, 8)))
}

private fun countVisibleTrees(linesOfSight: List<List<Tree>>): Int {
    return linesOfSight.flatMap {
        it.fold(mutableListOf<Tree>()) { acc, tree ->
            if (acc.isEmpty() || tree.height > acc.maxOf { it.height }) {
                acc.add(tree)
            }
            acc
        }
    }.distinctBy { it.row to it.col }.count()
}

private fun calculateMaxScenicScore(map: Stream<String>): Tree {
    val trees = map.map { it.toList() }.map { it.map { Tree(height = it.digitToInt()) } }.toList().toNavigateableMap()
    trees.forEach {tree ->
        tree.scenicScore = listOf(trees::getNorthCell, trees::getSouthCell, trees::getWestCell, trees::getEastCell).multiplyLong { direction ->
            var score = 0L
            var lookingFrom: Tree = tree
            var lookingAt: Tree?
            do {
                lookingAt = direction.invoke(lookingFrom.row, lookingFrom.col)
                if (lookingAt != null) {
                    score ++
                    lookingFrom = lookingAt
                }
            } while (lookingAt != null && lookingAt.height < tree.height)
            score
        }
    }

    return trees.maxBy { it.scenicScore }
}

private data class Tree(var row: Int = -1, var col: Int = -1, val height: Int, var scenicScore: Long = -1) :
    PositionAware {
    override fun setPosition(x: Int, y: Int) {
        row = x
        col = y
    }
}

private fun List<List<Char>>.toTreeSights(): List<List<Tree>> {
    val linesOfSight: MutableList<List<Tree>> = mutableListOf()

    (0 until this.first().size).forEach { index ->
        linesOfSight.add(this[index].mapIndexed { rowIndex, char -> Tree(rowIndex, index, char.digitToInt()) })
        linesOfSight.add(this[index].mapIndexed { rowIndex, char -> Tree(rowIndex, index, char.digitToInt()) }
            .reversed())
        linesOfSight.add(this.mapIndexed { colIndex, it -> Tree(index, colIndex, it[index].digitToInt()) }.toList())
        linesOfSight.add(
            this.mapIndexed { colIndex, it -> Tree(index, colIndex, it[index].digitToInt()) }.reversed().toList()
        )
    }

    return linesOfSight
}
