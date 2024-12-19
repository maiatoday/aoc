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
        val answer = tidyTowels(towels, patterns).count { it.isNotEmpty() }
        return answer.toLong()
    }

    private fun tidyTowels(towels: List<String>, patterns: List<String>): List<List<String>> =
        patterns
            .mapNotNull {
                println("matching $it with brandguide ${brandGuide.size}")
                makeMatch(it, towels)
            }

    val brandGuide = mutableMapOf<String, List<String>>()

    private fun makeMatch(pattern: String, towels: List<String>): List<String>? {
        val answer = brandGuide[pattern]
        if (answer != null) return answer
        if (pattern.isEmpty()) return emptyList()
        val startTowels = towels.filter { pattern.startsWith(it) }
        var result: List<String>? = null
        for (ss in startTowels) {
            val nextPattern = pattern.substringAfter(ss)
            val nextResult = makeMatch(nextPattern, towels)
            if (nextResult != null) {
                brandGuide[nextPattern] = nextResult
                result = listOf(ss) + nextResult
            }
        }
        return result
    }

    private fun readOnsenBranding(input: List<String>): Pair<List<String>, List<String>> =
        input.splitByBlankLine().let {
            it[0].first().split(", ") to it[1]
        }


    override fun part2(input: List<String>): Long {
        return -1L
    }
}

