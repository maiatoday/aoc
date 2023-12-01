package days

import util.readInput
import kotlin.test.Test
import kotlin.test.assertEquals

class Day01Test {
    private val dayNumber = 1
    private val day = Day01
    private val dayTestAnswer1 = 209L //142L
    private val dayTestAnswer2 = 281L
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