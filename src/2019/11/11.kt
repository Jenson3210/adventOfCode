package `2019`.`11`

import util.readFileLineCsvToLong
import util.readFileToText
import kotlin.streams.toList

private val input = """3,8,1005,8,311,1106,0,11,0,0,0,104,1,104,0,3,8,102,-1,8,10,101,1,10,10,4,10,1008,8,1,10,4,10,1001,8,0,29,1006,0,98,2,1005,8,10,1,1107,11,10,3,8,102,-1,8,10,1001,10,1,10,4,10,1008,8,0,10,4,10,101,0,8,62,1006,0,27,2,1002,12,10,3,8,1002,8,-1,10,1001,10,1,10,4,10,108,0,8,10,4,10,1002,8,1,90,1,1006,1,10,2,1,20,10,3,8,102,-1,8,10,1001,10,1,10,4,10,1008,8,1,10,4,10,102,1,8,121,1,1003,5,10,1,1003,12,10,3,8,102,-1,8,10,101,1,10,10,4,10,1008,8,1,10,4,10,1002,8,1,151,1006,0,17,3,8,102,-1,8,10,1001,10,1,10,4,10,108,0,8,10,4,10,1002,8,1,175,3,8,102,-1,8,10,1001,10,1,10,4,10,108,1,8,10,4,10,101,0,8,197,2,6,14,10,1006,0,92,1006,0,4,3,8,1002,8,-1,10,101,1,10,10,4,10,108,0,8,10,4,10,1001,8,0,229,1006,0,21,2,102,17,10,3,8,1002,8,-1,10,101,1,10,10,4,10,1008,8,1,10,4,10,1001,8,0,259,3,8,102,-1,8,10,1001,10,1,10,4,10,108,0,8,10,4,10,102,1,8,280,1006,0,58,1006,0,21,2,6,11,10,101,1,9,9,1007,9,948,10,1005,10,15,99,109,633,104,0,104,1,21101,937150919572,0,1,21102,328,1,0,1105,1,432,21101,0,387394675496,1,21102,1,339,0,1106,0,432,3,10,104,0,104,1,3,10,104,0,104,0,3,10,104,0,104,1,3,10,104,0,104,1,3,10,104,0,104,0,3,10,104,0,104,1,21102,46325083283,1,1,21102,1,386,0,1106,0,432,21101,0,179519401051,1,21102,397,1,0,1106,0,432,3,10,104,0,104,0,3,10,104,0,104,0,21102,1,868410348308,1,21102,1,420,0,1105,1,432,21102,718086501140,1,1,21102,1,431,0,1105,1,432,99,109,2,22101,0,-1,1,21101,40,0,2,21101,0,463,3,21101,453,0,0,1106,0,496,109,-2,2105,1,0,0,1,0,0,1,109,2,3,10,204,-1,1001,458,459,474,4,0,1001,458,1,458,108,4,458,10,1006,10,490,1101,0,0,458,109,-2,2105,1,0,0,109,4,2102,1,-1,495,1207,-3,0,10,1006,10,513,21102,0,1,-3,22102,1,-3,1,22102,1,-2,2,21102,1,1,3,21102,1,532,0,1105,1,537,109,-4,2105,1,0,109,5,1207,-3,1,10,1006,10,560,2207,-4,-2,10,1006,10,560,22101,0,-4,-4,1105,1,628,22102,1,-4,1,21201,-3,-1,2,21202,-2,2,3,21102,1,579,0,1105,1,537,22101,0,1,-4,21102,1,1,-1,2207,-4,-2,10,1006,10,598,21102,1,0,-1,22202,-2,-1,-2,2107,0,-3,10,1006,10,620,22102,1,-1,1,21102,1,620,0,105,1,495,21202,-2,-1,-2,22201,-4,-2,-4,109,-5,2106,0,0"""

private class Robot(column: Int = 0, row: Int = 0, var direction: Int = 0) : Coordinates(column, row)  {

    fun turnRight() {
        direction += 90
        move()
    }
    fun turnLeft() {
        direction += 270
        move()
    }

    fun move() {
        direction = direction.rem(360)
        column += when (direction) {
            90   -> 1
            270  -> -1
            else -> 0
        }
        row += when (direction) {
            0   -> -1
            180  -> 1
            else -> 0
        }
    }
}

private abstract class Coordinates(var column: Int, var row: Int) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Coordinates

        if (column != other.column) return false
        if (row != other.row) return false

        return true
    }

    override fun hashCode(): Int {
        var result = column
        result = 31 * result + row
        return result
    }

    override fun toString(): String {
        return "Coordinates(column=$column, row=$row)"
    }


}

private class Field(column: Int, row: Int) : Coordinates(column, row)

private enum class Color(val color: Int) {
    BLACK(0),
    WHITE(1)
}

private class Parameters(var fields: MutableMap<Field, Color> = mutableMapOf(), var output: MutableList<Long> = mutableListOf(), var haltOccured: Boolean = false) {
    val robot = Robot()
    var count = 0

    fun getAmountOfPaintedFields(): Int {
        handleOutput()
        return fields.size
    }
    fun getNextValue(): Long {
        handleOutput()
        return fields[getRobotField()]!!.color.toLong()
    }

    private fun getRobotField() : Field  {
        if (!fields.containsKey(Field(robot.column, robot.row))) {
            count += 1
        }
        fields.putIfAbsent(Field(robot.column, robot.row), Color.BLACK)
        return fields.keys.first { it == Field(robot.column, robot.row) }

    }

    private fun handleOutput() {
        while(output.isNotEmpty()) {
            fields[getRobotField()] = when(output.removeAt(0)) {
                0.toLong() -> Color.BLACK
                1.toLong() -> Color.WHITE
                else       -> throw UnsupportedOperationException()
            }
            when(output.removeAt(0)) {
                0.toLong() -> robot.turnLeft()
                1.toLong() -> robot.turnRight()
                else -> throw UnsupportedOperationException()
            }
        }
    }
    fun displayGrid() {
        val xMin = fields.keys.minByOrNull { it.column }!!.column
        val xMax = fields.keys.maxByOrNull { it.column }!!.column
        val yMin = fields.keys.minByOrNull { it.row }!!.row
        val yMax = fields.keys.maxByOrNull { it.row }!!.row
        for (row in yMin..yMax) {
            for (column in xMin..xMax) {
                if (fields.filter { it.key.row == row && it.key.column == column }.values.firstOrNull() == Color.WHITE) {
                    print("#")
                } else print(" ")
            }
            println()
        }
    }
}

fun main() {
    val memory: MutableList<Long> = input.split(",").stream().mapToLong { it.toLong() }.toList().toMutableList()
    val params = Parameters()
    println("A: " + solve(params, memory, false))
    //REPLACE VALUE FOR WHITE AND BLACK TO SOLVE PART 2
    println("B: " + solve(params, memory, false))
    params.displayGrid()
}

private fun solve(params: Parameters, memory : MutableList<Long>, debug: Boolean) : String {
    if (debug) {
        OptCodeProgram(memory).debug(params)
    } else {
        OptCodeProgram(memory).run(params)
    }
    return params.getAmountOfPaintedFields().toString()
}

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
