package days

import utils.execFileByLine

class Day6 {

    private fun findMarker(input: String, size: Int): Int =
        input.windowed(size).indexOfFirst { it.toSet().size == size } + size

    fun solve() {
        execFileByLine(6) { line ->
            println(findMarker(line, 4))
            println(findMarker(line, 14))
        }
    }
}