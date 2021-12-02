package days

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import readInput

class Day01Test {

    private val testInput = readInput("Day01_test")
    @Test
    fun part1() {
        assertThat(Day01.part1(testInput), `is`(7))
        assertThat(Day01.part1Idiomatic(testInput), `is`(7))
    }

    @Test
    fun part2() {
        assertThat(Day01.part2(testInput), `is`(5))
        assertThat(Day01.part2Idiomatic(testInput), `is`(5))
    }
}