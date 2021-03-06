package days

import Day21
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Test
import readInput

class Day21Test {
    private val dayName = "Day21"
    private val day = Day21
    private val dayTestAnswer1 = 739785L
    private val dayTestAnswer2 = 444356092776315L
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