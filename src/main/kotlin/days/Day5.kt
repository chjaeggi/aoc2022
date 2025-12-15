package jaeggich.aoc2022.days

import utils.execFileByLine

class Day5 {

    private enum class CrateMover {
        CM_9000, CM_9001
    }

    private data class Move(val count: Int, val from: Int, val to: Int)

    private fun parseInput(): Pair<MutableMap<Int, ArrayDeque<Char>>, List<Move>> {
        val crates = mutableMapOf<Int, ArrayDeque<Char>>()
        val moves = mutableListOf<Move>()
        val moveRegex = """move (\d+) from (\d+) to (\d+)""".toRegex()

        execFileByLine(5) { line ->
            if (line.isEmpty()) {
                return@execFileByLine
            }

            if (line.contains("[")) {
                line.chunked(4).forEachIndexed { index, chunk ->
                    val crateChar = chunk.trim().removePrefix("[").removeSuffix("]").getOrNull(0)
                    if (crateChar != null) {
                        crates.getOrPut(index + 1) { ArrayDeque() }.addFirst(crateChar)
                    }
                }
            } else {
                moveRegex.find(line)?.let {
                    val (count, from, to) = it.destructured
                    moves.add(Move(count.toInt(), from.toInt(), to.toInt()))
                }
            }
        }
        return crates to moves
    }

    private fun solve(crateMover: CrateMover) {
        val (crates, moves) = parseInput()

        for ((count, from, to) in moves) {
            val fromStack = crates[from]!!
            val toStack = crates[to]!!
            val itemsToMove = fromStack.takeLast(count)
            repeat(count) { fromStack.removeLast() }

            when (crateMover) {
                CrateMover.CM_9000 -> toStack.addAll(itemsToMove.reversed())
                CrateMover.CM_9001 -> toStack.addAll(itemsToMove)
            }
        }
        println(crates.values.mapNotNull { it.lastOrNull() }.joinToString(""))
    }

    fun solve() {
        solve(CrateMover.CM_9000)
        solve(CrateMover.CM_9001)
    }
}