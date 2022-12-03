package days

import Day03
import Day99
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Test

import readInput

class Day03Test {
    private val dayNumber = 3
    private val day = Day03
    private val dayTestAnswer1 = 157
    private val dayTestAnswer2 = 70
    private val testInput = readInput(dayNumber, "Day_test")

    @Test
    fun part1() {
        MatcherAssert.assertThat(day.part1(testInput), CoreMatchers.`is`(dayTestAnswer1))
    }

    @Test
    fun part2() {
        MatcherAssert.assertThat(day.part2(testInput), CoreMatchers.`is`(dayTestAnswer2))
    }
}