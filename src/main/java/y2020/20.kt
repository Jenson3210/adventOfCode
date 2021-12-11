package y2020

import utils.multiplyLong
import utils.printDay
import utils.readFileLineByLineToText
import java.util.stream.Stream

fun main() {
    val image = Image(readFileLineByLineToText(2020, 20))
    printDay(1)
    print("Corners multiplied: ")
    println(image.getCorners().multiplyLong { it.tileId.toLong() })

    printDay(2)
    print("The habitat's water roughness is: ")
    println(image.getImage().getAllDirectionsCopy().first { it.searchObjects(getSeaMonster()) }.countRoughWaters())
}

private class Image(input: Stream<String>) {
    val tiles = input.toImageTiles()
    val image: MutableList<List<ImageTile>> = mutableListOf()

    fun getCorners(): List<ImageTile> {
        return listOf(image.first().first(), image.first().last(), image.last().first(), image.last().last())
    }

    init {
        val remainingTiles = tiles.toMutableList()

        var remainingBorder =
            tiles.flatMap { it.getNESWBorders() }.groupBy { it }.filter { it.value.size == 1 }.keys.toList()
        var cornerTiles =
            remainingTiles.filter { it.getNESWBorders().intersect(remainingBorder).size == 2 }.toMutableList()
        var topLeftCorner = cornerTiles.removeAt(0)
        var topRightCorner =
            cornerTiles.minByOrNull { topLeftCorner.getPathToPassingBy(remainingTiles, it, remainingBorder).size }!!
        var path: List<ImageTile>
        do {
            path = topLeftCorner.getPathToPassingBy(remainingTiles, topRightCorner, remainingBorder)
            image.add(path)

            remainingTiles.removeAll(path)
            remainingBorder = path.flatMap { it.getNESWBorders() }
            try {
                topLeftCorner =
                    remainingTiles.first { it.getNESWBorders().intersect(path.first().getNESWBorders()).size == 1 }
                topRightCorner =
                    remainingTiles.first { it.getNESWBorders().intersect(path.last().getNESWBorders()).size == 1 }
            } catch (ignore: NoSuchElementException) {
            }
        } while (remainingTiles.isNotEmpty())

        image.fixOrientation()
    }

    fun getImage() : ImageTile {
        return image.toTile(0)
    }

}

private class ImageTile(val tileId: Int, var bytes: MutableList<String>) {
    fun getNESWBorders(): List<String> {
        return getRealNESWBorders().map { listOf(it, it.reversed()).minOrNull()!! }
    }

    fun getPathToPassingBy(
        possibleTiles: List<ImageTile>,
        destination: ImageTile,
        borders: List<String>
    ): List<ImageTile> {
        if (this.tileId == destination.tileId) {
            return listOf(this)
        }
        val shortestPathNeighbor =
            possibleTiles.filter { it.getNESWBorders().intersect(borders).isNotEmpty() }.minus(this)
                .filter { it.isNeighbour(this) }
                .minByOrNull { it.getPathToPassingBy(possibleTiles.minus(this), destination, borders).size }!!

        return listOf(this).plus(
            shortestPathNeighbor.getPathToPassingBy(
                possibleTiles.minus(this),
                destination,
                borders
            )
        )
    }

    private fun isNeighbour(other: ImageTile): Boolean {
        return other.getNESWBorders().intersect(getNESWBorders()).isNotEmpty()
    }

    private fun getRealNESWBorders(): List<String> {
        return listOf(
            bytes.first(),
            String(bytes.map { it.last() }.toCharArray()),
            bytes.last(),
            String(bytes.map { it.first() }.toCharArray())
        )
    }

    private fun flip() : ImageTile {
        bytes = bytes.map { String(it.reversed().toCharArray()) }.toMutableList()
        return this
    }

    private fun rotate() : ImageTile {
        bytes = bytes.mapIndexed { x, row ->
            String(row.mapIndexed { y, _ ->
                bytes[y][x]
            }.reversed().toCharArray())
        }.toMutableList()
        return this
    }

    override fun toString(): String {
        return tileId.toString()
    }

    fun setTopCorner() {
        rotate()
        flip()
    }

    fun under(above: ImageTile) {
        while (above.getNESWBorders().none { it == getNESWBorders()[0] }) {
            rotate()
        }
        if (above.getRealNESWBorders().none { it == getRealNESWBorders()[0] }) {
            flip()
            under(above)
        }
    }

