package days

import util.splitByBlankLine
import kotlin.math.max

object Day13 : Day<Long, List<String>> {
    override val number: Int = 13
    override val expectedPart1Test: Long = 405L
    override val expectedPart2Test: Long = 400L
    override var useTestData = true
    override val debug = false

    override fun part1(input: List<String>): Long {
        val notes = input.splitByBlankLine()
        val vertical = notes.sumOf { findMirror(it, false) }
        val horizontal = notes.sumOf { findMirror(it, true) }
        return (horizontal + (100 * vertical)).toLong()
    }

    private fun findMirror(input: List<String>, transpose: Boolean, smudgeCount: Int = 0): Int {
        val field = if (transpose) transpose(input) else input
        val possible = (1..<field.size)
                .filter { it.isMirror(field, smudgeCount) }
                .toList()
        return if (possible.isEmpty()) 0 else possible.last()
    }

    private fun Int.isMirror(
            field: List<String>,
            smudgeCount: Int): Boolean {
        val l = (this - 1) downTo 0
        val r = this..<field.size
        val zippy = l zip r
        var maxSmudge = smudgeCount
        val isMirror = zippy.map { (first, second) ->
            if (compareEquals(field[first], field[second])) {
                true
            } else {
                if (maxSmudge > 0 && compareWithSmudge(field[first], field[second], maxSmudge)) {
                    maxSmudge = max(0, maxSmudge - 1)
                    true
                } else {
                    false
                }
            }
        }.all { it }
        // we have to fix smudgeCount smudges otherwise for part two we get duplicate mirrors
        return isMirror && maxSmudge == 0
    }

    private fun transpose(input: List<String>): List<String> = buildList {
        input[0].indices.map { c ->
            add(input.map { r -> r[c] }.joinToString(""))
        }
    }

    override fun part2(input: List<String>): Long {
        val notes = input.splitByBlankLine()
        val vertical = notes.sumOf { findMirror(it, false, 1) }
        val horizontal = notes.sumOf { findMirror(it, true, 1) }
        return (horizontal + (100 * vertical)).toLong()
    }

    val compareWithSmudge: (String, String, Int) -> Boolean = { a, b, smudgeCount -> ((a zip b).filter { it.first != it.second }.size == smudgeCount) }
    val compareEquals: (String, String) -> Boolean = { a, b -> a == b }
}
