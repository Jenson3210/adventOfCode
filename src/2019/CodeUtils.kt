package `2019`

fun codeGenerator(options: String, length: Int) : List<String>{
    if (length == 1) {
        return options.toCharArray().map { it.toString() }.toList()
    }
    val opts: MutableList<String> = mutableListOf()
    for (combination in codeGenerator(options, length - 1)) {
        for (char in options.toCharArray()) {
            opts.add(combination + char)
        }
    }
    return opts;
}

fun codeGeneratorSingularUsage(options: String, length: Int) : List<String>{
    if (length == 1) {
        return options.toCharArray().map { it.toString() }.toList()
    }
    val opts: MutableList<String> = mutableListOf()
    for (char in options.toCharArray()) {
        for (combination in codeGeneratorSingularUsage(
            options.replace(char.toString(), ""),
            length - 1
        )) {
            opts.add(combination + char)
        }
    }
    return opts;
}