    fun rightOf(left: ImageTile) {
        while (left.getNESWBorders().none { it == getNESWBorders()[3] }) {
            rotate()
        }
        if (left.getRealNESWBorders().none { it == getRealNESWBorders()[3] }) {
            flip()
            rightOf(left)
        }
    }

    fun getAllDirectionsCopy() : List<ImageTile> {
        return listOf(
            ImageTile(this.tileId, this.bytes),
            ImageTile(this.tileId, this.bytes).rotate(),
            ImageTile(this.tileId, this.bytes).rotate().rotate(),
            ImageTile(this.tileId, this.bytes).rotate().rotate().rotate(),
            ImageTile(this.tileId, this.bytes).flip(),
            ImageTile(this.tileId, this.bytes).flip().rotate(),
            ImageTile(this.tileId, this.bytes).flip().rotate().rotate(),
            ImageTile(this.tileId, this.bytes).flip().rotate().rotate().rotate()
        )
    }

    fun searchObjects(objectToSearch: List<Pair<Int, Int>>): Boolean {
        var found = false
        val maxHeight = objectToSearch.maxByOrNull { it.first }!!.first
        val maxWidth = objectToSearch.maxByOrNull { it.second }!!.second
        (0..(bytes.size - maxHeight)).forEach { x ->
            (0..(bytes.size - maxWidth)).forEach { y ->
                val actualSpots = objectToSearch.map { Pair(it.first + x,  it.second + y) }
                if (actualSpots.all { bytes[it.first][it.second] == '#' }) {
                    found = true
                    actualSpots.forEach {
                        val tempBytes = bytes[it.first].toCharArray()
                        tempBytes[it.second] = '0'
                        bytes[it.first] = String(tempBytes)
                    }
                }
            }
        }
        return found
    }

    fun countRoughWaters(): Int {
        println()
        bytes.forEach { println(it) }
        return bytes.sumOf { it.count { it == '#' } }
    }
}

private fun Stream<String>.toImageTiles(): List<ImageTile> {
    val list = mutableListOf<ImageTile>()
    var id = 0
    var imageBytes = mutableListOf<String>()
    this.forEach {
        if (!it.isBlank()) {
            if (it.contains("Tile")) {
                id = it.substringBefore(":").substringAfter(" ").toInt()
            } else {
                imageBytes.add(it)
            }
        } else {
            list.add(ImageTile(id, imageBytes))
            imageBytes = mutableListOf()

        }
    }
    list.add(ImageTile(id, imageBytes))
    return list
}

private fun List<List<ImageTile>>.fixOrientation() {
    var mostRecentTile: ImageTile = this[0][0]
    var mostRecentRowHeader: ImageTile = mostRecentTile
    (this.indices).forEach { row ->
        (this.indices).forEach { col ->
            when {
                row == 0 && col == 0 ->
                    mostRecentTile.setTopCorner()
                col == 0 -> {
                    this[row][0].under(mostRecentRowHeader)
                    mostRecentRowHeader = this[row][0]
                    mostRecentTile = mostRecentRowHeader
                }
                else -> {
                    this[row][col].rightOf(mostRecentTile)
                    mostRecentTile = this[row][col]
                }
            }
        }
    }

}

private fun List<List<ImageTile>>.toTile(id: Int): ImageTile {
    val bytes = mutableListOf<String>()
    var strings: MutableList<String>
    (this.indices).forEach { row ->
        strings = mutableListOf()
        (this.indices).forEach { col ->
            if (col == 0) {
                strings.addAll(this[row][0].bytes.drop(1).dropLast(1).map { it.drop(1).dropLast(1) })
            } else {
                this[row][col].bytes.drop(1).dropLast(1).withIndex().forEach {
                    strings[it.index] += it.value.drop(1).dropLast(1)
                }
            }
        }
        bytes.addAll(strings)
    }
    return ImageTile(id, bytes)
}

private fun getSeaMonster() : List<Pair<Int, Int>> {
    return listOf(
        Pair(0, 18), Pair(1, 0), Pair(1, 5), Pair(1, 6), Pair(1, 11), Pair(1, 12),
        Pair(1, 17), Pair(1, 18), Pair(1, 19), Pair(2, 1), Pair(2, 4), Pair(2, 7),
        Pair(2, 10), Pair(2, 13), Pair(2, 16)
    )
}