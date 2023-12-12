package days

import kotlin.math.abs

object Day11 : Day<Long, List<String>> {
    override val number: Int = 11
    override val expectedPart1Test: Long = 374L
    override val expectedPart2Test: Long = 82000210L
    override var useTestData = true
    override val debug = false

    override fun part1(input: List<String>): Long {
        val galaxies = findGalaxies(input, 2)
        val doublePaths = galaxies.flatMap { g -> (galaxies - g).map { g.shortestPath(it) } }.sum()
        return doublePaths / 2 // 1->2 is the same as 2->1
    }

    private fun findGalaxies(input: List<String>, rate: Int): List<Galaxy> = buildList {
        val blankColumns = input.findEmptyColumns()
        val blankRows = input.findEmptyRows()
        input.foldIndexed(0L) { r, er, s ->
            s.foldIndexed(0L) { c, ec, b ->
                if (b == '#') {
                    add(Galaxy(er, ec))
                }
                if (c in blankColumns) ec + rate else ec + 1
            }
            if (r in blankRows) er + rate else er + 1
        }
    }

    private fun List<String>.findEmptyColumns() = this[0].indices.filter { c -> this.all { r -> r[c] == '.' } }
    private fun List<String>.findEmptyRows() = this.mapIndexed { i, r -> (i to r) }.filter { "#" !in it.second }.map { it.first }

    data class Galaxy(val row: Long, val column: Long) {
        fun shortestPath(other: Galaxy) = abs(row - other.row) + abs(column - other.column)
    }

    override fun part2(input: List<String>): Long {
        val galaxies = findGalaxies(input, 1000000)
        val doublePaths = galaxies.flatMap { g -> (galaxies - g).map { g.shortestPath(it) } }.sum()
        return doublePaths / 2 // 1->2 is the same as 2->1
    }
}
