package days

import util.readInput
import util.readInputString
import kotlin.test.Test
import kotlin.test.assertEquals

class DayTest {
    private val dayNumber = 14
    private val day = Day14
    // private val testInput = readInput(dayNumber, "Day_test")

    private val testInput = readInput(day.number, "Day_test")
//        private val puzzleInput = readInput(day.number, "Day")

    //private val testInput = readInputString(day.number, "Day_test")
    //    private val puzzleInput = readInputString(day.number,"Day")

    @Test
    fun part1() {
        assertEquals(day.expectedPart1Test, day.part1(testInput))
    }

    @Test
    fun part2() {
        assertEquals(day.expectedPart2Test, day.part2(testInput))
    }
}