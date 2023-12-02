package days

import util.readInput
import kotlin.test.Test
import kotlin.test.assertEquals

class Day02Test {
    private val dayNumber = 2
    private val day = Day02
    private val testInput = readInput(dayNumber, "Day_test")

    @Test
    fun part1() {
        assertEquals(day.expectedPart1Test, day.part1(testInput))
    }

    @Test
    fun part2() {
        assertEquals(day.expectedPart2Test, day.part2(testInput))
    }
}