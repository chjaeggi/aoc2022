package days

import utils.*
import utils.Direction.*

class Day8 {

    val forest =
        Array(numberOfLinesPerFile(8)) { IntArray(numberOfCharsPerLine(8)) }

    val trees = mutableSetOf<Point2D>()
    fun solve() {
        execFileByLineIndexed(8) { y, line ->
            line.forEachIndexed { x, v ->
                forest[y][x] = v.digitToInt()
                trees.add(Point2D(x, y))
            }
        }
        // Part 1: Count visible trees
        val visibleTrees = trees.count { tree ->
            Direction.entries.any { dir ->
                val distanceToEdge = when (dir) {
                    N -> tree.y
                    S -> forest.size - 1 - tree.y
                    W -> tree.x
                    E -> forest[0].size - 1 - tree.x
                    else -> {}
                }
                tree.viewingDistance(dir) == distanceToEdge
            }
        }
        println(visibleTrees)

        val maxScenicScore = trees.maxOf { tree ->
            listOf(N, E, S, W).fold(1L) { acc, dir ->
                acc * tree.viewingDistance(dir, true)
            }
        }
        println(maxScenicScore)
    }

    private fun Point2D.viewingDistance(dir: Direction, includeEnd: Boolean = false): Int {
        var view = 0
        var next = this + dir
        while (forest.atOrNull(next) != null) {
            if (forest.at(next) < forest.at(this)) {
                view++
                next += dir
            } else {
                return if (includeEnd) view + 1 else view
            }
        }
        return view
    }
}