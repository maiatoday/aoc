package days

import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Test
import readInput
import readInputString

class DayStringTest {
    private val day = Day05
    private val testInput = readInputString(day.number,"Day_test")
    @Test
    fun part1() {
        MatcherAssert.assertThat(day.part1S(testInput), CoreMatchers.`is`(day.expectedPart1STest))
    }

    @Test
    fun part2() {
        MatcherAssert.assertThat(day.part2S(testInput), CoreMatchers.`is`(day.expectedPart2STest))
    }
}