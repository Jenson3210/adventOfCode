package `2019`.`08`

import util.readFileLineToIntStream
import kotlin.streams.toList


fun main() {
    val image = Image(readFileLineToIntStream("2019_08.txt").toList(), 25, 6)
    val layer = image.layers.stream().sorted { o1, o2 -> o1.countNumber(0).compareTo(o2.countNumber(0)) }.findFirst().orElseThrow()
    println(layer.countNumber(1) * layer.countNumber(2))
    println(image.print())
}

private class Image(val layers: MutableList<Layer> = mutableListOf()) {
    companion object {
        operator fun invoke(colors: List<Int>, width: Int, height: Int) : Image {
            val layers: MutableList<Layer> = mutableListOf()
            var index = 0
            while (colors.lastIndex >= index * (width * height)) {
                layers.add(Layer(colors.subList(index * (width * height), index * (width * height) + (width * height)), width, height))
                index ++
            }
            return Image(layers)
        }
    }

    fun print(): String {
        var toReturn: StringBuilder = StringBuilder()
        var done = false
        for (index in layers[0].toString().indices) {
            done = false
            for (layer in layers) {
                if (!done) {
                    when (layer.toString()[index]) {
                        '0' -> {
                            toReturn.append(" "); done = true
                        }
                        '1' -> {
                            toReturn.append("X"); done = true
                        }
                        '\n' -> {
                            toReturn.append("\n"); done = true
                        }
                        else -> {
                            //println("unknown character " + layer.toString()[index])
                        }
                    }
                }
            }
        }
        return toReturn.toString()
    }
}
private class Layer(val rows: List<Row> = listOf()) {
    companion object {
        operator fun invoke(colors: List<Int>, width: Int, height: Int) : Layer {
            val rows: MutableList<Row> = mutableListOf()
            var index = 0
            for (h in 1..height) {
                rows.add(Row(colors.subList(index, index + width)))
                index += width
            }
            return Layer(rows)
        }
    }

    fun countNumber(number: Int) = rows.map { it.countNumber(number) }.sum()
    override fun toString()  = rows.joinToString { it.toString() }
}
private class Row(val columns: List<Pixel> = mutableListOf()){
    companion object {
        operator fun invoke(colors: List<Int>) = Row(colors.map { Pixel(it) })
    }

    fun countNumber(number: Int) = columns.map { it.color }.filter { it == number }.count()
    override fun toString()  = columns.joinToString { it.color.toString() } + "\n"
}
private class Pixel(val color: Int)