package `2019`.`02`

import util.readFileLineCsvToInt
import kotlin.streams.toList


fun main() {
    print(searchResult("02.txt", 19690720))
}

private fun searchResult(fileName: String, wantedSum: Int): Int {
    var noun = 1
    var verb = 1
    while(processIntcode(
            readFileLineCsvToInt(fileName).toList().toMutableList(),
            noun,
            verb
        )[0] != wantedSum) {
        //println("[$noun, $verb]")
        verb ++
        if (verb > 99) {
            verb = 1
            noun ++
        }
    }
    return 100 * noun + verb
}

private fun processIntcode(memory: MutableList<Int>, noun: Int = 12, verb: Int = 2): List<Int> {
    memory[1] = noun
    memory[2] = verb
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