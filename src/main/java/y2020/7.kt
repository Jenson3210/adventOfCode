package y2020

import utils.printDay
import utils.readFileLineByLineToText
import kotlin.streams.toList

fun main() {
    printDay(1)
    print("The amount of bags that can contain a shiny golden bag: ")
    println(canContainBag("shiny gold", readFileLineByLineToText(2020, 7).map { Rule(it) }.toList()).count())

    printDay(2)
    print("Amount of bags a shiny gold bag has to contain: ")
    println(getBagCount("shiny gold", readFileLineByLineToText(2020, 7).map { Rule(it) }.toList()) - 1)
}

private class Rule() {
    lateinit var color: String
    lateinit var contains: List<Rule>
    var amount = 0

    constructor(rule:String): this() {
        amount = 1
        val split = rule.lowercase().split(" bags contain ", " bag.", " bags.")
        color = split[0]
        if(split[1] == "no other") {
            contains = listOf()
        } else {
            val splitColors = split[1].split(" bag, ", " bags, ", " bags.")
            val rules: MutableList<Rule> = mutableListOf()
            splitColors.forEach {
                val singleRule = it.split(" ", limit = 2)
                rules.add(Rule(singleRule[1], singleRule[0].toInt()))
            }
            contains = rules.toList()
        }
    }

    constructor(color:String, amount: Int): this() {
        this.amount = amount
        this.color = color
        this.contains = listOf()
    }

    override fun toString(): String {
        return color
    }
}

private fun canContainBag(color: String, rules: List<Rule>): List<Rule> {
    val result: MutableList<Rule> = mutableListOf()

    val rulesContainingThisColor = rules.filter { it.contains.map { it.color.lowercase() }.contains(color.lowercase()) }.toList()
    result.addAll(rulesContainingThisColor)
    result.addAll(rulesContainingThisColor.flatMap { canContainBag(it.color, rules) }.toList())
    return result.distinct()
}

private fun getBagCount(color: String, rules: List<Rule>): Long {
    val thisBagRule = rules.filter { it.color.lowercase() == color.lowercase() }.get(0)
    if (thisBagRule.contains.isEmpty()) {
        return 1
    }
    return thisBagRule.contains.map { getBagCount(it.color, rules) * it.amount }.sum() + 1 * thisBagRule.amount
}