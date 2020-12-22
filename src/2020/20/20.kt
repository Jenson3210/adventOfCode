package `2020`.`20`

import util.printDay
import util.readFileLineByLineToText
import java.util.stream.Stream
import kotlin.math.sqrt

fun main() {
    printDay(1)
    print("Corners multiplied: ")
//    println(Image(readFileLineByLineToText("2020_20REAL.txt")).corners.multiplyLong { it.tileId.toLong() })
    Image(readFileLineByLineToText("2020_20.txt"))

    printDay(2)
    print("Valid string count: ")
//    println(readFileLineByLineToText("2020_19.txt").toList())
}

private class Image(input: Stream<String>) {
    val tiles = input.toImageTiles()
    val imageBorders = tiles.flatMap { it.getBorders() }.groupBy { it }.filter { it.value.size == 1 }.keys
    val corners = tiles.filter { it.getBorders().intersect(imageBorders).size == 2 }
    val imageSize = sqrt(tiles.size.toDouble()).toInt() * (corners[0].bytes[0].length - 2)
    val image = Array(imageSize) {Array(imageSize) {' '}}

    init {
        val remainingTiles = tiles.toMutableList()

        var remainingBorder = tiles.flatMap { it.getBorders() }.groupBy { it }.filter { it.value.size == 1 }.keys.toList()
        var cornerTiles = remainingTiles.filter { it.getBorders().intersect(remainingBorder).size == 2 }.toMutableList()
        var topLeftCorner = cornerTiles.removeAt(0)
        var topRightCorner = cornerTiles.minBy { topLeftCorner.getPathToPassingBy(remainingTiles, it, remainingBorder).size }!!
        var path = topLeftCorner.getPathToPassingBy(remainingTiles, topRightCorner, remainingBorder)
        println(path)
        remainingTiles.removeAll(path)

        while (remainingTiles.isNotEmpty()) {
            val outsideTiles = remainingTiles.filter { it.getBorders().intersect(remainingBorder).size == 2 }.toMutableList()
            topLeftCorner = remainingTiles.first { it.getBorders().intersect(path.first().getBorders()).size == 1 }
            topRightCorner = remainingTiles.first { it.getBorders().intersect(path.last().getBorders()).size == 1 }
            remainingBorder = path.flatMap { it.getBorders() }
            path = topLeftCorner.getPathToPassingBy(remainingTiles, topRightCorner, remainingBorder)
            println(path)

            remainingTiles.removeAll(path)

        }
    }

    fun printImage() {
        image.forEach { println(String(it.toCharArray())) }
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

        return listOf(this).plus(possibleTiles.filter { it.getBorders().intersect(borders).isNotEmpty() }.filter { it.isNeighbour(this) }.minBy { it.getPathToPassingBy(possibleTiles.minus(this), destination, borders).size }!!)
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