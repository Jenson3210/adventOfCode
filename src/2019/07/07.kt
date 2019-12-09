package `2019`.`07`

import util.codeGenerator
import util.codeGeneratorSingularUsage
import util.readFileLineCsvToInt
import kotlin.streams.toList

private class Parameters(var input: MutableList<Int> = mutableListOf(), var output: MutableList<Int> = mutableListOf(0), var haltOccured: Boolean = false) {

    fun getNextValue(): Int {
        val toReturn = input[0]
        input.removeAt(0)
        return toReturn
    }

    fun nextAmplifier(): Parameters {
        input.clear()
        input.addAll(output)
        output.clear()
        return this
    }
}

fun main() {
    val memory: MutableList<Int> = readFileLineCsvToInt("07.txt").toList().toMutableList()
    println("A: " + solve(memory, false, false, "01234"))
    println("B: " + solve(memory, false, true, "56789"))
}

private fun solve(memory : MutableList<Int>, debug: Boolean, feedbackLoop: Boolean, options: String) : Int {
    var params: Parameters
    var programs: MutableList<OptCodeProgram>
    var highestValue = 0
    for (code in codeGeneratorSingularUsage(options, options.length)) {
        programs = mutableListOf()
        params = Parameters(haltOccured = !feedbackLoop)
        if (debug) println("code: $code")
        for (amplifier in 0..4) {
            val memoryClone: MutableList<Int> = mutableListOf()
            memoryClone.addAll(memory)
            programs.add(OptCodeProgram(memoryClone, Character.getNumericValue(code[amplifier])))
        }
        do {
            for (program in programs) {
                params.nextAmplifier()
                if (debug) {
                    program.debug(params)
                } else {
                    program.run(params)
                }
            }
        } while (!params.haltOccured)
        if (params.output.last() > highestValue) {
            highestValue = params.output.last()
        }
    }
    return highestValue
}

private class OptCodeProgram(val memory: MutableList<Int>, val phase: Int? = null) {
    private var currentIndex: Int = 0
    private var debug = false
    private var running = false

    fun run(params: Parameters) {
        debug = false
        execute(params)
    }
    fun debug(params: Parameters) {
        debug = true
        execute(params)
    }

    private fun execute(params: Parameters) {
        if (debug) println("Starting phase $phase")
        if (!running && phase != null) {
            params.input.add(0, phase)
            running = true
        } else {
            if (debug) println("Starting phase $phase")
        }
        var operation: Operation? = null
        var verb: String
        var parameterModes : String
        var stillHasInput = true
        while ((operation == null || operation !is HaltOperation) && stillHasInput) {
            verb = String.format("%05d", memory[currentIndex])
            parameterModes = verb.substring(0, 3)
            if (debug) print("\t${verb.substring(3, 5)}: ")
            operation = when (verb.substring(3, 5)) {
                "01" -> SumOperation(currentIndex, memory, parameterModes, debug)
                "02" -> MultiplyOperation(currentIndex, memory, parameterModes, debug)
                "03" -> InputOperation(currentIndex, memory, params, parameterModes, debug)
                "04" -> OutputOperation(currentIndex, memory, params, parameterModes, debug)
                "05" -> JumpIfTrueOperation(currentIndex, memory, parameterModes, debug)
                "06" -> JumpIfFalseOperation(currentIndex, memory, parameterModes, debug)
                "07" -> LessThanOperation(currentIndex, memory, parameterModes, debug)
                "08" -> EqualsOperation(currentIndex, memory, parameterModes, debug)
                "99" -> HaltOperation(debug)
                else -> {
                    if (debug) println("Illegal argument: instruction = ${verb.substring(3, 5)}")
                    throw IllegalArgumentException()
                }
            }

            try {
                operation.perform()
                currentIndex = (operation.nextIndex)
                if (currentIndex == 0) {
                    params.haltOccured = true
                }
            } catch (indexOutOfBounds: IndexOutOfBoundsException) {
                //ignore this one to go to next amp
                if (debug) println()
                stillHasInput = false
            }
        }
    }
}

private abstract class Operation(var nextIndex: Int, val debug: Boolean) {
    abstract fun perform()
}
private class HaltOperation(debug: Boolean) : Operation(0, debug) {
    override fun perform() = run { if (debug) println("THE END") }
}

