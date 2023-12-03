package days

import util.readInput
import kotlin.test.Test
import kotlin.test.assertEquals

class Day03Test {
    private val dayNumber = 3
    private val day = Day03
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