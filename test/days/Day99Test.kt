package days

import util.readInput
import kotlin.test.Test
import kotlin.test.assertEquals

class Day99Test {
    private val dayNumber = 99
    private val day = Day99
    private val dayTestAnswer1 = -1L
    private val dayTestAnswer2 = -1L
    private val testInput = readInput(dayNumber, "Day_test")

    @Test
    fun part1() {
        assertEquals(dayTestAnswer1, day.part1(testInput))
    }

    @Test
    fun part2() {
        assertEquals(dayTestAnswer2, day.part2(testInput))
    }
}