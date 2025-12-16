package days

import utils.Direction
import utils.Point2D
import utils.execFileByLine
import utils.plus
import kotlin.math.abs

class Day9 {

    fun solve() {
        val rope = MutableList(10) { Point2D(0, 0) }
        val visitedPart1 = mutableSetOf(rope[1])
        val visitedPart2 = mutableSetOf(rope[9])

        execFileByLine(9) { line ->
            val l = line.split(" ")
            val dir = l.first().toCharArray().first()
            val amount = l.last().toInt()
            repeat(amount) {
                rope[0] += when (dir) {
                    'U' -> Direction.N
                    'D' -> Direction.S
                    'L' -> Direction.W
                    'R' -> Direction.E
                    else -> throw IllegalArgumentException("Unknown direction: $dir")
                }

                for (i in 1 until rope.size) {
                    rope[i] = updateTail(rope[i - 1], rope[i])
                }

                visitedPart1.add(rope[1])
                visitedPart2.add(rope[9])
            }
        }
        println(visitedPart1.size)
        println(visitedPart2.size)
    }

    private fun updateTail(head: Point2D, tail: Point2D): Point2D {
        val dx = head.x - tail.x
        val dy = head.y - tail.y
        return if (abs(dx) > 1 || abs(dy) > 1) {
            val moveX = if (dx == 0) 0 else dx / abs(dx)
            val moveY = if (dy == 0) 0 else dy / abs(dy)
            tail + Point2D(moveX, moveY)
        } else tail
    }
}