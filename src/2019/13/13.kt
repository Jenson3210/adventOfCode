package `2019`.`13`

import util.readFileLineCsvToLong
import kotlin.streams.toList

fun main() {
    val memory: List<Long> = readFileLineCsvToLong("13.txt").toList()
    val paramsA = Parameters(false)
    //A
    val memoryA: MutableList<Long> = memory.toMutableList()
    println("A: " + solve(paramsA, memoryA, false))
    //B
    val paramsB = Parameters(false)
    val memoryB: MutableList<Long> = memory.toMutableList()
    memoryB[0] = 2L
    println("B: " + solve(paramsB, memoryB, false))
}

private fun solve(params: Parameters, memory : MutableList<Long>, debug: Boolean) : String {
    val solutionB = memory[0] == 2L
    if (debug) {
        OptCodeProgram(memory).debug(params)
    } else {
        OptCodeProgram(memory).run(params)
    }
    if (solutionB) return params.highScore().toString()
    return params.countBlockTiles().toString()
}

private class Parameters(val debug: Boolean, var output: MutableList<Long> = mutableListOf(), var haltOccured: Boolean = false) {

    var highScore = 0L
    val screen: MutableMap<Pair<Long, Long>, Long> = mutableMapOf()

    fun getNextValue(): Long {
        val willReturn = handleOutput()
        return willReturn
    }

    fun highScore() : Long {
        handleOutput()
        return highScore
    }

    fun handleOutput() : Long {
        var paddleX = 0L
        var ballX = 0L


        while (output.isNotEmpty()) {
            val x = output.removeAt(0)
            val y = output.removeAt(0)
            val type = output.removeAt(0)
            if (x == -1L) {
                highScore = type
            } else {
                screen[Pair(x, y)] = type
                if (type == 3L) {
                    paddleX = x
                } else if (type == 4L) {
                    ballX = x
                }
            }

        }
        if (debug) {
            printGrid()
        }
        if (ballX == paddleX) return 0L
        if (ballX > paddleX) return 1L
        if (ballX < paddleX) return -1L
        return 0L
    }

    fun printGrid() {

        var blocks = screen.map { Pair(Pair(it.key.first, it.key.second), it.value) }.toList()
        for (row in blocks.map { it.first.second }.distinct()) {
            for (column in blocks.map { it.first.first }.filter { it != -1L }.distinct().sorted()) {
                if (blocks.filter { it.first == Pair(column, row) }.map { it.second }.firstOrNull() != null) {
                        print(when (blocks.filter { it.first == Pair(column, row) }.map { it.second }.first()) {
                            1L -> "X"
                            2L -> "#"
                            3L -> "_"
                            4L -> "o"
                            else -> " "
                        }
                        )
                    }
            }
            println()
        }
    }

    fun countBlockTiles() : Int{
        handleOutput()
        return screen.filter { it.value == 2L }.size
    }

}

/**
The game didn't run because you didn't put in any quarters. Unfortunately, you did not bring any quarters. Memory address 0 represents the number of quarters that have been inserted; set it to 2 to play for free.

The arcade cabinet has a joystick that can move left and right. The software reads the position of the joystick with input instructions:

If the joystick is in the neutral position, provide 0.
If the joystick is tilted to the left, provide -1.
If the joystick is tilted to the right, provide 1.
The arcade cabinet also has a segment display capable of showing a single number that represents the player's current score. When three output instructions specify X=-1, Y=0, the third output instruction is not a tile; the value instead specifies the new score to show in the segment display. For example, a sequence of output values like -1,0,12345 would show 12345 as the player's current score.

Beat the game by breaking all the blocks. What is your score after the last block is broken?

 **/

private class OptCodeProgram(val memory: MutableList<Long>, val phase: Long? = null) {
    private var currentIndex: Int = 0
    private var currentBase: Int = 0
    private var debug = false

    fun run(params: Parameters) {
        debug = false
        execute(params)
    }
    fun debug(params: Parameters) {
        debug = true
        execute(params)
    }

