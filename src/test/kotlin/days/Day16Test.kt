package days

import Day16
import Day1X
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Test

import readInput

class Day16Test {
    private val dayName = "Day16"
    private val day = Day16
    private val dayTestAnswer1 = 20L
    private val dayTestAnswer2 = 1L
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