package days

import Day22
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Test
import readInput

class Day22Test {
    private val dayName = "Day22"
    private val day = Day22
    private val dayTestAnswer1 = 474140L
    private val dayTestAnswer2 = 2758514936282235L
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