//Memory
private abstract class MemoryOperation(val index: Int, parameterAmount: Int, val memory: MutableList<Int>, val parameterModes: String, debug: Boolean) : Operation(index + parameterAmount + 1, debug) {
    fun getParameter(num: Int): Int {
        if (parameterModes.reversed().substring(num - 1, num ) == "0") {
            return memory[memory[index + num]]
        } else {
            return memory[index + num]
        }
    }
    fun getParameterForOutput(num: Int) : Int {
        if (parameterModes.reversed().substring(num - 1, num ) == "0") {
            return memory[index + num]
        } else {
            return index + num
        }
    }
}

//SHOW-OPERATIONS
private class OutputOperation(index: Int, memory: MutableList<Int>, val params: Parameters, parameterModes: String, debug: Boolean) : MemoryOperation(index, 1, memory, parameterModes, debug) {

    override fun perform() {
        params.output.add(getParameter(1))
        if (debug) println("Output ${getParameter(1)}")
        if (debug) println(getParameter(1))
    }
}

//WRITE-OPERATIONS
private abstract class WriteOperation(index: Int, memory: MutableList<Int>, parameterModes: String, parameterAmount: Int, val destinationParameter: Int, debug: Boolean) : MemoryOperation(index, parameterAmount, memory, parameterModes, debug) {

    override fun perform() {
        val result = getResult()
        if (debug) println("${getDescription()} $result and store to ${getParameterForOutput(destinationParameter)}")
        memory[getParameterForOutput(destinationParameter)] = result
    }
    abstract fun getResult() : Int
    abstract fun getDescription() : String
}
private class InputOperation(index: Int, memory: MutableList<Int>, val params: Parameters, parameterModes: String, debug: Boolean) : WriteOperation(index, memory, parameterModes, 1, 1, debug){
    override fun getResult(): Int = params.getNextValue()
    override fun getDescription(): String = "Input "
}
private class SumOperation(index: Int, memory: MutableList<Int>, parameterModes: String, debug: Boolean) : WriteOperation(index, memory, parameterModes, 3, 3, debug){
    override fun getResult() : Int = (getParameter(1) + getParameter(2))
    override fun getDescription(): String = "summed ${getParameterForOutput(1)}(${getParameter(1)}) + ${getParameterForOutput(2)}(${getParameter(2)}) = "

}

private class MultiplyOperation(index: Int, memory: MutableList<Int>, parameterModes: String, debug: Boolean) : WriteOperation(index, memory, parameterModes, 3, 3, debug){
    override fun getResult() : Int = getParameter(1) * getParameter(2)
    override fun getDescription(): String = "multiplied ${getParameter(1)} * ${getParameter(2)} = "
}
private class LessThanOperation(index: Int, memory: MutableList<Int>, parameterModes: String, debug: Boolean) : WriteOperation(index, memory, parameterModes, 3, 3, debug){
    override fun getResult(): Int = if (getParameter(1) < getParameter(2)) 1 else 0
    override fun getDescription(): String = "if ${getParameter(1)} < ${getParameter(2)} then we write "
}
private class EqualsOperation(index: Int, memory: MutableList<Int>, parameterModes: String, debug: Boolean) : WriteOperation(index, memory, parameterModes, 3, 3, debug){
    override fun getResult(): Int = if (getParameter(1) == getParameter(2)) 1 else 0
    override fun getDescription(): String = "if ${getParameter(1)} == ${getParameter(2)} then we write "
}
//
// JUMP-OPERATIONS
private abstract class JumpOperation(index: Int, memory: MutableList<Int>, parameterModes: String, debug: Boolean) : MemoryOperation(index, 2, memory, parameterModes, debug) {
    override fun perform() {
        if (shouldJump()) {
            nextIndex = getParameter(2)
            if (debug) println("jumping to $nextIndex(${memory[nextIndex]})")
        } else {
            if (debug) println("not jumping")
        }
    }
    abstract fun shouldJump() : Boolean
}
private class JumpIfTrueOperation(index: Int, memory: MutableList<Int>, parameterModes: String, debug: Boolean) : JumpOperation(index, memory, parameterModes, debug) {
    override fun shouldJump() = getParameter(1) != 0
}
private class JumpIfFalseOperation(index: Int, memory: MutableList<Int>, parameterModes: String, debug: Boolean) : JumpOperation(index, memory, parameterModes, debug) {
    override fun shouldJump() = getParameter(1) == 0
}