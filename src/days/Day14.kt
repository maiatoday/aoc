package days

import util.PPoint
import util.Point

object Day14 : Day<Long, List<String>> {
    override val number: Int = 14
    override val expectedPart1Test: Long = 136L
    override val expectedPart2Test: Long = 64L
    override var useTestData = true
    override val debug = false

    override fun part1(input: List<String>): Long {
        val field = input.toCharGrid()
        tilt(field)
        return field.totalLoad()
    }

    private fun tilt(field: Array<CharArray>, direction: Direction = Direction.North) {
        // opting for code duplication instead of rewriting the whole matrix in memory and rotating anti clockwise
        val rowRange = field.indices
        val columnRange = field.first().indices
        when (direction) {
            Direction.North -> {
                for (outerR in 1..rowRange.last) {
                    for (r in outerR downTo 1) {
                        for (c in columnRange) {
                            if (field[r][c] == 'O' && field[r - 1][c] == '.') {
                                field[r][c] = '.'
                                field[r - 1][c] = 'O'
                            }
                        }
                    }
                }
            }

            Direction.West -> {
                for (outerC in 1..columnRange.last) {
                    for (c in outerC downTo 1) {
                        for (r in rowRange) {
                            if (field[r][c] == 'O' && field[r][c - 1] == '.') {
                                field[r][c] = '.'
                                field[r][c - 1] = 'O'
                            }
                        }
                    }
                }
            }

            Direction.South -> {
                for (outerR in rowRange.last - 1 downTo 0) {
                    for (r in outerR..<rowRange.last) {
                        for (c in columnRange) {
                            if (field[r][c] == 'O' && field[r + 1][c] == '.') {
                                field[r][c] = '.'
                                field[r + 1][c] = 'O'
                            }
                        }
                    }
                }
            }

            Direction.East -> {
                for (outerC in columnRange.last - 1 downTo 0) {
                    for (c in outerC..<columnRange.last) {
                        for (r in rowRange) {
                            if (field[r][c] == 'O' && field[r][c + 1] == '.') {
                                field[r][c] = '.'
                                field[r][c + 1] = 'O'
                            }
                        }
                    }
                }
            }
        }
    }

    private fun Array<CharArray>.totalLoad(): Long =
            this.foldIndexed(0L) { r, sum, s ->
                s.fold(sum) { sum2, c ->
                    if (c == 'O') sum2 + (this.indices.last - r + 1) else sum2
                }
            }

    enum class Direction {
        North, West, South, East
    }

    private fun spin(field: Array<CharArray>) {
        tilt(field, Direction.North)
        tilt(field, Direction.West)
        tilt(field, Direction.South)
        tilt(field, Direction.East)
    }

    override fun part2(input: List<String>): Long {
        val field = input.toCharGrid()
        val seen = mutableListOf<Int>()
        var currentHash = field.contentDeepHashCode()
        // check for cycles
        while (currentHash !in seen) {
            seen.add(currentHash)
            spin(field)
            currentHash = field.contentDeepHashCode()
        }
        // figure out how many more times to run to get to the same offset after the repeat for a billion
        val offset = seen.indexOf(currentHash)
        val moreRuns = (1_000_000_000 - offset) % (seen.size - offset)
        repeat(moreRuns) {
            spin(field)
        }
        return field.totalLoad()
    }

    private fun List<String>.toCharGrid() = Array(size) { get(it).toCharArray() }

}
