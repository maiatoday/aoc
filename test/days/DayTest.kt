package days

import util.readInput
import kotlin.test.Test
import kotlin.test.assertEquals

class DayTest {
    private val dayNumber = 8
    private val day = Day08
    private val testInput = readInput(dayNumber, "Day_test")
    
     //   private val testInput = readInput(day.number,"Day")
    //    private val puzzleInput = readInput(day.number, "Day")

    //    private val testInputString = readInputString(day.number,"Day_test")
    //    private val puzzleInputString = readInputString(day.number,"Day")

    @Test
    fun part1() {
        assertEquals(day.expectedPart1Test, day.part1(testInput))
    }

    @Test
    fun part2() {
        assertEquals(day.expectedPart2Test, day.part2(testInput))
    }
}