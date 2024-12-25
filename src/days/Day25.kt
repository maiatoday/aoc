package days

import util.Point
import util.listFromGrid
import util.splitByBlankLine

object Day25 : Day<Long, List<String>> {
    override val number: Int = 25
    override val expectedPart1Test: Long = 3L
    override val expectedPart2Test: Long = -1L
    override var useTestData = true
    override val debug = false

    override fun part1(input: List<String>): Long {
        val (keys, locks) = parseKeysLocks(input)
        var countMatch = 0
        for (lock in locks) {
            val matches = keys.filter { key -> key.any { it in lock } && lock.all { it in key} }
            countMatch += matches.size
        }

        return countMatch.toLong()
    }

    fun parseKeysLocks(input: List<String>): Pair<List<List<Point>>, List<List<Point>>> {
        val blocks = input.splitByBlankLine()
        val keys = blocks.filter { it.first().all { it == '.' } }.map { it.subList(1, it.size - 1) }
            .map { it.listFromGrid(".") }
        val locks = blocks.filter { it.first().all { it == '#' } }.map { it.subList(1, it.size - 1) }
            .map { it.listFromGrid("#") }
        return (keys to locks)
    }

    override fun part2(input: List<String>): Long {
        return -1L
    }
}