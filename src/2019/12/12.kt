package `2019`.`12`

import util.codeGeneratorSingularUsage
import util.leastCommonMultiplication
import util.readFileLineByLineToText
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs
import kotlin.streams.toList


fun main() {

    val moons = readFileLineByLineToText("12.txt").map {
        val params = it
            .replace("<", "")
            .replace(">", "")
            .replace(" ", "")
            .replace("=", "")
            .replace("x", "")
            .replace("y", "")
            .replace("z", "")
            .split(",")
        Moon(Point3D(params[0].toInt(), params[1].toInt(), params[2].toInt()))
    }.toList()

    val startTime = System.currentTimeMillis()

    /** A **/
    val moonsA: MutableList<Moon> = mutableListOf()
    moonsA.addAll(moons)
    for (i in 0..999) {
        for (moonsComparison in codeGeneratorSingularUsage("0123", 2).sorted()) {
            moonsA[moonsComparison[0].toString().toInt()].applyGravity(moonsA[moonsComparison[1].toString().toInt()])
        }
        for (moon in moonsA) {
            moon.move()
        }
    }
    println("A: " + moonsA.sumBy { it.getEnergy() })

    val intermediateTime = System.currentTimeMillis()

    /** B **/
    val moonsB: MutableList<Moon> = mutableListOf()
    moonsB.addAll(moons)
    val baseSnapshot = Snapshot(moonsB[0], moonsB[1], moonsB[2], moonsB[3])
    lateinit var snapshot: Snapshot
    var lcmFoundx = false
    var lcmx: Int = 0
    var lcmFoundy = false
    var lcmy: Int = 0
    var lcmFoundz = false
    var lcmz: Int = 0


    while (!lcmFoundx || !lcmFoundy || !lcmFoundz) {
        for (moonsComparison in codeGeneratorSingularUsage("0123", 2).sorted()) {
            moonsB[moonsComparison[0].toString().toInt()].applyGravity(moonsB[moonsComparison[1].toString().toInt()])
        }

        for (moon in moonsB) {
            moon.move()
        }
        snapshot = Snapshot(moonsB[0], moonsB[1], moonsB[2], moonsB[3])

        if (!lcmFoundx) {
            lcmx ++
            lcmFoundx = baseSnapshot.xMatches(snapshot)
        }
        if (!lcmFoundy) {
            lcmy ++
            lcmFoundy = baseSnapshot.yMatches(snapshot)
        }
        if (!lcmFoundz) {
            lcmz ++
            lcmFoundz = baseSnapshot.zMatches(snapshot)
        }
    }
    val lcm: Long = leastCommonMultiplication(listOf(lcmx, lcmy, lcmz))
    println("B: $lcm")

    val endTime = System.currentTimeMillis()

    val formatter: DateFormat = SimpleDateFormat("ss.SSS")
    formatter.timeZone = TimeZone.getTimeZone("UTC")
    println("A took " + formatter.format(intermediateTime - startTime) + " seconds")
    println("B took " + formatter.format(endTime - intermediateTime) + " seconds")
    println("TOTAL: " + formatter.format(endTime - startTime) + " seconds")

}

private class Snapshot {
    var moon1: Moon
    var moon2: Moon
    var moon3: Moon
    var moon4: Moon

    constructor(moon1: Moon, moon2: Moon, moon3: Moon, moon4: Moon) {
        this.moon1 = Moon(moon1.position.copy(), moon1.velocity.copy())
        this.moon2 = Moon(moon2.position.copy(), moon2.velocity.copy())
        this.moon3 = Moon(moon3.position.copy(), moon3.velocity.copy())
        this.moon4 = Moon(moon4.position.copy(), moon4.velocity.copy())
    }
    
    fun xMatches(other: Snapshot) : Boolean = 
        this.moon1.position.x == other.moon1.position.x 
                && this.moon2.position.x == other.moon2.position.x
                && this.moon3.position.x == other.moon3.position.x
                && this.moon4.position.x == other.moon4.position.x
                && this.moon1.velocity.x == other.moon1.velocity.x
                && this.moon2.velocity.x == other.moon2.velocity.x
                && this.moon3.velocity.x == other.moon3.velocity.x
                && this.moon4.velocity.x == other.moon4.velocity.x

    fun yMatches(other: Snapshot) : Boolean =
        this.moon1.position.y == other.moon1.position.y
                && this.moon2.position.y == other.moon2.position.y
                && this.moon3.position.y == other.moon3.position.y
                && this.moon4.position.y == other.moon4.position.y
                && this.moon1.velocity.y == other.moon1.velocity.y
                && this.moon2.velocity.y == other.moon2.velocity.y
                && this.moon3.velocity.y == other.moon3.velocity.y
                && this.moon4.velocity.y == other.moon4.velocity.y

    fun zMatches(other: Snapshot) : Boolean =
        this.moon1.position.z == other.moon1.position.z
                && this.moon2.position.z == other.moon2.position.z
                && this.moon3.position.z == other.moon3.position.z
                && this.moon4.position.z == other.moon4.position.z
                && this.moon1.velocity.z == other.moon1.velocity.z
                && this.moon2.velocity.z == other.moon2.velocity.z
                && this.moon3.velocity.z == other.moon3.velocity.z
                && this.moon4.velocity.z == other.moon4.velocity.z
    
}

private data class Moon(val position: Point3D, val velocity: Velocity = Velocity()) {
    fun applyGravity(other: Moon) {
        velocity.applyGravity(this.position, other.position)
    }
    fun move() {
        position.applyVelocity(velocity)
    }

    override fun toString(): String {
        return "<pos=$position, vel=$velocity>"
    }

    fun getEnergy() : Int = position.getPotentialEnergy() * velocity.getKineticEnergy()
}
private data class Velocity(var x: Int = 0, var y: Int = 0, var z: Int = 0) {
    fun applyGravity(position: Point3D, other: Point3D) {
        x += getVelocityChange(position.x, other.x)
        y += getVelocityChange(position.y, other.y)
        z += getVelocityChange(position.z, other.z)
    }

    private fun getVelocityChange(a: Int, b: Int): Int {
        if (a < b) return 1
        if (a > b) return -1
        return 0
    }

    override fun toString(): String {
        return "<x=$x, y=$y, z=$z>"
    }

    fun getKineticEnergy() : Int = abs(x) + abs(y) + abs(z)
}
private data class Point3D(var x: Int, var y: Int, var z: Int) {
    fun applyVelocity(velocity: Velocity) {
        x += velocity.x
        y += velocity.y
        z += velocity.z
    }

    override fun toString(): String {
        return "<x=$x, y=$y, z=$z>"
    }

    fun getPotentialEnergy() : Int = abs(x) + abs(y) + abs(z)

}