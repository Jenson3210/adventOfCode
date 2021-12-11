package y2020

import utils.printDay
import utils.readFileLineByLineToText
import utils.sum

fun main() {
    printDay(1)
    print("Weird math result: ")
    println(readFileLineByLineToText(2020, 18).map { it.solveWithParentheses(String::solveSimpleMath) }.sum())

    printDay(2)
    print("Weird math result: ")
    println(readFileLineByLineToText(2020, 18).map { it.solveWithParentheses(String::solveAdvancedMath) }.sum())
}

private fun Long.solve(operator: String, number: Int) : Long {
    return when (operator) {
        "+" -> this + number
        "*" -> this * number
        else -> this
    }
}

private fun String.solveWithParentheses(solver: (String) -> Long): Long {
    if (this.contains("(")) {
        var string = this.substringBefore("(")
        val restString = this.substringAfter("(")
        var innerString = ""

        var unmatchedOpenings = 0;
        for(i in restString.indices) {
            if (restString[i] == '(') {
                unmatchedOpenings++
            }
            if (restString[i] == ')') {
                if (unmatchedOpenings == 0) {
                    string += innerString.solveWithParentheses(solver)
                    if (i != restString.indices.last) {
                        string += restString.substring(i + 1)
                    }
                    break
                } else {
                    unmatchedOpenings--
                }
            }
            innerString += restString[i]
        }

        return string.solveWithParentheses(solver)
    } else {
        return solver.invoke(this)
    }
}

private fun String.solveSimpleMath() : Long {
    var result = 0L
    var operator = "+"
    for (looper in this.split(" ")) {
        if (looper.contains(Regex("[0-9]+"))) {
            result = result.solve(operator, looper.toInt())
        } else {
            operator = looper
        }
    }

    return result
}

private fun String.solveAdvancedMath() : Long {
    var tempString = this
    while (tempString.contains("+")) {
        tempString =
            tempString.substringBefore(" + ").substringBeforeLast(" ", "") +
                    " " +
                    ( tempString.substringBefore(" + ").substringAfterLast(" ").toInt() +
                            tempString.substringAfter(" + ").substringBefore(" ").toInt()) +
                    " " +
                    tempString.substringAfter(" + ").substringAfter(" ", "")
    }
    return tempString.trim().solveSimpleMath()
}