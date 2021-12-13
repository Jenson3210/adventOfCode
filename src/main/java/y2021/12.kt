package y2021

import utils.getElementsWithTheirCount
import utils.printDay
import utils.readFileLineByLineToText

fun main() {
    printDay(1)
    println(findSinglePassSmallCavesRoutes().count())

    printDay(2)
    println(findDoublePassSmallCavesRoutes().count())
}

private fun findSinglePassSmallCavesRoutes(route: List<Cave> = listOf(initMapAndGetStart())): List<List<Cave>> {
    if (route.last().name == "end") {
        return listOf(route)
    }

    return route.last().getLinkedPassableCaves(route)
        .flatMap { findSinglePassSmallCavesRoutes(route + it) }
}

private fun findDoublePassSmallCavesRoutes(route: List<Cave> = listOf(initMapAndGetStart())): List<List<Cave>> {
    if (route.last().isEndCave()) {
        return listOf(route)
    }

    val routes: MutableList<List<Cave>> = mutableListOf()
    route.last().linkedCaves
        .filterNot { it.isStartCave() }
        .forEach {
            if (it.name.isSmallCave() && route.contains(it)) {
                if (!route.containsTwoSmallCavePass()) {
                    routes.addAll(findDoublePassSmallCavesRoutes(route + it))
                }
            } else {
                routes.addAll(findDoublePassSmallCavesRoutes(route + it))
            }
        }

    return routes
}

private class Cave(val name: String) {
    val linkedCaves: MutableSet<Cave> = mutableSetOf()

    fun linkCaves(otherCave: Cave) {
        linkedCaves.add(otherCave)
    }

    fun getLinkedPassableCaves(passedCaves: List<Cave>): List<Cave> {
        return linkedCaves.filterNot { it.name.isSmallCave() && passedCaves.contains(it) }
            .filterNot { it.isStartCave() }
    }

    fun isStartCave() = "start" == this.name
    fun isEndCave() = "end" == this.name
    override fun toString() = name
}


private fun List<Cave>.containsTwoSmallCavePass() = this.filter { it.name.isSmallCave() }.getElementsWithTheirCount().any { it.value == 2 }
private fun String.isSmallCave() = "[a-z]*".toRegex().matches(this)

private fun initMapAndGetStart(): Cave {
    val caves = mutableMapOf<String, Cave>()
    readFileLineByLineToText(2021, 12).forEach {
        val fromCaveName = it.substringBefore("-")
        val tillCaveName = it.substringAfter("-")
        val fromCave = caves.getOrPut(fromCaveName) { Cave(fromCaveName) }
        val tillCave = caves.getOrPut(tillCaveName) { Cave(tillCaveName) }
        fromCave.linkCaves(tillCave)
        tillCave.linkCaves(fromCave)
    }

    return caves["start"]!!
}