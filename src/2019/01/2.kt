package `2019`.`01`

import util.readFileLineByLineToInt
import kotlin.math.floor

fun main() {
    print(readFileLineByLineToInt("01.txt").map {
        calculateFuelRequirement(
            it
        )
    }.sum())
}

private fun calculateFuelRequirement(mass: Int): Int {
    var m = mass
    var fuel = 0
    while ((floor(m / 3.0) - 2).toInt() > 0) {
        fuel += (floor(m / 3.0) - 2).toInt()
        m = (floor(m / 3.0) - 2).toInt()
    }
    return fuel
}