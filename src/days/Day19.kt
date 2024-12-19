package days

import util.splitByBlankLine

object Day19 : Day<Long, List<String>> {
    override val number: Int = 19
    override val expectedPart1Test: Long = 6L
    override val expectedPart2Test: Long = -1L
    override var useTestData = true
    override val debug = false

    override fun part1(input: List<String>): Long {
        val (towels, patterns) = readOnsenBranding(input)
        val answer = tidyTowelsCount(towels, patterns)
        return answer.toLong()
    }

    private fun tidyTowelsCount(towels: List<String>, patterns: List<String>): Int =
        patterns.filter { makeMatch(it, towels) }.size

    val brandGuide = mutableMapOf<String, Boolean>()

    private fun makeMatch(pattern: String, towels: List<String>): Boolean {
        val answer = brandGuide[pattern]
        if (answer != null) return answer
        if (pattern.isEmpty()) return true
        val startTowels = towels.filter { pattern.startsWith(it) }
        return  startTowels.map { makeMatch(pattern.substringAfter(it), towels) }.any { it }
    }

    private fun readOnsenBranding(input: List<String>): Pair<List<String>, List<String>> =
        input.splitByBlankLine().let {
            it[0].first().split(", ") to it[1]
        }


    override fun part2(input: List<String>): Long {
        return -1L
    }
}

