package `2019`.`04`

import util.readFileLineCsvToInt
import kotlin.streams.toList

fun main() {
    val range = readFileLineCsvToInt("04.txt").toList()
    var password: String
    val options: MutableList<Int> = mutableListOf()
    for (i in range[0]..range[1]) {
        try {
            password = i.toString()
            verifyTwoAdjacentTheSameAndNotPartOfLargerGroup(password)
            verifyNeverDecrease(password)

            options.add(i)
        } catch (InvalidPasswordExceptionB: InvalidPasswordExceptionB) {

        }
    }
    println("B: " + options.size)
}

private fun verifyNeverDecrease(password: String) {
    for (i in password.indices) {
        if (password.length - 1  != i && password[i] > password[i + 1]) {
            throw InvalidPasswordExceptionB()
        }
    }
}

private fun verifyTwoAdjacentTheSameAndNotPartOfLargerGroup(password: String) {
    var occured = false;
    for (i in password.indices) {
        if (password.length - 1  != i && password[i] == password[i + 1] && !twoAdjacentPartOfLargerGroup(password, i, i + 1 )) {
            occured = true
        }
    }
    if (!occured) throw InvalidPasswordExceptionB()
}

private fun twoAdjacentPartOfLargerGroup(password: String, i1: Int, i2: Int) : Boolean{
    var partOfLargerGroup = false
    if (i1 - 1 >= 0 && password[i1 - 1] == password[i1]) {
        partOfLargerGroup = true
    }
    if (password.length -1 != i2 && password[i2 + 1] == password[i2]) {
        partOfLargerGroup = true
    }
    return partOfLargerGroup
}

private class InvalidPasswordExceptionB() : RuntimeException()
