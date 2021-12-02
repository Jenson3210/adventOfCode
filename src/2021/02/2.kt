package `2021`.`02`

import util.printDay
import util.readFileLineByLineToText
import java.util.stream.Stream


fun main() {
    printDay(1)
    println(getFinalPositionPart1(readFileLineByLineToText("2021_02.txt")))

    printDay(2)
    println(getFinalPositionPart2(readFileLineByLineToText("2021_02.txt")))
}

private fun getFinalPositionPart1(directions: Stream<String>): Long {
    var depth: Long = 0
    var horizontal: Long = 0
    directions.forEach {
        depth += it.depthIncrement1()
        horizontal += it.horizontalIncrement1()
    }

    return depth * horizontal
}

private fun getFinalPositionPart2(directions: Stream<String>): Long {
    var depth: Long = 0
    var horizontal: Long = 0
    var aim: Long = 0
    directions.forEach {
        if (it.isAimChange()) {
            aim += it.aimIncrement()
        } else {
            depth += it.getAmount() * aim
            horizontal += it.horizontalIncrement2()
        }
    }

    return depth * horizontal
}

//PART 1
private fun String.depthIncrement1() = if (isDepthChange()) getAmount() * getDirectionMultiplier() else 0
private fun String.horizontalIncrement1() = if (!isDepthChange()) getAmount() else 0
private fun String.isDepthChange() = goesUp() || goesDown()

// PART 2
private fun String.horizontalIncrement2() = getAmount()
private fun String.aimIncrement() = if (isAimChange()) getAmount() * getDirectionMultiplier() else 0
private fun String.isAimChange() = goesUp() || goesDown()

//SHARED
private fun String.getDirectionMultiplier() = if (goesUp()) -1 else 1
private fun String.goesUp() = getDirection().equals("up", true)
private fun String.goesDown() = getDirection().equals("down", true)
private fun String.getDirection() = split(" ")[0]
private fun String.getAmount() = split(" ")[1].toInt()