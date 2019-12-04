package util

import java.io.File
import java.util.stream.IntStream
import java.util.stream.Stream


fun readFileLineByLineToText(fileName: String): Stream<String> {
    return File(ClassLoader.getSystemResource(fileName).file).readLines(Charsets.UTF_8).stream()
}

fun readFileLineByLineToInt(fileName: String): IntStream {
    return readFileLineByLineToText(fileName).mapToInt { it.toInt() }
}

fun readFileLineCsvToInt(fileName: String): IntStream{
    return readFileLineCsvToText(fileName).mapToInt { it.toInt() };
}

fun readFileLineCsvToText(fileName: String): Stream<String>{
    return File(ClassLoader.getSystemResource(fileName).file).readText(Charsets.UTF_8).split(",").stream();
}