package y2022

import utils.printDay
import utils.readFileLineByLineToText
import utils.sumByLong
import java.util.stream.Stream

fun main() {
    val fileSystem = getFileSystem(readFileLineByLineToText(2022, 7));
    printDay(1)
    println(fileSystem.flatten().map { it.calculateSize() }.filter { it <= 100000 }.sumByLong { it })

    printDay(2)
    val requiredDeletionSpace = 30000000 - (70000000 - fileSystem.calculateSize());
    println(fileSystem.flatten().filter { it.calculateSize() > requiredDeletionSpace }.minBy { it.calculateSize() })
}

private fun getFileSystem(input: Stream<String>): Directory {
    var currentDirectory = Directory("")
    input.forEach {
        if (it.isCommand()) {
            if (it.getCommand().startsWith("cd")) {
                currentDirectory = currentDirectory.cd(it.getCommand().substringAfter(" "))
            } else if (it.getCommand() == "ls") { }
            else {
                throw Exception()
            }
        } else {
            currentDirectory.add(it.toFileSystemEntry())
        }
    }

    return currentDirectory.cd("/")
}

private fun String.isCommand(): Boolean {
    return this.startsWith("$ ")
}

private fun String.getCommand(): String {
    return this.substringAfter("$ ")
}

private fun String.toFileSystemEntry(): FileSystemEntry {
    if (this.startsWith("dir ")) {
        return Directory(this.substringAfter("dir "))
    }

    return File(this.substringAfter(" "), this.substringBefore(" ").toLong())
}

private abstract class FileSystemEntry(val name: String, var parent: Directory? = null) {
    override fun toString(): String {
        return name
    }
}

private class Directory(name: String): FileSystemEntry(name) {
    var entries: MutableList<FileSystemEntry> = mutableListOf();

    fun calculateSize(): Long {
        return entries.fold(0) {acc, fileSystemEntry -> acc + if (fileSystemEntry is Directory) fileSystemEntry.calculateSize() else if (fileSystemEntry is File) fileSystemEntry.size else 0 }
    }

    fun flatten(): List<Directory> {
        val list = mutableListOf(this)
        entries.filterIsInstance<Directory>()
            .flatMap { it.flatten() }
            .forEach(list::add)
        return list
    }

    fun cd(name: String): Directory {
        return if (name == "/") {
            if (parent != null) return parent!!.cd(name)
            return this
        } else if (name == "..") {
            if (parent != null) return parent as Directory
            return this
        } else entries.filterIsInstance<Directory>().first { it.name == name }
    }

    fun add(entry: FileSystemEntry) {
        entry.parent = this
        entries.add(entry)
    }

    override fun toString(): String {
        return name + "(" + calculateSize() + ")"
    }


}

private class File(name: String, val size: Long): FileSystemEntry(name)
