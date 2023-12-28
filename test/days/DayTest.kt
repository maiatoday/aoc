package days

import util.readInput
import util.readInputString
import kotlin.test.Test
import kotlin.test.assertEquals

class DayTest {
    private val dayNumber = 13
    private val day = Day13
    // private val testInput = readInput(dayNumber, "Day_test")

    private val fileString = if (day.useTestData) "Day_test" else "Day"
    private val input = readInput(day.number, fileString)

    //private val testInput = readInputString(day.number, "Day_test")
    //    private val puzzleInput = readInputString(day.number,"Day")

    @Test
    fun part1() {
        assertEquals(day.expectedPart1Test, day.part1(input))
    }

    @Test
    fun part2() {
        assertEquals(day.expectedPart2Test, day.part2(input))
    }
}