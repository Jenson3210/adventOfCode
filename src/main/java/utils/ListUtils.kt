package utils

fun List<List<*>>.commonObjects(): List<*> {
    val list = this.first().toMutableList()
    for (l in this) {
        list.removeIf { !list.intersect(l).contains(it) }
    }
    return list
}

fun <T : Any> List<T>.mostCommonElements(): List<T> {
    val elementsWithTheirCount = getElementsWithTheirCount()
    val maxValue = elementsWithTheirCount.values.maxByOrNull { it }

    return elementsWithTheirCount.filter { it.value == maxValue }.map { it.key }
}

fun <T : Any> List<T>.leastCommonElements(): List<T> {
    val elementsWithTheirCount = getElementsWithTheirCount()
    val minValue = elementsWithTheirCount.values.minByOrNull { it }

    return elementsWithTheirCount.filter { it.value == minValue }.map { it.key }
}

fun <T : Any> List<T>.getElementsWithTheirCount(): Map<T, Int> {
    return groupingBy { it }.eachCount()
}