package y2020

import utils.printDay
import utils.readFileLineByLineToText
import kotlin.streams.toList

fun main() {
    printDay(1)
    print("Valid string count: ")
    println(Validator(readFileLineByLineToText(2020, 19).toList()).countValidRules())

    printDay(2)
    print("Valid string count: ")
    println(Validator(readFileLineByLineToText(2020, 19).toList())
        .replaceRule("8: 42 | 42 8")
        .replaceRule("8: 42 | 42 8")
        .countValidRules())
}

private class Validator(input: List<String>) {
    val rules = mutableMapOf<Int, String>()
    val toValidate = mutableListOf<String>()

    init {
        var readingRules = true
        input.forEach {
            if (readingRules) {
                if (it.isBlank()) {
                    readingRules = false
                } else {
                    rules[it.substringBefore(":").toInt()] = it.substringAfter(": ").replace("\"", "")
                }
            } else {
                toValidate.add(it)
            }
        }
    }

    fun replaceRule(string: String) : Validator {
        rules[string.substringBefore(":").toInt()] = string.substringAfter(": ").replace("\"", "")
        return this
    }

    fun countValidRules(): Int {
        return toValidate.count { it.validLine(rules, listOf(0)) }
    }
}

private fun String.validLine(rules: Map<Int, String>, ruleNrsToValidate: List<Int>) : Boolean {
    return emptyLineValid(ruleNrsToValidate) || (!ruleNrsToValidate.isEmpty() && rules[ruleNrsToValidate[0]].let {
        if (it!!.first().isLetter()) {
            if (this.startsWith(it.first())) {
                this.drop(1).validLine(rules, ruleNrsToValidate.drop(1))
            } else false
        } else {
            it.split(" | ").any {
                this.validLine(rules, it.split(" ").map(String::toInt) + ruleNrsToValidate.drop(1))
            }
        }
    })
}

private fun String.emptyLineValid(ruleNrsToValidate: List<Int>) : Boolean {
    return this.isEmpty() && ruleNrsToValidate.isEmpty()
}