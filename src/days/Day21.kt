package days

import util.Point
import util.neighbours

object Day21 : Day<Long, List<String>> {
    override val number: Int = 21
    override val expectedPart1Test: Long = 16L
    override val expectedPart2Test: Long = -1L
    override var useTestData = false
    override val debug = false

    val maxSteps = if (useTestData) 6 else 64

    override fun part1(input: List<String>): Long {
        val start = parseMap(input, 'S').first()
        val rocks = parseMap(input, '#')
        val possibleEndPlots = walk(start, rocks, maxSteps)
        return possibleEndPlots.count().toLong()
    }

    private fun walk(start: Point, rocks: List<Point>, maxSteps: Int): Set<Point> =
            (1..maxSteps).fold(setOf(start)) { reached, _ ->
                reached.flatMap { p -> p.neighbours().filter { it !in rocks } }.toSet()
            }

    data class Step(val point: Point, val step: Int) : Comparable<Step> {
        override fun compareTo(other: Step): Int = this.step - other.step
    }


    private fun parseMap(input: List<String>, type: Char): List<Point> = buildList {
        for (y in input.indices) {
            for (x in input.first().indices) {
                if (input[y][x] == type) add(Point(x, y))
            }
        }
    }

    override fun part2(input: List<String>): Long {
        return -1L
    }
}
