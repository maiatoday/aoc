package days

import Day15
import Day1X
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Test

import readInput

class Day15Test {
    private val dayName = "Day15"
    private val day = Day15
    private val dayTestAnswer1 = 40L
    private val dayTestAnswer2 = 315L
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