package days

import Day03
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Test

import org.junit.Assert.*
import readInput

class Day03Test {

    private val dayName = "Day03"
    private val day = Day03
    private val dayTestAnswer1 = 198
    private val dayTestAnswer2 = 230
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