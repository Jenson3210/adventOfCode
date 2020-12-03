package `2019`.`04`

import util.readFileLineCsvToInt
import kotlin.streams.toList

fun main() {
    val range = readFileLineCsvToInt("2019_04.txt").toList()
    var password: String
    val options: MutableList<Int> = mutableListOf()
    for (i in range[0]..range[1]) {
        try {
            password = i.toString()
            verifyTwoAdjacentTheSame(password)
            verifyNeverDecrease(password)
            options.add(i)
        } catch (InvalidPasswordExceptionA: InvalidPasswordExceptionA) {

        }
    }
    println("A: " + options.size)
}

private fun verifyNeverDecrease(password: String) {
    for (i in password.indices) {
        if (password.length - 1  != i && password[i] > password[i + 1]) {
            throw InvalidPasswordExceptionA()
        }
    }
}

private fun verifyTwoAdjacentTheSame(password: String) {
    var occured = false;
    for (i in password.indices) {
        if (password.length - 1  != i && password[i] == password[i + 1]) {
            occured = true
        }
    }
    if (!occured) throw InvalidPasswordExceptionA()
}

private class InvalidPasswordExceptionA() : RuntimeException()