package `2020`.`20`

import util.multiplyLong
import util.printDay
import util.readFileLineByLineToText
import java.util.*
import java.util.stream.Stream

fun main() {
    val image = Image(readFileLineByLineToText("2020_20REAL.txt"))
    printDay(1)
    print("Corners multiplied: ")
    println(image.getCorners().multiplyLong { it.tileId.toLong() })

    printDay(2)
    print("The habitat's water roughness is: ")
//    println(readFileLineByLineToText("2020_19.txt").toList())
}

private class Image(input: Stream<String>) {
    val tiles = input.toImageTiles()
    val image: MutableList<List<ImageTile>> = mutableListOf()

    fun getCorners(): List<ImageTile> {
        return listOf(image.first().first(), image.first().last(), image.last().first(), image.last().last())
    }

    init {
        val remainingTiles = tiles.toMutableList()

        var remainingBorder = tiles.flatMap { it.getBorders() }.groupBy { it }.filter { it.value.size == 1 }.keys.toList()
        var cornerTiles = remainingTiles.filter { it.getBorders().intersect(remainingBorder).size == 2 }.toMutableList()
        var topLeftCorner = cornerTiles.removeAt(0)
        var topRightCorner = cornerTiles.minBy { topLeftCorner.getPathToPassingBy(remainingTiles, it, remainingBorder).size }!!
        var path: List<ImageTile>
        do {
            path = topLeftCorner.getPathToPassingBy(remainingTiles, topRightCorner, remainingBorder)
            image.add(path)

            remainingTiles.removeAll(path)
            remainingBorder = path.flatMap { it.getBorders() }
            try {
                topLeftCorner = remainingTiles.first { it.getBorders().intersect(path.first().getBorders()).size == 1 }
                topRightCorner = remainingTiles.first { it.getBorders().intersect(path.last().getBorders()).size == 1 }
            } catch (ignore: NoSuchElementException) {}
        } while (remainingTiles.isNotEmpty())
    }

}

private class ImageTile(val tileId: Int, val bytes: List<String>) {
    fun getBorders() : List<String> {
        return listOf(
            getSortedBorder(String(bytes.map { it.first() }.toCharArray())),
            getSortedBorder(String(bytes.map { it.last() }.toCharArray())),
            getSortedBorder(bytes.first()),
            getSortedBorder(bytes.last())
        )
    }

    fun getTile(borders: List<String>) : List<String> {
        var bytes = this.bytes

        borders.forEach {
            when (it) {
                bytes.first().reversed() -> bytes = bytes.map { it.reversed() }
                bytes.last() -> bytes = bytes.reversed()
                bytes.last().reversed() -> bytes = bytes.reversed().map { it.reversed() }
                String(bytes.map { it.first() }.toCharArray()) -> bytes = bytes.indices.map { index -> String(bytes.map { it[index] }.toCharArray()) }
                String(bytes.map { it.first() }.toCharArray()).reversed() -> bytes = bytes.indices.map { index -> String(bytes.map { it[index] }.toCharArray()).reversed() }
                String(bytes.map { it.last() }.toCharArray()) -> bytes = bytes.indices.map { index -> String(bytes.map { it.reversed()[index] }.toCharArray()) }
                String(bytes.map { it.last() }.toCharArray()).reversed() -> bytes = bytes.indices.map { index -> String(bytes.map { it.reversed()[index] }.toCharArray()).reversed() }
            }
        }

        return bytes
    }

    fun getPathToPassingBy(possibleTiles: List<ImageTile>, destination: ImageTile, borders: List<String>) : List<ImageTile> {
        if (this.tileId == destination.tileId) {
            return listOf(this)
        }
        val shortestPathNeighbor = possibleTiles.filter { it.getBorders().intersect(borders).isNotEmpty() }.minus(this).filter { it.isNeighbour(this) }.minBy { it.getPathToPassingBy(possibleTiles.minus(this), destination, borders).size }!!

        return listOf(this).plus(shortestPathNeighbor.getPathToPassingBy(possibleTiles.minus(this), destination, borders))
    }

    private fun isNeighbour(other: ImageTile) : Boolean {
        return other.getBorders().intersect(getBorders()).isNotEmpty()
    }

    private fun getSortedBorder(string: String) : String{
        return listOf(string, string.reversed()).min()!!
    }

    override fun toString(): String {
        return tileId.toString()
    }
}

private fun Stream<String>.toImageTiles() : List<ImageTile> {
    val list = mutableListOf<ImageTile>()
    var id: Int = 0
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