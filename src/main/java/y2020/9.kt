package y2020

import utils.cartesianProduct
import utils.printDay
import utils.readFileLineByLineToText
import utils.sumByLong
import kotlin.streams.toList

fun main() {
    printDay(1)
    val preamble = Preamble(25)
    print("The first number that does not follow the preamble: ")
    val solution = readFileLineByLineToText(2020, 9).toList().dropWhile { preamble.addNumber(it.toLong()) }.firstOrNull()
    println(solution)

    printDay(2)
    print("The first number that does not follow the preamble: ")
    println(preamble.getEncryptionWeakness(solution!!.toLong()))

}

private class Preamble(val preambleSize: Int) {
    val numbers = mutableListOf<Long>()

    fun addNumber(number: Long) : Boolean {
        if(numbers.size >= preambleSize && !validNumber(number)) {
            return false
        }
        numbers.add(number)
        return true
    }

    fun getEncryptionWeakness(number: Long) : Long {
        val encryptionWeaknessList = getEncryptionWeaknessList(number)
        if (encryptionWeaknessList.isNotEmpty()) {
            return encryptionWeaknessList.minOrNull()!! + encryptionWeaknessList.maxOrNull()!!
        }
        return 0
    }

    private fun getEncryptionWeaknessList(number: Long): List<Long> {
        var subList: MutableList<Long>
        var resultList: MutableList<Long>
        numbers.indices.forEach {
            subList = numbers.toMutableList().subList(it, numbers.lastIndex)
            resultList = mutableListOf()

            while (resultList.sum() < number) {
                resultList.add(subList.removeAt(0))
                if (resultList.sum() == number) return resultList
            }
        }
        return emptyList()
    }

    private fun validNumber(number: Long) : Boolean {
        return cartesianProduct(numbers.takeLast(preambleSize), numbers.takeLast(preambleSize))
            .filter { it.distinct().size != 1 }
            .map { it.sumByLong { it.toString().toLong() } }
            .contains(number)
    }
}