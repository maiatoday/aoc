package days

import Day01
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Test

import readInput

class Day01Test {
    private val dayNumber = 1
    private val day = Day01
    private val dayTestAnswer1 = 24000L
    private val dayTestAnswer2 = 45000L
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