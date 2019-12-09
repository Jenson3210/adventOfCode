package `2019`.`06`

import util.readFileLineByLineToText
import java.util.stream.Stream

fun main() {
    println("1: " + SolarSystem(readFileLineByLineToText("06.txt")).countOrbits())
    println("2: " + SolarSystem(readFileLineByLineToText("06.txt")).countOrbitSteps("YOU", "SAN"))
}

private class SolarSystem(val bodies: MutableMap<String, CelestialBody> = mutableMapOf()) {
    constructor(input: Stream<String>) : this() {
        input.forEach {
            getCelestialBody(it.split(")")[1]).turnsAround(getCelestialBody(it.split(")")[0]))
        }
    }

    private fun getCelestialBody(name: String): CelestialBody {
        bodies.putIfAbsent(name, CelestialBody(name, null))
        return bodies[name]!!
    }

    fun countOrbits(): Int = bodies.values.map { it.getCount() }. sum()
    fun countOrbitSteps(from: String, till: String) : Int {
        var pathFrom : MutableList<CelestialBody> = getCelestialBody(from).getPath()
        var pathTill : MutableList<CelestialBody> = getCelestialBody(till).getPath()
        var junctionPath : List<CelestialBody> = pathFrom.intersect(pathTill).toList()
        pathFrom.removeAll(junctionPath)
        pathTill.removeAll(junctionPath)
        return (pathFrom + pathTill).size
    }
}

private class CelestialBody(val name: String, var turningAround: CelestialBody?) {
    fun turnsAround(body: CelestialBody) { turningAround = body }
    fun getCount() : Int = if (turningAround != null) turningAround!!.getCount() + 1 else 0
    fun getPath() : MutableList<CelestialBody> {
        val path : MutableList<CelestialBody>
        if (turningAround == null) {
            path = mutableListOf()
        } else {
            path = turningAround!!.getPath()
            path.add(turningAround!!)
        }
        return path
    }
}