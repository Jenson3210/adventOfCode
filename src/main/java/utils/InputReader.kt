package utils

import java.io.File
import java.util.stream.IntStream
import java.util.stream.LongStream
import java.util.stream.Stream
import kotlin.streams.toList

fun readFile(year: Int, day:Int): File {
    return File(ClassLoader.getSystemResource(getFileName(year, day)).file)
}

fun readFileToText(year: Int, day:Int): String {
    return File(ClassLoader.getSystemResource(getFileName(year, day)).file).readText(Charsets.UTF_8)
}

fun readFileLineByLineToText(year: Int, day:Int): Stream<String> {
    return File(ClassLoader.getSystemResource(getFileName(year, day)).file).readLines(Charsets.UTF_8).stream()
}

fun readFileLineByLineToInt(year: Int, day:Int): IntStream {
    return readFileLineByLineToText(year, day).mapToInt { it.toInt() }
}

fun readFileLineCsvToInt(year: Int, day:Int): IntStream{
    return readFileLineCsvToText(year, day).mapToInt { it.toInt() }
}

fun readFileLineCsvToLong(year: Int, day:Int): LongStream {
    return readFileLineCsvToText(year, day).mapToLong { it.toLong() }
}

fun readFileLineToIntStream(year: Int, day:Int): IntStream{
    return readFileLineToText(year, day).mapToInt { it.toInt() }
}

fun readFileLineCsvToText(year: Int, day:Int): Stream<String>{
    return readFileToText(year, day).split(",").stream()
}

fun readFileLineToText(year: Int, day:Int): Stream<String>{
    return readFileToText(year, day).toCharArray().map { it.toString() }.stream()
}

fun readNthCharsOfEquallyLongLines(year: Int, day:Int): List<List<Char>> {
    val file = readFileLineByLineToText(year, day).toList()
    if (file.isEmpty()) return emptyList()
    return (0 until file[0].length).map { ix -> file.map { it[ix] } }
}

private fun getFileName(year: Int, day:Int) = "$year/$day.txt"