package y2022

import utils.*

fun main() {
    printDay(1)
    println(travel())

    printDay(2)
    println(scenicRoute())
}

private fun travel(): Int {
    val navigableMap = readNthCharsOfEquallyLongLines(2022, 12)
        .map { it.map { Position(it.toValue()) } }
        .toNavigateableMap()

    val graph = navigableMap.toStepCalculatingDijkstra(travelCondition = { from: Position, to: Position -> from.value.toElevationValue() >= to.value.toElevationValue() - 1 })
    val startingPoint = graph.nodes.filterValues { node2D -> node2D.position == navigableMap.first { it.value == 0 } }.toList().first().second
    graph.calculateShortestPathFromSource(startingPoint)
    val endPoint = graph.nodes.filterValues { node2D -> node2D.position == navigableMap.first { it.value == 27 } }.toList().first().second

    return endPoint.distance
}

private fun scenicRoute(): Int {
    val navigableMap = readNthCharsOfEquallyLongLines(2022, 12)
        .map { it.map { Position(it.toValue()) } }
        .toNavigateableMap()

    val graph = navigableMap.toStepCalculatingDijkstra(travelCondition = { to: Position, from: Position -> from.value.toElevationValue() >= to.value.toElevationValue() - 1 })
    val startingPoint = graph.nodes.filterValues { node2D -> node2D.position == navigableMap.first { it.value == 27 } }.toList().first().second
    graph.calculateShortestPathFromSource(startingPoint)
    return graph.nodes.filterValues { node2D -> navigableMap.filter { it.value == 0 || it.value == 1 }.contains(node2D.position) }.minOf { it.value.distance }
}

private data class Position(val value: Int, var row: Int = 0, var col: Int = 0): PositionAware {

    override fun setPosition(x: Int, y: Int) {
        row = x
        col = y
    }
}

private fun Int.toElevationValue(): Int {
    if (this == 0) return 1
    if (this == 27) return 26
    return this
}

private fun Char.toValue(): Int {
    return "SabcdefghijklmnopqrstuvwxyzE".indexOf(this)
}
