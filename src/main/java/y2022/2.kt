package y2022

import utils.printDay
import utils.readFileLineByLineToText
import java.util.stream.Stream

fun main() {
    printDay(1)
    println(predictScore(readFileLineByLineToText(2022, 2)))

    printDay(2)
    println(predictResult(readFileLineByLineToText(2022, 2)))
}

fun predictScore(input: Stream<String>): Int {
    return input.map { it.lowercase().split(" ").zipWithNext().first() }
        .mapToInt { it.getRockPaperScissorsScore() }
        .sum()
}

fun predictResult(input: Stream<String>): Int {
    return input.map { it.lowercase().split(" ").zipWithNext().first() }
        .mapToInt { it.getRockPaperScissorsPredictionResult() }
        .sum()
}

private fun Pair<String, String>.getRockPaperScissorsScore(): Int {
    return getRockPaperScissorsOutcome() + second.getRockPaperScissorScore();
}

private fun Pair<String, String>.getRockPaperScissorsPredictionResult(): Int {
    val outcome = second.getRockPaperScissorExpectedScore();
    return outcome + first.getExpectedInput(outcome).getRockPaperScissorScore();
}

private fun Pair<String, String>.getRockPaperScissorsOutcome(): Int {
    val firstMapped = first.getRockPaperScissor()
    val secondMapped = second.getRockPaperScissor()
    if (firstMapped == secondMapped) return 3
    if (firstMapped == "rock" && secondMapped == "paper") return 6
    if (firstMapped == "paper" && secondMapped == "scissor") return 6
    if (firstMapped == "scissor" && secondMapped == "rock") return 6
    return 0;
}

private fun String.getRockPaperScissorExpectedScore(): Int {
    return when(this) {
        "x" -> 0
        "y" -> 3
        "z" -> 6
        else -> throw Exception()
    }
}

private fun String.getRockPaperScissor(): String {
    return when(this) {
        "rock", "a", "x" -> "rock"
        "paper", "b", "y" -> "paper"
        "scissor", "c", "z" -> "scissor"
        else -> throw Exception()
    }
}

private fun String.getExpectedInput(result: Int): String {
    if (result == 3) {
        return this.getRockPaperScissor()
    }
    if (result == 0) {
        if (this.getRockPaperScissor() == "rock") return "scissor"
        if (this.getRockPaperScissor() == "paper") return "rock"
        if (this.getRockPaperScissor() == "scissor") return "paper"
    } else {
        if (this.getRockPaperScissor() == "rock") return "paper"
        if (this.getRockPaperScissor() == "paper") return "scissor"
        if (this.getRockPaperScissor() == "scissor") return "rock"
    }
    throw Exception();
}


private fun String.getRockPaperScissorScore(): Int {
    return when(this.getRockPaperScissor()) {
        "rock" -> 1
        "paper" -> 2
        "scissor" -> 3
        else -> throw Exception()
    }
}

