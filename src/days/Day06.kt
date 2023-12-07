package days

import util.readInts

object Day06 : Day<Long, List<String>> {
    override val number: Int = 6
    override val expectedPart1Test: Long = -1L
    override val expectedPart2Test: Long = -1L
    override var useTestData = true
    override val debug = false

    override fun part1(input: List<String>): Long {
        val numbers = input.map { it.readInts() }
        val races = numbers[0] zip numbers[1]
            

        return -1L
    }

    override fun part2(input: List<String>): Long {
        return -1L
    }

}
