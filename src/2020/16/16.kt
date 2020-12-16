package `2020`.`16`

import util.multiplyLong
import util.printDay
import util.readFileLineByLineToText
import util.sumByLong
import java.util.stream.Stream

fun main() {
    printDay(1)
    print("The error rate is ")
    val reader = TicketReader(readFileLineByLineToText("2020_16.txt"))
    println(reader.getErrorRate())

    printDay(2)
    reader.filterInvalidTickets()
    print("The department fields multiplied are  : ")
    println(reader.getDepartureFields())
}

private class TicketReader(inputLines: Stream<String>) {

    val ticketRules = mutableListOf<TicketRule>()
    val tickets = mutableListOf<List<Int>>()
    lateinit var ownTicket: List<Int>;

    init {
        var readingRules = true
        var ownTicketRead = false
        inputLines.forEach {
            if (it.contains(":")) {
                if (readingRules) {
                    ticketRules.add(TicketRule(it))
                }
            } else if (it.isNotBlank()) {
                if (ownTicketRead) {
                    tickets.add(it.split(",").map { it.toInt() })
                } else
                    ownTicket = it.split(",").map { it.toInt() }
                    ownTicketRead = true
            } else {
                readingRules = false
            }
        }
    }

    fun getErrorRate() : Long {
        val invalidNumbers = mutableListOf<Int>()
        tickets.forEach {
            invalidNumbers.addAll(it.filter { num -> num !in ticketRules.flatMap { rule -> rule.getValidNumbers(it) }.distinct() })
        }
        return invalidNumbers.sumByLong { it.toLong() }
    }

    fun getDepartureFields(): Long {
        return getFieldRulesMapping().entries.filter { ticketRules.take(6).contains(it.value) }.map { it.key }.map { ownTicket[it] }.multiplyLong { it.toLong() }
    }

    private fun getFieldRulesMapping() : Map<Int, TicketRule> {
        val ruleFields = mutableMapOf<Int, MutableList<TicketRule>>()
        for (i in tickets.first().indices) {
            ruleFields[i] = ticketRules.filter { it.isValid(tickets.flatMap { ticketNumbers -> listOf(ticketNumbers[i]) }) }.toMutableList()
        }
        return calculateFieldRulesMapping(ruleFields)
    }


    private fun calculateFieldRulesMapping(fieldNumberToPossibilities : MutableMap<Int, MutableList<TicketRule>>) : Map<Int, TicketRule> {
        val toReturn: MutableMap<Int, TicketRule> = mutableMapOf()
        while (fieldNumberToPossibilities.entries.any { it.value.size != 1 }) {
            for(entry in fieldNumberToPossibilities.filterKeys { !toReturn.containsKey(it) }) {
                if (entry.value.size == 1) {
                    toReturn[entry.key] = entry.value[0]
                } else {
                    entry.value.removeIf { toReturn.containsValue(it) }
                }
            }
        }
        return toReturn
    }

    fun filterInvalidTickets() {
        tickets.removeIf { ticketRules.none { rule -> rule.isValid(it) } }
    }
}

private class TicketRule(inputLine: String) {

    val allowedRanges = mutableListOf<IntRange>()

    init {
        inputLine
            .substringAfter(": ")
            .split(" or ")
            .forEach {
                allowedRanges.add(IntRange(it.substringBefore("-").toInt(), it.substringAfter("-").toInt()))
            }
    }

    fun isValid(list: List<Int>) : Boolean {
        return list.all { allowedRanges.any { range -> range.contains(it) } }
    }

    fun getValidNumbers(list: List<Int>) : List<Int> {
        return list.filter { number -> allowedRanges.any { it.contains(number) } }.toList()
    }
}