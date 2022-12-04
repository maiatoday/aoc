package days

import Day02
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Test

import readInput

class Day02Test {
    private val dayNumber = 2
    private val day = Day02
    private val dayTestAnswer1 = 15L
    private val dayTestAnswer2 = 12L
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