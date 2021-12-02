package days

import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Test

import org.junit.Assert.*
import readInput

class Day03Test {
    private val testInput = readInput("Day02_test")
    @Test
    fun part1() {
        MatcherAssert.assertThat(Day01.part1(testInput), CoreMatchers.`is`(7))
    }

    @Test
    fun part2() {
        MatcherAssert.assertThat(Day01.part2(testInput), CoreMatchers.`is`(5))
    }
}