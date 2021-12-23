package utils

// For optimizations The shortestPath is not stored per node.

class Graph {
    val nodes: MutableMap<Pair<Int, Int>, Node> = mutableMapOf()
}

class Node(val name: String){
//    var shortestPath: LinkedHashSet<Node> = LinkedHashSet()
    var distance: Int = Int.MAX_VALUE
    val adjacentNodes: MutableMap<Node, Int> = mutableMapOf()

    override fun toString(): String {
        return "$name:$distance)"
    }
}

fun NavigateableMap<Int>.toDijkstraGraph(): Graph {
    val graph = Graph()
    val allCoordinates = cartesianProduct(this.rowIndices().toList(), this.colIndices().toList()).map { it[0] as Int to it[1] as Int }

    graph.nodes.putAll(allCoordinates.map { Pair(it.first to it.second,  Node("${it.first}-${it.second}")) })
    graph.nodes.forEach { (coordinate, node) ->
        val row = coordinate.first
        val col = coordinate.second

        var neighbour: Int? = this.getNorthCell(row, col)
        if (neighbour != null) {
            node.adjacentNodes[graph.nodes[row-1 to col]!!] = neighbour
        }
        neighbour = this.getEastCell(row, col)
        if (neighbour != null) {
            node.adjacentNodes[graph.nodes[row to col+1]!!] = neighbour
        }
        neighbour = this.getSouthCell(row, col)
        if (neighbour != null) {
            node.adjacentNodes[graph.nodes[row+1 to col]!!] = neighbour
        }
        neighbour = this.getWestCell(row, col)
        if (neighbour != null) {
            node.adjacentNodes[graph.nodes[row to col-1]!!] = neighbour
        }
    }

    return graph
}

fun Graph.calculateShortestPathFromSource(source: Node): Graph {
    source.distance = 0

    val settledNodes = mutableSetOf<Node>()
    val unsettledNodes = mutableSetOf<Node>()
    var currentNode: Node?
    var adjacentNode: Node
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

private fun getLowestDistanceNode(unsettledNodes: Set<Node> ): Node? {
    var lowestDistanceNode: Node? = null
    var lowestDistance: Int = Integer.MAX_VALUE
    for (node in unsettledNodes) {
        if (node.distance < lowestDistance) {
            lowestDistance = node.distance;
            lowestDistanceNode = node;
        }
    }
    return lowestDistanceNode
}

private fun calculateMinimumDistance(evaluationNode: Node, edgeWeigh: Int, sourceNode: Node) {
    val sourceDistance = sourceNode.distance
    if (sourceDistance + edgeWeigh < evaluationNode.distance) {
        evaluationNode.distance = sourceDistance + edgeWeigh
//        val shortestPath = LinkedHashSet(sourceNode.shortestPath);
//        shortestPath.add(sourceNode);
//        evaluationNode.shortestPath = shortestPath;
    }
}