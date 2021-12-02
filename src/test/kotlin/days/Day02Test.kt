package days

import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

import readInput

class Day02Test {

    private val testInput = readInput("Day02_test")
    @Test
    fun part1() {
        assertThat(Day02.part1(testInput), CoreMatchers.`is`(150))
    }

    @Test
    fun part2() {
        assertThat(Day02.part2(testInput), CoreMatchers.`is`(900))
    }
}