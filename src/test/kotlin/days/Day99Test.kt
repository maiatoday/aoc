package days

import Day99
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Test

import readInput

class Day99Test {
    private val dayNumber = 99
    private val day = Day99
    private val dayTestAnswer1 = -1L
    private val dayTestAnswer2 = -1L
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