    private fun execute(params: Parameters) {
        if (debug && phase != null) println("Starting phase $phase")
        var operation: Operation? = null
        var verb: String
        var parameterModes : String
        var stillHasInput = true
        while ((operation == null || operation !is HaltOperation) && stillHasInput) {
            verb = String.format("%05d", memory[currentIndex])
            parameterModes = verb.substring(0, 3)
            if (debug) print("\t${verb.substring(3, 5)}: ")
            operation = when (verb.substring(3, 5)) {
                "01" -> SumOperation(currentBase, currentIndex, memory, parameterModes, debug)
                "02" -> MultiplyOperation(currentBase, currentIndex, memory, parameterModes, debug)
                "03" -> InputOperation(currentBase, currentIndex, memory, params, parameterModes, debug)
                "04" -> OutputOperation(currentBase, currentIndex, memory, params, parameterModes, debug)
                "05" -> JumpIfTrueOperation(currentBase, currentIndex, memory, parameterModes, debug)
                "06" -> JumpIfFalseOperation(currentBase, currentIndex, memory, parameterModes, debug)
                "07" -> LessThanOperation(currentBase, currentIndex, memory, parameterModes, debug)
                "08" -> EqualsOperation(currentBase, currentIndex, memory, parameterModes, debug)
                "09" -> ShiftRelativeBaseOperation(currentBase, currentIndex, memory, parameterModes, debug)
                "99" -> HaltOperation(debug)
                else -> {
                    if (debug) println("Illegal argument: instruction = ${verb.substring(3, 5)}")
                    throw IllegalArgumentException()
                }
            }

            try {
                operation.perform()
                currentIndex = operation.nextIndex
                currentBase = operation.relativeBase
                if (operation is HaltOperation) {
                    params.haltOccured = true
                }
            } catch (noMoreInput: NoMoreInputException) {
                //ignore this one to go to next amp
                if (debug) println()
                stillHasInput = false
            }
        }
    }
}

private abstract class Operation(var nextIndex: Int, var relativeBase: Int, val debug: Boolean) {
    abstract fun perform()
}
private class HaltOperation(debug: Boolean) : Operation(0, 0, debug) {
    override fun perform() = run { if (debug) println("THE END") }
}

//Memory
private abstract class MemoryOperation(relativeBase: Int, val index: Int, parameterAmount: Int, val memory: MutableList<Long>, val parameterModes: String, debug: Boolean) : Operation(index + parameterAmount + 1, relativeBase, debug) {
    fun getParameter(num: Int): Long = memory[indexOutOfBoundsSafeReturnIndex(getIndexForParameter(num))]

    private fun indexOutOfBoundsSafeReturnIndex(index: Int): Int {
        if (index < 0) {
            throw UnsupportedOperationException()
        }
        if (index >= memory.size) {
            for (number in memory.size..index) {
                memory.add(0)
            }
        }
        return index
    }

    fun getIndexForParameter(num: Int) : Int {
        return when (parameterModes.reversed().substring(num - 1, num )) {
            "0" -> indexOutOfBoundsSafeReturnIndex(memory[index + num].toInt())
            "1" -> indexOutOfBoundsSafeReturnIndex(index + num)
            "2" -> indexOutOfBoundsSafeReturnIndex(relativeBase + memory[index + num].toInt())
            else -> throw UnsupportedOperationException()
        }
    }
}

//SPECIAL-OPERATIONS
private class ShiftRelativeBaseOperation(relativeBase: Int, index: Int, memory: MutableList<Long>, parameterModes: String, debug: Boolean) : MemoryOperation(relativeBase, index, 1, memory, parameterModes, debug) {

    override fun perform() {
        relativeBase += getParameter(1).toInt()
        if (debug) println("Shifting relative base to $relativeBase")
    }
}
//SHOW-OPERATIONS
private class OutputOperation(relativeBase: Int, index: Int, memory: MutableList<Long>, val params: Parameters, parameterModes: String, debug: Boolean) : MemoryOperation(relativeBase, index, 1, memory, parameterModes, debug) {

