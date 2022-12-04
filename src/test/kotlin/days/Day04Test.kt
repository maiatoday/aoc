package days

import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Test

import readInput

class Day04Test {
    private val dayNumber = 4
    private val day = Day04
    private val dayTestAnswer1 = 2L
    private val dayTestAnswer2 = 4L
    private val testInput = readInput(dayNumber,"Day_test")
    @Test
    fun part1() {
        MatcherAssert.assertThat(day.part1(testInput), CoreMatchers.`is`(dayTestAnswer1))
    }

    @Test
    fun part2() {
        MatcherAssert.assertThat(day.part2(testInput), CoreMatchers.`is`(dayTestAnswer2))
    }
}