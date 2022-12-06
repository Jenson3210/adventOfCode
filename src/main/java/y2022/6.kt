package y2022

import utils.printDay
import utils.readFileToText
import java.util.*

fun main() {
    printDay(1)
    println(findStartMarker(readFileToText(2022, 6), 4))

    printDay(2)
    println(findStartMarker(readFileToText(2022, 6), 14))
}

private fun findStartMarker(input: String, markerLength: Int): Int {
    input.toList().foldIndexed(Pair<Int, LinkedList<Char>>(0, LinkedList())) { index, acc, c ->
        if (acc.second.size == markerLength) acc.second.poll()
        acc.second.add(c)
        if (acc.second.size == markerLength && acc.second.distinct().size == markerLength) {
            return acc.first  + 1
        }
        index + 1 to acc.second
    }

    return 0
}
