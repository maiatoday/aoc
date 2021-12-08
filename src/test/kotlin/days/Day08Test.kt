package days

import Day08
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Test

import readInput

class Day08Test {
    private val dayName = "Day08"
    private val day = Day08
    private val dayTestAnswer1 = 26
    private val dayTestAnswer2 = 61229
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