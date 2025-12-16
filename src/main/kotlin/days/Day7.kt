package days

import days.Command.*
import utils.execFileByLine

data class AocFile(
    val name: String,
    val fileSize: Long,
)

data class Path(
    val current: String,
    val parent: String,
    val children: MutableSet<String> = mutableSetOf(),
    val files: MutableSet<AocFile> = mutableSetOf()
)

sealed interface Command {
    object Ls : Command
    class Cd(val dir: String) : Command
    class OutputFile(val name: String, val size: Long) : Command
    class OutputDir(val name: String) : Command
}

class Day7 {
    private val navigation: MutableMap<String, Path> = mutableMapOf()
    private var currentPath: String = ""

    fun solve() {
        navigation["/"] = Path("/", "") // Initialize root directory
        execFileByLine(7) { line ->
            when (val cmd = parseCommand(line)) {
                is Cd -> {
                    if (cmd.dir == "..") {
                        currentPath = navigation[currentPath]!!.parent
                    } else if (cmd.dir == "/") {
                        currentPath = "/"
                    } else {
                        val newPath = if (currentPath == "/") "/${cmd.dir}" else "$currentPath/${cmd.dir}"
                        navigation.getOrPut(newPath) { Path(newPath, currentPath) }
                        currentPath = newPath
                    }
                }

                is Ls -> {
                    // no-op
                }

                is OutputDir -> {
                    val childPath = if (currentPath == "/") "/${cmd.name}" else "$currentPath/${cmd.name}"
                    navigation[currentPath]!!.children.add(childPath)
                    navigation.getOrPut(childPath) { Path(childPath, currentPath) }
                }

                is OutputFile -> {
                    navigation[currentPath]!!.files.add(AocFile(cmd.name, cmd.size))
                }
            }
        }
        val smallDirs = navigation.map { (key, _) ->
            val total = getDirSize(key)
            if (total < 100_000L) {
                total
            } else {
                0
            }
        }.sum()
        println(smallDirs)

        val totalUsed = getDirSize("/")
        val freeSpace = 70_000_000L - totalUsed
        val neededSpace = 30_000_000L - freeSpace

        val dirToDelete = navigation.keys
            .map { getDirSize(it) }
            .filter { it >= neededSpace }
            .minOrNull()

        println(dirToDelete)
    }

    private fun parseCommand(line: String): Command {
        if (line.startsWith("$")) {
            val parts = line.split(" ")
            if (parts[1] == "cd") {
                val file = line.split(" ").last()
                return Cd(file)
            } else {
                return Ls
            }
        } else {
            return if (line.contains("dir")) {
                OutputDir(line.split(" ").last())
            } else {
                OutputFile(line.split(" ").last(), line.split(" ").first().toLong())
            }
        }
    }
    
    private fun getDirSize(path: String): Long {
        val dir = navigation[path]!!
        return dir.files.sumOf { it.fileSize } + dir.children.sumOf { getDirSize(it)}
    }
}