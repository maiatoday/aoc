package days

import kotlin.math.abs

object Day01 : Day<Long, List<String>> {
    override val number: Int = 1
    override val expectedPart1Test: Long = 11L
    override val expectedPart2Test: Long = 31L
    override var useTestData = true
    override val debug = false

    override fun part1(input: List<String>): Long {
        val pairs = parse(input)
        val newPairs = parse(input).map { it[0] }.sorted() zip pairs.map { it[1] }.sorted()
        return newPairs.sumOf { abs(it.first - it.second) }.toLong()
    }

    override fun part2(input: List<String>): Long {
        val lists = parse(input).map { it[0] to it[1] }.unzip()
        val diffs = lists.first.map { f ->
            f to lists.second.count { i -> i == f }
        }
        return diffs.sumOf { it.first * it.second }.toLong()
    }

    private fun parse(input: List<String>): List<List<Int>> = input.map { s ->
        s.split("   ").map { it.toInt() }
    }

}