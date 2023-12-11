package days

import kotlin.math.abs

object Day11 : Day<Long, List<String>> {
    override val number: Int = 11
    override val expectedPart1Test: Long = 374L
    override val expectedPart2Test: Long = -1L
    override var useTestData = true
    override val debug = false

    override fun part1(input: List<String>): Long {
        val expandedSpace = expand(input)
        val galaxies = findGalaxies(expandedSpace)
        val doublePaths = galaxies.flatMap { g -> (galaxies - g).map { g.shortestPath(it) } }.sum().toLong()
        return doublePaths / 2 // 1->2 is the same as 2->1
    }

    private fun expand(input: List<String>): List<String> = buildList {
        val blankColumns = input[0].indices.filter { c -> input.all { r -> r[c] == '.' } }
        val blankRow = ".".repeat(input[0].length + blankColumns.size)
        input.map { row ->
            if ("#" !in row) {
                add(blankRow)
                add(blankRow)
            } else {
                val expandedColumns = buildString(blankRow.length) {
                    row.mapIndexed { c, space ->
                        this@buildString.append(space)
                        if (c in blankColumns) this@buildString.append(space)

                    }
                }
                add(expandedColumns)
            }
        }
    }

    private fun findGalaxies(input: List<String>): List<Galaxy> = buildList {
        var count = 1
        input.mapIndexed { r, s ->
            s.mapIndexed { c, b ->
                if (b == '#') {
                    add(Galaxy(count, r, c))
                    count++
                }
            }
        }
    }


    data class Galaxy(val id: Int, val row: Int, val column: Int) {
        fun shortestPath(other: Galaxy) = abs(row - other.row) + abs(column - other.column)
    }

    override fun part2(input: List<String>): Long {
        return -1L
    }
}
