package days

import utils.execFileByLine

class Day6 {

    fun solve() {
        execFileByLine(6) {
            println(it.findMarker(4))
            println(it.findMarker(14))
        }
    }

    private fun String.findMarker(size: Int): Int =
        windowed(size).indexOfFirst { it.toSet().size == size } + size
}