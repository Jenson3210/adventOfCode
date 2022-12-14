package utils

interface PositionAware {
    fun setPosition(x: Int, y: Int);
}

fun <T> List<List<T>>.toNavigateableMap(): NavigateableMap<T> {
    val map =
        NavigateableMap(this.mapIndexed { row, list -> row to list.mapIndexed { column, it -> column to it }.toMap() }
            .toMap())

    if (map.cells().all { it is PositionAware }) {
        map.rows().forEachIndexed { rowNum, rowData ->
            rowData.cols().forEachIndexed { colNum, colData -> (colData as PositionAware).setPosition(rowNum, colNum) }
        }
    }

    return map
}

fun <T> Map<Int, T>.cols() = this.values

class NavigateableMap<T>(private val data: Map<Int, Map<Int, T>>) :Iterable<T> {

    fun rows() = data.values
    fun cells() = data.values.flatMap { it.values }

    fun rowIndices() = data.values.indices
    fun colIndices() = data.values.first().values.indices

    fun getNorthCell(x: Int, y: Int) = getCell(x, y, -1, 0);
    fun getEastCell(x: Int, y: Int) = getCell(x, y, 0, 1);
    fun getSouthCell(x: Int, y: Int) = getCell(x, y, 1, 0);
    fun getWestCell(x: Int, y: Int) = getCell(x, y, 0, -1);
    fun getNorthEastCell(x: Int, y: Int) = getCell(x, y, -1, 1);
    fun getSouthEastCell(x: Int, y: Int) = getCell(x, y, 1, 1);
    fun getNorthWestCell(x: Int, y: Int) = getCell(x, y, -1, -1);
    fun getSouthWestCell(x: Int, y: Int) = getCell(x, y, 1, -1);

    fun getSurroundingCells(x: Int, y: Int) = listOfNotNull(
        getNorthCell(x, y),
        getEastCell(x, y),
        getSouthCell(x, y),
        getWestCell(x, y),
        getNorthEastCell(x, y),
        getSouthEastCell(x, y),
        getNorthWestCell(x, y),
        getSouthWestCell(x, y)
    )

    fun getNESWSurroundingCells(x: Int, y: Int) =
        listOfNotNull(getNorthCell(x, y), getEastCell(x, y), getSouthCell(x, y), getWestCell(x, y))

    fun getCell(x: Int, y: Int) = getCell(x, y, 0, 0)
    fun first(predicate: (T) -> Boolean) = cells().first(predicate)
    private fun getCell(x: Int, y: Int, xOperation: Int, yOperation: Int) =
        data.getOrDefault(x + xOperation, mutableMapOf())[y + yOperation]

    override fun toString(): String = data.map { it.value.values.joinToString("") { it.toString() } }.joinToString("\n")
    override fun iterator(): Iterator<T> = cells().iterator()
}
