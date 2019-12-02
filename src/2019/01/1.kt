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
    return (floor(mass / 3.0) - 2).toInt()
}