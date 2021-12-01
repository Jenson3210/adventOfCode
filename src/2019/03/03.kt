package `2019`.`03`

import util.readFileLineByLineToText
import kotlin.math.abs
import kotlin.streams.toList

//

fun main() {
    val lines = readFileLineByLineToText("2019_03.txt").map { Line(it.toDirections()) }.toList()
    val junction1 = lines[0].points.intersect(lines[1].points).filter { it != Point(x = 0, y= 0, steps = 0) }
    val junction2 = lines[1].points.intersect(junction1)
    println("A: " + (junction1.map { it.getManhattanDistance() }.minOrNull()))
    println("B: " + ((junction1 + junction2).groupByTo(HashMap(), { it }, { it.steps }).values.map { it.reduce { i1, i2 -> i1!!.plus(i2!!) } }.sortedBy { it }.firstOrNull()))
}


private fun String.toDirections(): List<Direction> {
    return this.split(",").map { Direction(it.elementAt(0), it.substring(1).toInt()) }.toList()
}

private class Direction(val direction: Char, val amount: Int)
private class Line(directions: List<Direction>) {
    var basePoint: Point = Point(x = 0, y = 0, steps = 0 )
    val points: MutableList<Point> = mutableListOf(basePoint)
    var lastPoint: Point = basePoint
    var steps: Int = 0

    init {
        for (direction in directions) {
            when (direction.direction) {
                'U' -> move('U', direction.amount, 0, 1)
                'D' -> move('D', direction.amount, 0, -1)
                'R' -> move('R', direction.amount, 1, 0)
                'L' -> move('L', direction.amount, -1, 0)
                else -> throw IllegalArgumentException()
            }
        }
    }

    private fun move(direction: Char, amount: Int, xMovement: Int, yMovement: Int) {
        for (i in 1..amount) {
            steps ++
            if (points.find { it.x == lastPoint.x + xMovement && it.y == lastPoint.y + yMovement } == null) {
                lastPoint = Point(lastPoint.x + xMovement, lastPoint.y + yMovement, direction, steps = steps)
                points.add(lastPoint)
            } else {
                lastPoint = points.find { it.x == lastPoint.x + xMovement && it.y == lastPoint.y + yMovement }!!
                lastPoint.split = true
            }
        }
    }
}

private class Point(val x: Int, val y: Int, var direction: Char = 'O', var split: Boolean = false, var steps: Int?) {
    fun getManhattanDistance() = abs(x) + abs(y)
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Point

        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        return result
    }

    override fun toString(): String {
        return "{x: $x, y: $y, direction:" + if (split) "B}" else "$direction}"
    }
}