package days

import util.readInts
import util.splitByBlankLine

object Day05 : Day<Long, List<String>> {
    override val number: Int = 5
    override val expectedPart1Test: Long = 143L
    override val expectedPart2Test: Long = 123L
    override var useTestData = true
    override val debug = false

    override fun part1(input: List<String>): Long {
        val (rulesList, pageLists) = input.splitByBlankLine()
        val rules = rulesList.map {
            it.readInts()
        }
        val answer = pageLists
            .map { pageList ->
                applyRulesAndGetMiddle(pageList, rules) // give me a list of indices and the middle index as a pair
            }
            .mapNotNull { (middle, result) ->
                if (resultOk(result)) middle else null
            }
            .sum()
        return answer.toLong()
    }

    private fun resultOk(result: List<List<Int>>): Boolean = result.all { it[0] < it[1] }

    private fun applyRulesAndGetMiddle(pageList: String, rules: List<List<Int>>): Pair<Int, List<List<Int>>> {
        val (pages, results) = applyRulesPages(pageList, rules)
        return pages.middleElement() to results
    }

    override fun part2(input: List<String>): Long {
        val (rulesList, pageLists) = input.splitByBlankLine()
        val rules = rulesList.map { it.readInts() }
        val answer = pageLists
            .map { pageList ->
                applyRulesPages(pageList, rules) // give me the list of indices and the pages
            }.filter { (_, result) -> !resultOk(result) }
            .map { (pages, result) -> fixPages(pages, result) }
            .sumOf { it.middleElement() }
        return answer.toLong()
    }

    private fun applyRulesPages(pageList: String, rules: List<List<Int>>): Pair<List<Int>, List<List<Int>>> {
        val pages = pageList.readInts()
        val indices = rules
            .mapNotNull { rule ->
                val ii = listOf(pages.indexOf(rule[0]), pages.indexOf(rule[1]))
                if (ii[0] == -1 || ii[1] == -1) null else ii
            }
        return pages to indices
    }

    private fun fixPages(pages: List<Int>, results: List<List<Int>>): List<Int> {
        val violations = results.filter { it[0] > it[1] }
        val fixedPages = violations.fold(pages) { p, fix ->
            val wrong = p.elementAt(fix[0])
            p.toMutableList().apply {
                this -= wrong
                add(fix[1], wrong)
            }
        }
        return fixedPages

    }
    // 5305 too low

    private fun List<Int>.middleElement() = this[size / 2]
}