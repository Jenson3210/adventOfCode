package utils

import java.util.stream.Stream

fun leastCommonMultiplication(listOfNumbers: List<Int>) : Long {
    val numbers = listOfNumbers.toMutableList()
    var divider = 2
    var counter: Int
    var dividable: Boolean
    var lcm: Long = 1

    while (true) {
        counter = 0
        dividable = false

        for (index in numbers.indices) {

            // If one item is null, impossible to find lcm
            if (numbers[index] == 0) {
                return 0
            // Converting negative numbers into positives
            } else if (numbers[index] < 0) {
                numbers[index] = numbers[index] * (-1)
            }
            if (numbers[index] == 1) {
                counter++
            }

            // Divide element_array by devisor if complete
            // division i.e. without remainder then replace
            // number with quotient; used for find next factor
            if (numbers[index] % divider == 0) {
                dividable = true
                numbers[index] = numbers[index] / divider
            }
        }

        // If divisor able to completely divide any number
        // from array multiply with lcm_of_array_elements
        // and store into lcm_of_array_elements and continue
        // to same divisor for next factor finding.
        // else increment divisor
        if (dividable) {
            lcm *= divider
        } else {
            divider++
        }

        // Check if all element_array is 1 indicate
        // we found all factors and terminate while loop.
        if (counter == numbers.size) {
            return lcm
        }
    }
}

fun cartesianProduct(vararg lists: List<*>): Set<List<*>> =
    lists
        .fold(listOf(listOf<Any?>())) { acc, list ->
            acc.flatMap { newList -> list.map { element -> newList + element } }
        }
        .toSet()

//removed as part of day 14 for 2020. Earlier solutions might be broken
// fun cartesianProduct(a: List<*>, b: List<*>, vararg lists: List<*>): Set<List<*>> =
//    (listOf(a, b).plus(lists))
//        .fold(listOf(listOf<Any?>())) { acc, list ->
//            acc.flatMap { newList -> list.map { element -> newList + element } }
//        }
//        .toSet()

fun isBetween(a: Int, lowerBound: Int, upperBound: Int): Boolean {
    if (a < lowerBound) return false
    if (a > upperBound) return false
    return true
}

fun isFullyBetween(a: Int, lowerBound: Int, upperBound: Int): Boolean {
    if (a <= lowerBound) return false
    if (a >= upperBound) return false
    return true
}

inline fun <T> Iterable<T>.sumByLong(selector: (T) -> Long): Long {
    var sum = 0L
    for (element in this) {
        sum += selector(element)
    }
    return sum
}

fun Stream<Long>.sum(): Long {
    var sum = 0L
    for (element in this) {
        sum += element
    }
    return sum
}


inline fun <T> Iterable<T>.multiplyLong(selector: (T) -> Long): Long {
    var result = 1L
    for (element in this) {
        result *= selector(element)
    }
    return result
}

class Counter(var value: Long = 0) {
    fun increment() {
        increment(1)
    }
    fun increment(amount: Long) {
        value += amount
    }

    override fun toString(): String {
        return value.toString()
    }
}