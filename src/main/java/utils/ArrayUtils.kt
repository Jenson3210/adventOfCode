package utils;

fun <T> Array<Array<T>>.northOrNull(rowIndex: Int, seatIndex: Int): T? {
    return directionOrNull(rowIndex, seatIndex, this::north)
}

fun <T> Array<Array<T>>.eastOrNull(rowIndex: Int, seatIndex: Int): T? {
    return directionOrNull(rowIndex, seatIndex, this::east)
}

fun <T> Array<Array<T>>.southOrNull(rowIndex: Int, seatIndex: Int): T? {
    return directionOrNull(rowIndex, seatIndex, this::south)
}

fun <T> Array<Array<T>>.westOrNull(rowIndex: Int, seatIndex: Int): T? {
    return directionOrNull(rowIndex, seatIndex, this::west)
}

fun <T> Array<Array<T>>.northEastOrNull(rowIndex: Int, seatIndex: Int): T? {
    return directionOrNull(rowIndex, seatIndex, this::northEast)
}

fun <T> Array<Array<T>>.northWestOrNull(rowIndex: Int, seatIndex: Int): T? {
    return directionOrNull(rowIndex, seatIndex, this::northWest)
}

fun <T> Array<Array<T>>.southEastOrNull(rowIndex: Int, seatIndex: Int): T? {
    return directionOrNull(rowIndex, seatIndex, this::southEast)
}

fun <T> Array<Array<T>>.southWestOrNull(rowIndex: Int, seatIndex: Int): T? {
    return directionOrNull(rowIndex, seatIndex, this::southWest)
}

fun <T> Array<Array<T>>.north(rowIndex: Int, seatIndex: Int): T? {
    if (rowIndex > 0) {
        return this[rowIndex - 1][seatIndex]
    }
    throw IndexOutOfBoundsException()
}

fun <T> Array<Array<T>>.east(rowIndex: Int, seatIndex: Int): T? {
    if (seatIndex < this.first().size) {
        return this[rowIndex][seatIndex + 1]
    }
    throw IndexOutOfBoundsException()
}

fun <T> Array<Array<T>>.south(rowIndex: Int, seatIndex: Int): T? {
    if (rowIndex < this.size) {
        return this[rowIndex + 1][seatIndex]
    }
    throw IndexOutOfBoundsException()
}

fun <T> Array<Array<T>>.west(rowIndex: Int, seatIndex: Int): T? {
    if (seatIndex > 0) {
        return this[rowIndex][seatIndex - 1]
    }
    throw IndexOutOfBoundsException()
}

fun <T> Array<Array<T>>.northWest(rowIndex: Int, seatIndex: Int): T? {
    if (rowIndex > 0 && seatIndex > 0) {
        return this[rowIndex - 1][seatIndex -1]
    }
    throw IndexOutOfBoundsException()
}


fun <T> Array<Array<T>>.northEast(rowIndex: Int, seatIndex: Int): T? {
    if (rowIndex > 0 && seatIndex < this.first().size) {
        return this[rowIndex - 1][seatIndex + 1]
    }
    throw IndexOutOfBoundsException()
}

fun <T> Array<Array<T>>.southWest(rowIndex: Int, seatIndex: Int): T? {
    if (rowIndex < this.size && seatIndex > 0) {
        return this[rowIndex + 1][seatIndex -1]
    }
    throw IndexOutOfBoundsException()
}


fun <T> Array<Array<T>>.southEast(rowIndex: Int, seatIndex: Int): T? {
    if (rowIndex < this.size && seatIndex < this.first().size) {
        return this[rowIndex + 1][seatIndex + 1]
    }
    throw IndexOutOfBoundsException()
}

fun <T> Array<Array<T>>.allNorth(rowIndex: Int, seatIndex: Int): List<T> {
    return allInDirection(rowIndex, seatIndex, this::north, rowIncrement = -1)
}

fun <T> Array<Array<T>>.allEast(rowIndex: Int, seatIndex: Int): List<T> {
    return allInDirection(rowIndex, seatIndex, this::east, seatIncrement = 1)
}

fun <T> Array<Array<T>>.allSouth(rowIndex: Int, seatIndex: Int): List<T> {
    return allInDirection(rowIndex, seatIndex, this::south, rowIncrement = 1)
}

fun <T> Array<Array<T>>.allWest(rowIndex: Int, seatIndex: Int): List<T> {
    return allInDirection(rowIndex, seatIndex, this::west, seatIncrement = -1)
}

fun <T> Array<Array<T>>.allNorthEast(rowIndex: Int, seatIndex: Int): List<T> {
    return allInDirection(rowIndex, seatIndex, this::northEast, rowIncrement = -1, seatIncrement = 1)
}

fun <T> Array<Array<T>>.allNorthWest(rowIndex: Int, seatIndex: Int): List<T> {
    return allInDirection(rowIndex, seatIndex, this::northWest, rowIncrement = -1, seatIncrement = -1)
}

fun <T> Array<Array<T>>.allSouthEast(rowIndex: Int, seatIndex: Int): List<T> {
    return allInDirection(rowIndex, seatIndex, this::southEast, rowIncrement = 1, seatIncrement = 1)
}

fun <T> Array<Array<T>>.allSouthWest(rowIndex: Int, seatIndex: Int): List<T> {
    return allInDirection(rowIndex, seatIndex, this::southWest, rowIncrement = 1, seatIncrement = -1)
}

private fun <T>  directionOrNull(rowIndex: Int, seatIndex: Int, directionHelper: (Int, Int) -> T?) : T? {
    return try {
        directionHelper.invoke(rowIndex, seatIndex)
    } catch (ex: IndexOutOfBoundsException) {
        null
    }
}

private fun <T> allInDirection(
    rowIndex: Int,
    seatIndex: Int,
    directionHelper: (Int, Int) -> T?,
    rowIncrement: Int = 0,
    seatIncrement: Int = 0
) : List<T> {
    val toReturn = mutableListOf<T>()
    var item: T?
    var inGrid = true
    var row = rowIndex
    var seat = seatIndex

    while (inGrid) {
        try {
            item = directionHelper.invoke(row, seat)
            if (item != null) {
                toReturn.add(item)
            }
            row+=rowIncrement
            seat+=seatIncrement
        } catch (ex: IndexOutOfBoundsException) {
            inGrid = false
        }
    }
    return toReturn
}