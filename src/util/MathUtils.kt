package util

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