package days

import util.PPoint
import util.Point

object Day14 : Day<Long, List<String>> {
    override val number: Int = 14
    override val expectedPart1Test: Long = 136L
    override val expectedPart2Test: Long = -1L
    override var useTestData = true
    override val debug = false

    override fun part1(input: List<String>): Long {
        val spaces = input.findSpots('.')
        val startingRocks = input.findSpots('O')
        val endingRocks = tilt(
                fieldSize = input.size,
                startingSpaces = spaces,
                startingRocks = startingRocks)
        return -1L
    }

    private fun tilt(fieldSize: Int, startingSpaces: List<PPoint>, startingRocks: List<PPoint>): List<PPoint> =
            (1..<fieldSize).fold(startingSpaces) { spaces, i ->

                spaces
            }


    private fun List<String>.findSpots(spot: Char): List<PPoint> = this.foldIndexed(mutableListOf<PPoint>()) { row, a, s ->
        s.mapIndexed { col, c ->
            if (c == spot) a.add(row to col)
        }
        a
    }

    override fun part2(input: List<String>): Long {
        return -1L
    }
}
