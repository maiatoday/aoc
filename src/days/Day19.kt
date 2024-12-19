package days

import util.splitByBlankLine

object Day19 : Day<Long, List<String>> {
    override val number: Int = 19
    override val expectedPart1Test: Long = 6L
    override val expectedPart2Test: Long = -1L
    override var useTestData = true
    override val debug = false
    var  towels: List<String> = emptyList()

    override fun part1(input: List<String>): Long {
        val (towels, patterns) = readOnsenBranding(input)
        this.towels = towels
        brandGuide.clear()
        val answer = tidyTowels(patterns).count { it.isNotEmpty() }
        return answer.toLong()
    }

    private fun tidyTowels(patterns: List<String>): List<List<String>> =
        patterns
            .mapNotNull {
                println("matching $it with brandguide ${brandGuide.size}")
                makeMatch(it)
            }

    val brandGuide = mutableMapOf<String, List<String>?>()

    private fun makeMatch(pattern: String): List<String>? {
        if (pattern in brandGuide) return brandGuide[pattern]
        if (pattern.isEmpty()) return emptyList()
        val startTowels = towels.filter { pattern.startsWith(it) }
        var result: List<String>? = null
        for (ss in startTowels) {
            val nextPattern = pattern.drop(ss.length)
            val nextResult = makeMatch(nextPattern)
            brandGuide[nextPattern] = nextResult
            if (nextResult != null) {
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

