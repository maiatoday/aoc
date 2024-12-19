package days

import util.splitByBlankLine

object Day19 : Day<Long, List<String>> {
    override val number: Int = 19
    override val expectedPart1Test: Long = 6L
    override val expectedPart2Test: Long = 16L
    override var useTestData = true
    override val debug = false
    var towels: Set<String> = emptySet()

    override fun part1(input: List<String>): Long {
        val (towels, patterns) = readOnsenBranding(input)
        this.towels = towels.toSet()
        canMatchCache.clear()
        return patterns.map { canMatch(it) }.count { it == true }.toLong()
    }

    val canMatchCache = mutableMapOf<String, Boolean>()
    private fun canMatch(pattern: String): Boolean =
        canMatchCache.getOrPut(pattern) {
            if (pattern.isEmpty()) true
            else {
                towels.filter { aTowel -> pattern.startsWith(aTowel) }
                    .any { canMatch(pattern.drop(it.length)) }
            }
        }

    private fun readOnsenBranding(input: List<String>): Pair<List<String>, List<String>> =
        input.splitByBlankLine().let {
            it[0].first().split(", ") to it[1]
        }

    override fun part2(input: List<String>): Long {
        val (towels, patterns) = readOnsenBranding(input)
        this.towels = towels.toSet()
        canMatchCache.clear()
        val possiblePatterns = patterns.filter { canMatch(it) }
        matchCountCache.clear()
        return possiblePatterns.sumOf { matchCount(it) }
    }

    val matchCountCache = mutableMapOf<String, Long>()
    fun matchCount(pattern: String): Long =
        matchCountCache.getOrPut(pattern) {
            if (pattern.isEmpty()) 1L
            else {
                towels.filter { aTowel -> pattern.startsWith(aTowel) }
                    .sumOf { matchCount(pattern.drop(it.length)) }
            }
        }

}

