package days

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.junit.Test
import readInput
import readInputString

class DayTest {
    private val day = Day17
    private val testInput = readInput(day.number,"Day_test")
//    private val testInput = readInput(day.number,"Day")
    private val puzzleInput = readInput(day.number,"Day")

    private val testInputString = readInputString(day.number,"Day_test")
    private val puzzleInputString = readInputString(day.number,"Day")
    @Test
    fun part1() {
        day.useTestData = true
        MatcherAssert.assertThat(day.part1(testInputString), `is`(day.expectedPart1Test))
    }

    @Test
    fun part2() {
        day.useTestData = true
        MatcherAssert.assertThat(day.part2(testInputString), `is`(day.expectedPart2Test))
    }

    @Test
    fun solve1() {
        day.useTestData = false
        val solution1 = day.part1(puzzleInputString)
        println("Day${day.number} Part 1 ⭐️")
//        val wrongValue = 0
//        MatcherAssert.assertThat(day.part2(testInput), not(wrongValue))
//        MatcherAssert.assertThat(day.part2(testInput), `is`(solution1 < wrongValue))
//        MatcherAssert.assertThat(day.part2(testInput), `is`(solution1 > wrongValue))
        println(solution1)
    }

    @Test
    fun solve2() {
        day.useTestData = false
        val solution2 = day.part2(puzzleInputString)
        println("Day${day.number} Part 2 ⭐️⭐️")
//        val wrongValue = 0
//        MatcherAssert.assertThat(day.part2(testInput), not(wrongValue))
//        MatcherAssert.assertThat(day.part2(testInput), `is`(solution2 < wrongValue))
//        MatcherAssert.assertThat(day.part2(testInput), `is`(solution2 > wrongValue))
        println(solution2)
    }
}