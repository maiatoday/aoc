package days

import Day06
import Day0X
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Test

import readInput

//Test template
class Day06Test {
    private val dayName = "Day06"
    private val day = Day06
    private val dayTestAnswer1 = 5934
  //  private val dayTestAnswer2 = 5934L
    private val dayTestAnswer2 = 26984457539L
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