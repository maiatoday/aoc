package days

import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Test
import readInput

class DayTest {
    private val day = Day13
    private val testInput = readInput(day.number,"Day_test")
//    private val testInput = readInput(day.number,"Day")
    @Test
    fun part1() {
        MatcherAssert.assertThat(day.part1(testInput), CoreMatchers.`is`(day.expectedPart1Test))
    }

    @Test
    fun part2() {
        MatcherAssert.assertThat(day.part2(testInput), CoreMatchers.`is`(day.expectedPart2Test))
    }
}