package days

import utils.execFileByLine

class Day6 {

    fun solve() {
        execFileByLine(6) {
            val commMarkerStart = 4
            val messageMarkerStart = 14
            println(
                it.windowed(4)
                    .indexOfFirst { it.toSet().size == commMarkerStart } + commMarkerStart)
            println(
                it.windowed(14)
                    .indexOfFirst { it.toSet().size == messageMarkerStart } + messageMarkerStart)
        }
    }
}