    override fun perform() {
        params.output.add(getParameter(1))
        if (debug) println("Output ${getParameter(1)}")
        if (debug) println(getParameter(1))
    }
}

//WRITE-OPERATIONS
private abstract class WriteOperation(relativeBase: Int, index: Int, memory: MutableList<Long>, parameterModes: String, parameterAmount: Int, val destinationParameter: Int, debug: Boolean) : MemoryOperation(relativeBase, index, parameterAmount, memory, parameterModes, debug) {

    override fun perform() {
        val result = getResult()
        if (debug) println("${getDescription()} $result and store to ${getIndexForParameter(destinationParameter)}")
        memory[getIndexForParameter(destinationParameter)] = result
    }
    abstract fun getResult() : Long
    abstract fun getDescription() : String
}
private class InputOperation(relativeBase: Int, index: Int, memory: MutableList<Long>, val params: Parameters, parameterModes: String, debug: Boolean) : WriteOperation(relativeBase, index, memory, parameterModes, 1, 1, debug){
    override fun getResult(): Long = params.getNextValue()
    override fun getDescription(): String = "Input "
}
private class SumOperation(relativeBase: Int, index: Int, memory: MutableList<Long>, parameterModes: String, debug: Boolean) : WriteOperation(relativeBase, index, memory, parameterModes, 3, 3, debug){
    override fun getResult() : Long = (getParameter(1) + getParameter(2))
    override fun getDescription(): String = "summed ${getIndexForParameter(1)}(${getParameter(1)}) + ${getIndexForParameter(2)}(${getParameter(2)}) = "

}

private class MultiplyOperation(relativeBase: Int, index: Int, memory: MutableList<Long>, parameterModes: String, debug: Boolean) : WriteOperation(relativeBase, index, memory, parameterModes, 3, 3, debug){
    override fun getResult() : Long = getParameter(1) * getParameter(2)
    override fun getDescription(): String = "multiplied ${getParameter(1)} * ${getParameter(2)} = "
}
private class LessThanOperation(relativeBase: Int, index: Int, memory: MutableList<Long>, parameterModes: String, debug: Boolean) : WriteOperation(relativeBase, index, memory, parameterModes, 3, 3, debug){
    override fun getResult(): Long = if (getParameter(1) < getParameter(2)) 1 else 0
    override fun getDescription(): String = "if ${getParameter(1)} < ${getParameter(2)} then we write "
}
private class EqualsOperation(relativeBase: Int, index: Int, memory: MutableList<Long>, parameterModes: String, debug: Boolean) : WriteOperation(relativeBase, index, memory, parameterModes, 3, 3, debug){
    override fun getResult(): Long = if (getParameter(1) == getParameter(2)) 1 else 0
    override fun getDescription(): String = "if ${getParameter(1)} == ${getParameter(2)} then we write "
}
//
// JUMP-OPERATIONS
private abstract class JumpOperation(relativeBase: Int, index: Int, memory: MutableList<Long>, parameterModes: String, debug: Boolean) : MemoryOperation(relativeBase, index, 2, memory, parameterModes, debug) {
    override fun perform() {
        if (shouldJump()) {
            nextIndex = getParameter(2).toInt()
            if (debug) println("jumping to $nextIndex(${memory[nextIndex]})")
        } else {
            if (debug) println("not jumping")
        }
    }
    abstract fun shouldJump() : Boolean
}
private class JumpIfTrueOperation(relativeBase: Int, index: Int, memory: MutableList<Long>, parameterModes: String, debug: Boolean) : JumpOperation(relativeBase, index, memory, parameterModes, debug) {
    override fun shouldJump() = getParameter(1).toInt() != 0
}
private class JumpIfFalseOperation(relativeBase: Int, index: Int, memory: MutableList<Long>, parameterModes: String, debug: Boolean) : JumpOperation(relativeBase, index, memory, parameterModes, debug) {
    override fun shouldJump() = getParameter(1).toInt() == 0
}

private class NoMoreInputException() : Exception()
