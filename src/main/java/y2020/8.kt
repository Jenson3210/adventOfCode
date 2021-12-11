package y2020

import utils.printDay
import utils.readFileLineByLineToText
import kotlin.streams.toList

fun main() {
    printDay(1)
    print("Result at the end of the program : ")
    println(Program(readFileLineByLineToText(2020, 8).map { operation(it) }.toList()).execute().getResult())

    printDay(2)
    print("Result at the end of the program : ")
    val operations = readFileLineByLineToText(2020, 8).map { operation(it) }.toList()
    val programs = mutableListOf<Program>()
    operations
        .forEachIndexed { index, operation ->
            if (operation.operationType == OperationType.JUMP ||  operation.operationType == OperationType.NOOP) {
                val alteredType = when (operations[index].operationType) {
                    OperationType.JUMP -> OperationType.NOOP
                    OperationType.NOOP -> OperationType.JUMP
                    else -> null
                }
                val alteredOperations =
                    operations.toMutableList().also { it[index] = Operation(alteredType!!, operation.value) }
                programs.add(Program(alteredOperations).execute())
            }
        }

    println(programs.filter { it.isFinished() }.firstOrNull()?.getResult())
}

private class Program(val operations: List<Operation>, var accumulator: Int = 0, var currentlyAtLine: Int = 0, val allowDoublePassings: Boolean = false, val debug: Boolean = false) {
    fun execute() : Program {
        operations.forEach { it.reset() }
        while(shouldExecuteLine()) {
            executeLine()
        }
        return this
    }

    fun isFinished() : Boolean {
        return currentlyAtLine == operations.size
    }

    fun getResult(): Int {
        return accumulator
    }

    private fun executeLine() {
        val operation = operations[currentlyAtLine]
        when (operation.operationType) {
            OperationType.ADD -> {
                accumulator += operation.value
                currentlyAtLine ++
            }
            OperationType.JUMP -> currentlyAtLine += operation.value
            else -> currentlyAtLine ++
        }
        operation.passed()
        if (debug) {
            println("At line: $currentlyAtLine\tvalue: $accumulator")
        }

    }

    private fun shouldExecuteLine() : Boolean {
        if (!allowDoublePassings) {
            return currentlyAtLine < operations.size && operations[currentlyAtLine].passings < 1
        }
        return currentlyAtLine < operations.size
    }
}

private class Operation(var operationType: OperationType, val value: Int) {
    var passings = 0

    fun passed() {
        passings += 1
    }

    fun reset() {
        passings = 0
    }
}

private enum class OperationType {
    ADD,
    JUMP,
    NOOP;
}

private fun operation(operationLine: String) : Operation {
    val splittedOperation = operationLine.split(" ")
    return Operation(operationType(splittedOperation[0]), splittedOperation[1].toInt())
}

private fun operationType(operator: String) : OperationType {
    return when(operator.lowercase()) {
        "acc" -> OperationType.ADD
        "jmp" -> OperationType.JUMP
        "nop" -> OperationType.NOOP
        else -> throw RuntimeException("Unknown operation type: $operator")
    }
}