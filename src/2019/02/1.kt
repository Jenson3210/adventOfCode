package `2019`.`02`

import util.readFileLineCsvToInt
import java.util.stream.IntStream
import kotlin.streams.toList


fun main() {
    print(processIntcode(readFileLineCsvToInt("02.txt"))[0])
}

private fun processIntcode(input: IntStream): List<Int> {
    val memory = input.toList().toMutableList()
    memory[1] = 12
    memory[2] = 2
    var i = 0
    while (memory[i] != 99) {
        //println("[$i: ${memory[i]}, ${memory[i + 1]}, ${memory[i + 2]}, ${memory[i + 3]}]")
        memory[memory[i + 3]] = when (memory[i]) {
            1 -> sumNumbers(memory[memory[i + 1]], memory[memory[i + 2]])
            2 -> multiplyNumbers(memory[memory[i + 1]], memory[memory[i + 2]])
            else -> throw IllegalArgumentException()
        }
        i += 4
    }
    return memory
}

private fun sumNumbers(n1: Int, n2: Int) : Int {
    return n1 + n2
}

private fun multiplyNumbers(n1: Int, n2: Int) : Int {
    return n1 * n2
}