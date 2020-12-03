package util

import java.io.File
import java.util.stream.IntStream
import java.util.stream.LongStream
import java.util.stream.Stream

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