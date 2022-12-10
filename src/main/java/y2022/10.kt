package y2022

import utils.printDay
import utils.readFileLineByLineToText
import java.util.stream.Stream

fun main() {
    printDay(1)
    println(run(readFileLineByLineToText(2022, 10), ))

    printDay(2)
    draw(readFileLineByLineToText(2022, 10))
}

private fun run(instructions: Stream<String>): Long {
    val listeningToClockNumber = listOf(20, 60, 100, 140, 180, 220)
    var totalSignalStrength = 0L
    var amount: Int
    var registerX = 1
    var clockCycle = 0
    var cycleDuration: Int
    instructions.forEach {
        cycleDuration = if (it.startsWith("addx")) 2 else 1
        clockCycle += if (it.startsWith("addx")) 2 else 1
        amount = (if (it.startsWith("addx")) it.substringAfter(" ") else "0").toInt()

        (0 until cycleDuration).forEach {
            if (listeningToClockNumber.contains(clockCycle - it)) {
//                println("At cycle ${clockCycle - it} the register is $registerX")
                totalSignalStrength += ((clockCycle - it) * registerX)
            }
        }

        registerX += amount
    }
    return totalSignalStrength
}

private fun draw(instructions: Stream<String>) {
    var amount: Int
    var registerX = 1
    var clockCycle = 0
    var cycleDuration: Int
    instructions.forEach {
        cycleDuration = if (it.startsWith("addx")) 2 else 1
        clockCycle += if (it.startsWith("addx")) 2 else 1
        amount = (if (it.startsWith("addx")) it.substringAfter(" ") else "0").toInt()

        (cycleDuration - 1 downTo  0).forEach {
            val currentlyDrawing = ((clockCycle - it) % 40)
            if (((registerX % 40) - 1 .. (registerX % 40) + 1).contains(currentlyDrawing - 1)) {
//                println("During cycle ${clockCycle - it}: CRT draws pixel # (register = $registerX, sprite(${(registerX % 40) - 1 .. (registerX % 40) + 1}))")
                print("#")
            } else {
//                println("During cycle ${clockCycle - it}: CRT draws pixel . (register = $registerX, sprite(${(registerX % 40) - 1 .. (registerX % 40) + 1})")
                print(" ")
            }
            if ((clockCycle - it) % 40 == 0 ) {
//                println("EOL ($clockCycle - $it)")
                println()
            }
        }

        registerX += amount
    }
}
