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
        val (rules, pagesList) = input.splitByBlankLine().map { l -> l.map { it.readInts() } }
        val answer = pagesList
            .map { pages -> applyRulesPages(pages, rules) }// give me the list of indices and the pages
            .filter { (_, result) -> resultOk(result) }
            .sumOf { (pages, _) -> pages.middleElement() }
        return answer.toLong()
    }

    private fun resultOk(result: List<List<Int>>): Boolean = result.all { it[0] < it[1] }

    private fun applyRulesPages(pages: List<Int>, rules: List<List<Int>>): Pair<List<Int>, List<List<Int>>> {
        val indices = rules
            .mapNotNull { rule ->
                val ii = listOf(pages.indexOf(rule[0]), pages.indexOf(rule[1]))
                if (ii[0] == -1 || ii[1] == -1) null else ii
            }
        return pages to indices
    }

    private fun List<Int>.middleElement() = this[size / 2]

    override fun part2(input: List<String>): Long {
        val (rules, pagesList) = input.splitByBlankLine().map { l -> l.map { it.readInts() } }
        val brokenPages = pagesList
            .map { pages -> applyRulesPages(pages, rules) } // give me the list of indices and the pages
            .filter { (_, result) -> !resultOk(result) } // only get the broken pages
            .map { (pages, _) -> pages }
        val answer = brokenPages
            .map { pages -> applyRulesAndFixPages(pages, rules) }
            .sumOf { it.middleElement() }
        return answer.toLong()
    }

    private fun applyRulesAndFixPages(pages: List<Int>, rules: List<List<Int>>): List<Int> {
        var newPages = pages
        var rr = applyRulesPages(newPages, rules).second
        while (!resultOk(rr)) {
            newPages = fixPages(newPages, rr)
            rr = applyRulesPages(newPages, rules).second
        }
        return newPages
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
}