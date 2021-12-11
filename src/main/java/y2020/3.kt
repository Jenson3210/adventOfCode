package y2020

import utils.printDay
import utils.readFileLineByLineToText
import kotlin.streams.toList


fun main() {
    printDay(1)
    print("Trees encountered: ")
    println(getArea(readFileLineByLineToText(2020, 3).toList()).walkthrough(0,0,3,1).filter { it.tree }.count())

    printDay(2)
    print("Trees encountered: ")
    println(
        1L
        *   getArea(readFileLineByLineToText(2020, 3).toList()).walkthrough(0,0,1,1).filter { it.tree }.count()
        *   getArea(readFileLineByLineToText(2020, 3).toList()).walkthrough(0,0,3,1).filter { it.tree }.count()
        *   getArea(readFileLineByLineToText(2020, 3).toList()).walkthrough(0,0,5,1).filter { it.tree }.count()
        *   getArea(readFileLineByLineToText(2020, 3).toList()).walkthrough(0,0,7,1).filter { it.tree }.count()
        *   getArea(readFileLineByLineToText(2020, 3).toList()).walkthrough(0,0,1,2).filter { it.tree }.count()
    )
}

private class Area(val rows: List<Row>, val rowLength: Int){

    fun walkthrough(xStart: Int, yStart: Int, rightStepLength: Int, downStepLength: Int): List<Point> {
        val points = mutableListOf<Point>()
        var x = xStart;
        var y = yStart;
        var startingPoint = step(x, y, 0, 0)
        do {
            startingPoint = step(x, y, rightStepLength, downStepLength);
            x = startingPoint.x
            y = startingPoint.y
            points.add(startingPoint)
        } while (startingPoint.isInArea(this))
        return points
    }

    private fun step(x: Int, y: Int, rightSteps: Int, downSteps: Int): Point {
        var xIndex = x + rightSteps
        val yIndex = y + downSteps
        if (rows.size <= yIndex) {
            return Point(yIndex, xIndex, false);
        }

        if (rowLength <= xIndex) {
            xIndex -= rowLength
        }
        return rows.get(yIndex).points.get(xIndex)
    }
}

private class Row(val points: List<Point>) {
}

private class Point(val x: Int, val y: Int, val tree: Boolean){
    fun isInArea(area:Area): Boolean {
        if (area.rows.size > x) return true
        return false
    }
}

private fun getArea(inputlines: List<String>) = Area(inputlines.mapIndexed { index, line -> getRow(line, index) }, inputlines.first().length)
private fun getRow(inputLine: String, rowNumber: Int) = Row(inputLine.toList().mapIndexed { index, char -> Point(index, rowNumber, isTree(char)) })
private fun isTree(c: Char) = c == '#';