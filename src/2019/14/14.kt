package `2019`.`14`

import util.readFileLineByLineToText
import kotlin.math.ceil

fun main() {
    val reactor = Reactor()
    for (reaction in readFileLineByLineToText("2019_14.txt")) {
        val name = reaction.split(" => ")[1].split(" ")[1]
        val amount = reaction.split(" => ")[1].split(" ")[0].toLong()
        val requirements = reaction.split(" => ")[0].split(", ").map { Pair(it.split(" ")[1], it.split(" ")[0].toLong()) }.toList()
        reactor.reactions.add(Reaction(name, requirements, amount))
    }
    println(reactor.calculateMinOreRequirements(1, "FUEL"))

}

private class Reactor(val stock: MutableMap<String, Long> = mutableMapOf(), val reactions: MutableList<Reaction> = mutableListOf()) {
    fun calculateMinOreRequirements(amount: Long, result: String) : Long {
        var amountRequired = 0L
        if (amount != 0L) {
            val resultAmount = reactions.filter { it.name == result }.map { it.amount }.first()
            val reactionAmount = ceil(amount.toDouble() / resultAmount).toInt()
            for (requirement in reactions.filter { it.name == result }.first().requirements) {
                if (!containsStock(amount, requirement.first)) {
                    if (requirement.first == "ORE") {
                        amountRequired += reactionAmount * requirement.second
                    } else {
                        val amountNotInStock = getAmountNotInStock(requirement.second * reactionAmount, requirement.first)
                        amountRequired += calculateMinOreRequirements(amountNotInStock, requirement.first)
                    }
                    addStock(reactionAmount * resultAmount - amount, result)
                }
                else {
                    useStock(requirement.second * reactionAmount, requirement.first)
                }
            }
        }
        return amountRequired
    }

    private fun getAmountNotInStock(amount: Long, reaction: String): Long {
        if (stock.containsKey(reaction)) {
            return amount - stock[reaction]!!
        }
        return amount
    }

    private fun addStock(amount: Long, reaction: String) {
        stock.putIfAbsent(reaction, 0)
        stock[reaction] = amount
    }

    private fun useStock(amount: Long, reaction: String) {
        if (stock.containsKey(reaction)) {
            stock[reaction] = if (amount > stock[reaction]!!) 0 else stock[reaction]!! - amount
        }
    }

    private fun containsStock(amount: Long, reaction: String): Boolean = stock.filter { it.key == reaction }.map { it.value }.sum() >= amount
}
private class Reaction(val name: String, val requirements : List<Pair<String, Long>>, val amount: Long)