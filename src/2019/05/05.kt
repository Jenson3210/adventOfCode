package `2019`.`05`

import util.readFileLineCsvToInt
import kotlin.streams.toList

private class InputB(val input: MutableList<Int> = mutableListOf()) {
    fun getNextValue(): Int {
        val toReturn = input[0]
        input.removeAt(0)
        return toReturn
    }
}

fun main() {
    OptCodeProgram(readFileLineCsvToInt("05.txt").toList().toMutableList(), InputB(mutableListOf(1))).run()
//    OptCodeProgramB(readFileLineCsvToInt("05.txt").toList().toMutableList(), InputB(mutableListOf(1))).debug()
    OptCodeProgram(readFileLineCsvToInt("05.txt").toList().toMutableList(), InputB(mutableListOf(5))).run()
//    OptCodeProgramB(readFileLineCsvToInt("05.txt").toList().toMutableList(), InputB(mutableListOf(5))).debug()
}

private class OptCodeProgram(val code: MutableList<Int>, val input: InputB) {
    fun run() = execute(false)
    fun debug() = execute(true)

    private fun execute(debug: Boolean) {
        var i = 0
        var operation: Operation? = null
        var verb: String
        var parameterModes : String
        while (operation == null || operation !is HaltOperation) {
            verb = String.format("%05d", code[i])
            parameterModes = verb.substring(0, 3)
            if (debug) print("\t${verb.substring(3, 5)}: ")
            operation = when (verb.substring(3, 5)) {
                "01" -> SumOperation(i, code, parameterModes, debug)
                "02" -> MultiplyOperation(i, code, parameterModes, debug)
                "03" -> InputOperation(i, code, input, parameterModes, debug)
                "04" -> DisplayOperation(i, code, parameterModes, debug)
                "05" -> JumpIfTrueOperation(i, code, parameterModes, debug)
                "06" -> JumpIfFalseOperation(i, code, parameterModes, debug)
                "07" -> LessThanOperation(i, code, parameterModes, debug)
                "08" -> EqualsOperation(i, code, parameterModes, debug)
                "99" -> HaltOperation(debug)
                else -> {
                    if (debug) println("Illegal argument: instruction = ${verb.substring(3, 5)}")
                    throw IllegalArgumentException()
                }
            }
            operation.perform()
            i = (operation.nextIndex)
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
private class DisplayOperation(index: Int, memory: MutableList<Int>, parameterModes: String, debug: Boolean) : MemoryOperation(index, 1, memory, parameterModes, debug) {

    override fun perform() {
        if (debug)println(getParameter(1))
        println("${getParameter(1)}")
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
private class InputOperation(index: Int, memory: MutableList<Int>, val input: InputB, parameterModes: String, debug: Boolean) : WriteOperation(index, memory, parameterModes, 1, 1, debug){
    override fun getResult(): Int = input.getNextValue()
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
