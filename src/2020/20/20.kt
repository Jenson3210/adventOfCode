package `2020`.`20`

import util.printDay
import util.readFileLineByLineToText
import java.util.stream.Stream
import kotlin.math.sqrt

fun main() {
    printDay(1)
    print("Corners multiplied: ")
//    println(Image(readFileLineByLineToText("2020_20REAL.txt")).corners.multiplyLong { it.tileId.toLong() })
    Image(readFileLineByLineToText("2020_20.txt")).printImage()

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
        val notPlacedTiles = this.tiles.toMutableList()
        var tempImageBorders = this.imageBorders
        var outsideTiles: MutableList<ImageTile>
        var cornerTile: ImageTile
        var path: MutableList<ImageTile>
        val cornerBorders: MutableList<String> = mutableListOf()
        cornerBorders.addAll(corners[0].getBorders().intersect(imageBorders))
        val directionBorder: MutableList<String> = mutableListOf()
        directionBorder.addAll(notPlacedTiles.first { it.getBorders().any { cornerBorders.contains(it) } }.getBorders())
        while (notPlacedTiles.isNotEmpty()) {
            outsideTiles = notPlacedTiles.filter { it.getBorders().any { tempImageBorders.contains(it) } }.toMutableList()
            cornerTile = outsideTiles.first { it.getBorders().intersect(cornerBorders).isNotEmpty() }
            path = mutableListOf(cornerTile)
            path.addAll(cornerTile.getChain(outsideTiles.filter { it.tileId != cornerTile.tileId }, cornerTile, directionBorder))
            directionBorder.addAll(path[1].getBorders())

            for (tile in path) {
                tile.getTile(cornerBorders)
            }
            cornerBorders.addAll(path.first().getBorders())
            cornerBorders.addAll(path.last().getBorders())

            notPlacedTiles.removeIf { path.contains(it) }
            tempImageBorders = notPlacedTiles.flatMap { it.getBorders() }.groupBy { it }.filter { it.value.size == 1 }.keys
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

    fun getChain(toChain: List<ImageTile>, start: ImageTile, nextBorders: List<String>? = null) : List<ImageTile> {
        if (toChain.size == 1) {
            if (isNeighbour(toChain[0])) {
                return listOf(toChain[0])
            }
            return emptyList()
        }
        val chain =
            if (nextBorders != null) {
                mutableListOf(toChain.filter { nextBorders.intersect(it.getBorders()).isNotEmpty() }.first { start.isNeighbour(it) })
            } else {
                mutableListOf(toChain.first { start.isNeighbour(it) })
            }
        chain.addAll(getChain(toChain.filter { it.tileId != chain[0].tileId }, chain[0]))
        return chain
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