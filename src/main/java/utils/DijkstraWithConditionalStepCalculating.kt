package utils

class Graph2D<T> {
    val nodes: MutableMap<Pair<Int, Int>, Node2D<T>> = mutableMapOf()
}

class Node2D<T>(val position: T){
//    var shortestPath: LinkedHashSet<Node> = LinkedHashSet()
    var distance: Int = Int.MAX_VALUE
    val adjacentNodes: MutableMap<Node2D<T>, Int> = mutableMapOf()

    override fun toString(): String {
        return "$position:$distance)"
    }
}
fun <T> NavigateableMap<T>.toStepCalculatingDijkstra(stepSizeCalculator: (from: T, to: T) -> Int = { _, _ -> 1 }, travelCondition: (from: T, to: T) -> Boolean = { _, _ -> true }): Graph2D<T> {
    val graph = Graph2D<T>()
    val allCoordinates = cartesianProduct(this.rowIndices().toList(), this.colIndices().toList()).map { it[0] as Int to it[1] as Int }

    graph.nodes.putAll(allCoordinates.map { Pair(it.first to it.second,  Node2D(this.getCell(it.first, it.second)!!)) })
    graph.nodes.forEach { (coordinate, node) ->
        val row = coordinate.first
        val col = coordinate.second

        var neighbour: T? = this.getNorthCell(row, col)
        if (neighbour != null && travelCondition.invoke(node.position, neighbour)) {
            node.adjacentNodes[graph.nodes[row-1 to col]!!] = stepSizeCalculator.invoke(node.position, neighbour)
        }
        neighbour = this.getEastCell(row, col)
        if (neighbour != null && travelCondition.invoke(node.position, neighbour)) {
            node.adjacentNodes[graph.nodes[row to col+1]!!] = stepSizeCalculator.invoke(node.position, neighbour)
        }
        neighbour = this.getSouthCell(row, col)
        if (neighbour != null && travelCondition.invoke(node.position, neighbour)) {
            node.adjacentNodes[graph.nodes[row+1 to col]!!] = stepSizeCalculator.invoke(node.position, neighbour)
        }
        neighbour = this.getWestCell(row, col)
        if (neighbour != null && travelCondition.invoke(node.position, neighbour)) {
            node.adjacentNodes[graph.nodes[row to col-1]!!] = stepSizeCalculator.invoke(node.position, neighbour)
        }
    }

    return graph
}

fun <T> Graph2D<T>.calculateShortestPathFromSource(source: Node2D<T>): Graph2D<T> {
    source.distance = 0

    val settledNodes = mutableSetOf<Node2D<T>>()
    val unsettledNodes = mutableSetOf<Node2D<T>>()
    var currentNode: Node2D<T>?
    var adjacentNode: Node2D<T>
    var edgeWeight: Int

    unsettledNodes.add(source)
    do {
        currentNode = getLowestDistanceNode(unsettledNodes)
        unsettledNodes.remove(currentNode)
        if (currentNode != null) {
            currentNode.adjacentNodes.entries.forEach {
                adjacentNode = it.key
                edgeWeight = it.value
                if (!settledNodes.contains(adjacentNode)) {
                    calculateMinimumDistance(adjacentNode, edgeWeight, currentNode)
                    unsettledNodes.add(adjacentNode)
                }
            }
            settledNodes.add(currentNode)
        }
    } while (unsettledNodes.size != 0)

    return this
}

private fun <T> getLowestDistanceNode(unsettledNodes: Set<Node2D<T>> ): Node2D<T>? {
    var lowestDistanceNode: Node2D<T>? = null
    var lowestDistance: Int = Integer.MAX_VALUE
    for (node in unsettledNodes) {
        if (node.distance < lowestDistance) {
            lowestDistance = node.distance;
            lowestDistanceNode = node;
        }
    }
    return lowestDistanceNode
}

private fun <T>  calculateMinimumDistance(evaluationNode: Node2D<T>, edgeWeight: Int, sourceNode: Node2D<T>) {
    val sourceDistance = sourceNode.distance
    if (sourceDistance + edgeWeight < evaluationNode.distance) {
        evaluationNode.distance = sourceDistance + edgeWeight
//        val shortestPath = LinkedHashSet(sourceNode.shortestPath);
//        shortestPath.add(sourceNode);
//        evaluationNode.shortestPath = shortestPath;
    }
}
