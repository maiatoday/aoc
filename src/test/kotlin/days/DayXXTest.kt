package days

import DayXX
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Test

import readInput

class DayXXTest {
    private val dayName = "DayXX"
    private val day = DayXX
    private val dayTestAnswer1 = -1L
    private val dayTestAnswer2 = -1L
    private val testInput = readInput("${dayName}_test")
    @Test
    fun part1() {
        MatcherAssert.assertThat(day.part1(testInput), CoreMatchers.`is`(dayTestAnswer1))
    }

    @Test
    fun part2() {
        MatcherAssert.assertThat(day.part2(testInput), CoreMatchers.`is`(dayTestAnswer2))
    }
}