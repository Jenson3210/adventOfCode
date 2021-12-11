package utils

enum class PrintColor(val color: String) {
    RED("\u001b[31m"),
    BLACK("\u001b[30m"),
    GREEN("\u001b[32m"),
    YELLOW("\u001b[33m"),
    BLUE("\u001b[34m"),
    MAGENTA("\u001b[35m"),
    CYAN("\u001b[36m"),
    WHITE("\u001b[37m"),
    RESET("\u001b[0m"),
}

fun print(message: String, color: PrintColor) {
    print(color.color + message + PrintColor.RESET.color)
}

fun printDay(day: Int) {
    println()
    println("************************************")
    println("* $day                                *")
    println("************************************")
}