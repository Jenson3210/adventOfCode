package utils
interface Nested<T>
data class NestedList<T>(val list : MutableList<Nested<T>> = mutableListOf()): Nested<T>, Iterable<Nested<T>> {
    override fun iterator(): Iterator<Nested<T>> = list.iterator()
}

data class NestedObject<T>(val value : T): Nested<T>

fun <T> String.nestedReader(
    separator: String = ",",
    openingSeparator: Char = '[',
    closingSeparator: Char = ']',
    converter: (String) -> T
): NestedList<T> {
    val list = NestedList<T>()
    if (this.contains(openingSeparator)) {
        list.list.addAll(this.substringBefore(openingSeparator).nestedReader(separator, openingSeparator, closingSeparator, converter))
        var restString = this.substringAfter(openingSeparator)
        var innerString = ""

        var unmatchedOpenings = 0;
        for (i in restString.indices) {
            if (restString[i] == openingSeparator) {
                unmatchedOpenings++
            }
            if (restString[i] == closingSeparator) {
                if (unmatchedOpenings == 0) {
                    list.list.add(NestedList(innerString.nestedReader(separator, openingSeparator, closingSeparator, converter).list))
                    restString = restString.substringAfter(innerString)
                        .substringAfter(closingSeparator)
                        .substringAfter(separator)
                    if (restString.isNotBlank()) {
                        list.list.addAll(restString.nestedReader(separator, openingSeparator, closingSeparator, converter).list)
                    }
                    break
                } else {
                    unmatchedOpenings--
                }
            }
            innerString += restString[i]
        }
    } else {
        list.list.addAll(this.splitAndMap(separator, converter))
    }
    return list
}

private fun <T> String.splitAndMap(separator: String = ",", converter: (String) -> T) = this.split(separator).filter { it.isNotBlank() }.map { NestedObject(converter.invoke(it)) }
