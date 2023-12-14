package days

import util.splitByBlankLine

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

    data class IndexString(val i: Int, val s: String)

    private fun findMirror(input: List<String>, transpose: Boolean): Int {
        val field = if (transpose) transpose(input) else input
        val possible = field
                .findDoubleLine()
                .filter { it.isMirror(field) }
                .toList()
        return if (possible.isEmpty()) 0 else possible.first()
    }

    private fun Int.isMirror(
            field: List<String>,
            compare: (String, String) -> Boolean = compareEquals): Boolean {
        val l = (this - 1) downTo 0
        val r = this..<field.size
        val zippy = l zip r
        val isMirror = zippy.all { compare(field[it.first], field[it.second]) }
        return isMirror
    }

    private fun transpose(input: List<String>): List<String> = buildList {
        input[0].indices.map { c ->
            add(input.map { r -> r[c] }.joinToString(""))
        }
    }

    private fun List<String>.findDoubleLine(compare: (String, String) -> Boolean = compareEquals) =
            this.asSequence()
                    .mapIndexed { i, l -> IndexString(i, l) }
                    .zipWithNext()
                    .filter {
                        compare(it.first.s, it.second.s)
                    }
                    .map { it.second.i }

    override fun part2(input: List<String>): Long {

        val notes = input.splitByBlankLine()
        val both = notes.map {
            fixSmudge(it, transpose = false) to fixSmudge(it, transpose = true)
        }
        val horizontal = both.sumOf {
            if (it.second == 0) it.first else 0
        }

        val vertical = both.sumOf {
            if (it.first == 0) it.second else 0
        }

        return (horizontal + (100 * vertical)).toLong()
    }

    private fun fixSmudge(input: List<String>, transpose: Boolean): Int {
        println("transpose = $transpose")
        val field = if (transpose) transpose(input) else input

        val possible = field.fd(compare = compareWithSmudge).ifEmpty {
            field.fd()
        }
        val temp = possible.filter { it.isSmudgedMirror(field) }
        println(temp)
        return if (possible.isEmpty()) 0 else possible.first()
    }

    private fun Int.isSmudgedMirror(
            field: List<String>
    ): Boolean {
        var smudgeCount = 0
        val l = (this - 1) downTo 0
        val r = this..<field.size
        val zippy = l zip r
        val isMirror = zippy.all {
            val first = field[it.first]
            val second = field[it.second]
            if (smudgeCount == 0) {
                if (compareWithSmudge(first, second)) {
                    smudgeCount++
                    true
                } else compareEquals(first, second)
            } else {
                compareEquals(first, second)
            }
        }
        //val isMirror = zippy.all { compare(field[it.first], field[it.second]) }
        return isMirror
    }

    val compareWithSmudge: (String, String) -> Boolean = { a, b -> ((a zip b).filter { it.first != it.second }.size == 1) }
    val compareEquals: (String, String) -> Boolean = { a, b -> a == b }

    private fun List<String>.fd(compare: (String, String) -> Boolean = { a, b -> a == b }) =
            this
                    .mapIndexed { i, l -> IndexString(i, l) }
                    .zipWithNext()
                    .filter {
                        compare(it.first.s, it.second.s)
                    }
                    .map { it.second.i }
}
