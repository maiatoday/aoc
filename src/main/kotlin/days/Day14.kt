package days

import util.Point

typealias Day14ReturnType = Int
typealias Day14InputType = List<String>

typealias Cave = MutableMap<Point, Int>

object Day14 : Day<Day14ReturnType, Day14InputType> {
    override val number: Int = 14
    override val expectedPart1Test: Day14ReturnType = 24
    override val expectedPart2Test: Day14ReturnType = 93

    private const val debug = false
    private val sandSource = (500 to 0)

    override fun part1(input: Day14InputType): Day14ReturnType {
        val (caveWidth, caveHeight) = input.toCaveDimensions()
        val cave = input.toCave(caveWidth, caveHeight)
        cave.debug(caveWidth, caveHeight)
        return cave.pourSand(sandSource, caveWidth, caveHeight)
    }

    override fun part2(input: Day14InputType): Day14ReturnType {
        val (caveWidth, caveHeight) = input.toCaveDimensions(true)
        val cave = input.toCave(caveWidth, caveHeight, true)
        cave.debug(caveWidth, caveHeight)
        val sandCount = cave.pourSand(sandSource, caveWidth, caveHeight)
        return sandCount + 1 // add the last block that couldn't move
    }

    private fun List<String>.toCaveDimensions(withFloor: Boolean = false): Pair<IntRange, IntRange> {
        val points = this.flatMap {
            it.toPoints()
        }
        val minX = points.minOfOrNull { it.first } ?: 0
        val maxX = points.maxOfOrNull { it.first } ?: 1000
        val maxY = points.maxOfOrNull { it.second } ?: 1000
        val yOffset = if (withFloor) 2 else 0
        val xOffset = if (withFloor) maxY * 2 else 0
        return ((minX - xOffset)..(maxX + xOffset) to (0..(maxY + yOffset)))
    }

    private fun List<String>.toCave(caveWidth: IntRange, caveHeight: IntRange, withFloor: Boolean = false): Cave {
        val cave: Cave = mutableMapOf()
        for (x in caveWidth) for (y in caveHeight) cave[x to y] = SPACE
        val rockRanges = this.map { s ->
            s.toRanges()
        }
        rockRanges.forEach {
            it.forEach { w ->
                cave.addRockWall(w)
            }
        }
        if (withFloor) {
            val y = caveHeight.last
            for (x in caveWidth) {
                cave[x to y] = ROCK
            }
        }
        return cave
    }

    private fun String.toPoints(): List<Point> =
        this.split(" -> ")
            .flatMap { p ->
                p.split(",")
                    .chunked(2) {
                        it[0].toInt() to it[1].toInt()
                    }
            }

    private fun String.toRanges(): List<Pair<Point, Point>> {
        val points = this.toPoints()
        return points.zipWithNext()
    }

    private fun Cave.addRockWall(range: Pair<Point, Point>) {
        if (range.first.first == range.second.first) {
            // vertical
            val x = range.first.first
            val wall = makeRange(range.first.second, range.second.second)
            for (yy in wall) {
                this[(x to yy)] = ROCK
            }
        } else if (range.first.second == range.second.second) {
            // horizontal
            val y = range.first.second
            val wall = makeRange(range.first.first, range.second.first)
            for (xx in wall) {
                this[(xx to y)] = ROCK
            }
        } else {
            error("Oops weird wall $range")
        }
    }

    private fun makeRange(i: Int, j: Int): IntRange =
        if (i <= j) i..j
        else j..i


    private fun Cave.pourSand(
        sandSource: Point,
        caveWidth: IntRange,
        caveHeight: IntRange
    ): Int {
        var sandCount = 0
        var overflow = false
        while (!overflow) {
            overflow = this.sandFall(sandSource)
            if (!overflow) sandCount++
            this.debug(caveWidth, caveHeight)
        }
        return sandCount
    }

    private fun Cave.sandFall(
        point: Point
    ): Boolean {
        var position = point
        var overflow = false
        var sandCanMove = true
        move@ while (sandCanMove) {
            val x = position.first
            val y = position.second
            val newPositions = listOf((x to y + 1), (x - 1 to y + 1), (x + 1 to y + 1))
            var restCount = 0
            for (p in newPositions) {
                if (!this.contains(p)) {
                    overflow = true
                    sandCanMove = false
                    break
                } else if (this[p] == SPACE) {
                    this[position] = SPACE
                    this[p] = SAND
                    position = p
                    sandCanMove = true
                    break
                } else {
                    restCount++
                }
            }
            if (restCount == newPositions.size) sandCanMove = false
        }
        if (position == point) overflow = true

        // if we didn't overflow then do another otherwise it should stop
        return overflow
    }

    private fun Cave.debug(caveWidth: IntRange, caveHeight: IntRange) {
        if (debug) {
            println()
            for (y in caveHeight) {
                for (x in caveWidth) {
                    val c = when (this[x to y]) {
                        SPACE -> "."
                        ROCK -> "#"
                        SAND -> "o"
                        else -> "?"
                    }
                    print(c)
                }
                println()
            }
        }
    }

    private const val ROCK = 2
    private const val SAND = 1
    private const val SPACE = 0

}