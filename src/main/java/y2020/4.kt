package y2020

import utils.isBetween
import utils.printDay
import utils.readFile
import java.io.File
import java.nio.charset.Charset
import kotlin.reflect.KFunction1


fun main() {
    printDay(1)
    print("Valid passwords: ")
    println(getValidPasswordCountDayOne(readFile(2020, 4), PassportField.values().filter { !it.key.equals("cid", ignoreCase = true) }))

    printDay(2)
    print("Valid passwords: ")
    println(getValidPasswordCountDayTwo(readFile(2020, 4), PassportField.values().filter { !it.key.equals("cid", ignoreCase = true) }))
}

private fun getValidPasswordCountDayOne(file: File, requiredFields: List<PassportField>): Long {
    return getValidPasswordCount(file, requiredFields, PassportValidator::hasAllRequiredValues)
}


private fun getValidPasswordCountDayTwo(file: File, requiredFields: List<PassportField>): Long {
    return getValidPasswordCount(file, requiredFields, PassportValidator::isValid)
}

private fun getValidPasswordCount(file: File, requiredFields: List<PassportField>, validatorMethod: KFunction1<PassportValidator, Boolean>): Long {
    var validPasswordCount = 0L

    val passportValidator = PassportValidator(requiredFields)
    file.forEachLine(Charset.defaultCharset()) {
        if (it.isBlank()) {
            if (validatorMethod.invoke(passportValidator)) validPasswordCount += 1
            passportValidator.clearValidator()
        } else {
            passportValidator.setFields(it);
        }
    }
    if (validatorMethod.invoke(passportValidator)) validPasswordCount += 1

    return validPasswordCount
}

private class PassportValidator(val requiredFields: List<PassportField>) {
    val fields: MutableMap<String, String> = HashMap()

    fun setField(input: String) {
        val split = input.split(":")
        fields[split[0]] = split[1]
    }

    fun setFields(input: String) {
        input.split(" ").forEach { setField(it) }
    }

    fun hasAllRequiredValues() : Boolean {
        return requiredFields.map { fields.get(it.key) }.none { it == null }
    }

    fun isValid() : Boolean {
        for (requiredField in requiredFields) {
            if (fields[requiredField.key] == null) return false
            if (!requiredField.validate(fields[requiredField.key]!!)) return false
        }
        return true
    }

    fun clearValidator() {
        fields.clear()
    }
}

enum class PassportField(val key: String, private val validation: (String) -> Boolean) {
    BIRTH_YEAR("byr", ::validBirthYear),
    ISSUE_YEAR("iyr", ::validIssueYear),
    EXPIRATION_YEAR("eyr", ::validExpirationYear),
    HEIGHT("hgt", ::validHeight),
    HAIR_COLOR("hcl", ::validHairColor),
    EYE_COLOR("ecl", ::validEyeColor),
    PASSPORT_ID("pid", ::validPassportId),
    COUNTRY_ID("cid", ::validCountryId);

    fun validate(input: String) : Boolean {
        return validation.invoke(input);
    }
}

fun validBirthYear(input: String) : Boolean {
    return isBetween(input.toInt(), 1920, 2002)
}

fun validIssueYear(input: String) : Boolean {
    return isBetween(input.toInt(), 2010, 2020)
}

fun validExpirationYear(input: String) : Boolean {
    return isBetween(input.toInt(), 2020, 2030)
}

fun validHeight(input: String) : Boolean {
    var lowerBound = 0
    var upperBound = 0
    var value: String? = null
    if (input.endsWith("cm", ignoreCase = true)) {
        lowerBound = 150
        upperBound = 193
        value = input.split("cm")[0]
    }
    if (input.endsWith("in", ignoreCase = true)) {
        lowerBound = 59
        upperBound = 76
        value = input.split("in")[0]
    }
    if (value != null) {
        return isBetween(value.toInt(), lowerBound, upperBound)
    }
    return false
}

fun validHairColor(input: String) : Boolean {
    return input.startsWith("#") && input.substring(1).uppercase().matches(Regex("[0-9A-F]+"))
}

fun validEyeColor(input: String) : Boolean {
    return listOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth").contains(input.lowercase())
}

fun validPassportId(input: String) : Boolean {
    return input.matches(Regex("[0-9]+")) && input.length == 9
}

fun validCountryId(input: String) : Boolean {
    return true;
}