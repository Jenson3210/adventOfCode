package util

import java.io.File
import java.util.stream.IntStream
import java.util.stream.LongStream
import java.util.stream.Stream
import kotlin.streams.toList

fun readFile(fileName: String): File {
    return File(ClassLoader.getSystemResource(fileName).file)
}

fun readFileToText(fileName: String): String {
    return File(ClassLoader.getSystemResource(fileName).file).readText(Charsets.UTF_8)
}

fun readFileLineByLineToText(fileName: String): Stream<String> {
    return File(ClassLoader.getSystemResource(fileName).file).readLines(Charsets.UTF_8).stream()
}

fun readFileLineByLineToInt(fileName: String): IntStream {
    return readFileLineByLineToText(fileName).mapToInt { it.toInt() }
}

fun readFileLineCsvToInt(fileName: String): IntStream{
    return readFileLineCsvToText(fileName).mapToInt { it.toInt() }
}

fun readFileLineCsvToLong(fileName: String): LongStream {
    return readFileLineCsvToText(fileName).mapToLong { it.toLong() }
}

fun readFileLineToIntStream(fileName: String): IntStream{
    return readFileLineToText(fileName).mapToInt { it.toInt() }
}

fun readFileLineCsvToText(fileName: String): Stream<String>{
    return readFileToText(fileName).split(",").stream()
}

fun readFileLineToText(fileName: String): Stream<String>{
    return readFileToText(fileName).toCharArray().map { it.toString() }.stream()
}

fun readNthCharsOfEquallyLongLines(fileName: String): List<List<Char>> {
    val file = readFileLineByLineToText(fileName).toList()
    if (file.isEmpty()) return emptyList()
    return (0 until file[0].length).map { ix -> file.map { it[ix] } }
}