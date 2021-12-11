package y2020

import utils.isBetween
import utils.printDay
import utils.readFileLineByLineToText


fun main() {
    printDay(1)
    print("Valid passwords: ")
    println(readFileLineByLineToText(2020, 2).map { getPasswordValidator(it) }.filter { it.isValidAtOldJob() }.count())

    printDay(2)
    print("Valid passwords: ")
    println(readFileLineByLineToText(2020, 2).map { getPasswordValidator(it) }.filter { it.isValidAtCurrentJob() }.count())
}

private class PasswordValidator(val firstNumber:Int, val secondNumber: Int, val character: Char, val password: String) {
    fun isValidAtOldJob() = isBetween(password.toCharArray().filter { character == it }.count(), firstNumber, secondNumber)
    fun isValidAtCurrentJob() : Boolean {
        var isValid = false;
        if (password.toCharArray()[firstNumber -1] == character) isValid = !isValid;
        if (password.toCharArray()[secondNumber -1] == character) isValid = !isValid;
        return isValid;
    }

}

private fun getPasswordValidator(encodedPassword: String): PasswordValidator {
    val split = encodedPassword.split("-", " ", ": ")
    val firstNumber = split[0].toInt()
    val secondNumber = split[1].toInt()
    val character = split[2][0]
    val password = split[3]
    return PasswordValidator(firstNumber, secondNumber, character, password)
}
