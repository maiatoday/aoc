package days

import util.Point
import util.readInts
import kotlin.math.abs

object Day09 : Day<Long, List<String>> {
    override val number: Int = 9
    override val expectedPart1Test: Long = 50L
    override val expectedPart2Test: Long = -1L
    override var useTestData = true
    override val debug = false

    override fun part1(input: List<String>): Long {
        val points = input.map { it.readInts() }.map { (x, y) -> Point(x, y) }
        val sizes = getAllAreaSizes(points)
        return sizes.maxOf { it.second }
    }

    private fun getAllAreaSizes(points: List<Point>) = combos(points).map { it to it.getSize() }

    override fun part2(input: List<String>): Long {
        return -1L
    }

    fun combos(list: List<Point>): List<Pair<Point, Point>> =
        list.withIndex().flatMap { (i1, j1) ->
            list.withIndex().filter { (i2, _) -> i1 != i2 }
                .map { (_, j2) -> j1 to j2 }

        }

    private fun Pair<Point, Point>.getSize(): Long =
        (abs(first.x - second.x) + 1L) * (abs(first.y - second.y) + 1L)
}


