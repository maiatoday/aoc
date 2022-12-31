package days

import util.*
import kotlin.math.abs

typealias Day18ReturnType = Int
typealias Day18InputType = List<String>

object Day18 : Day<Day18ReturnType, Day18InputType> {
    override val number: Int = 18
    override val expectedPart1Test: Day18ReturnType = 64
    override val expectedPart2Test: Day18ReturnType = 58
    override var useTestData = true
    override val debug = true

    override fun part1(input: Day18InputType): Day18ReturnType {
        val lava = input.map { it.toPoint3() }
        return lava.sumOf { p -> 6 - lava.count { it.shareSides(p) } }
    }

    override fun part2(input: Day18InputType): Day18ReturnType {
        val lava = input.map { it.toPoint3() }
        return findSurface(lava)
    }

    //-----------------------------------------------------------

    private fun findSurface(lava: List<Point3>): Int {

        // a thin film of one cube around the lava
        val (xRange, yRange, zRange) = lava.boundaries(1)

        val frontier = ArrayDeque<Point3>()
        val visited: MutableList<Point3> = mutableListOf()

        frontier += Point3(xRange.first, yRange.first, zRange.first)
        val water = mutableSetOf<Point3>()
        while (frontier.isNotEmpty()) {
            val current = frontier.removeFirst()
            if (current !in visited) {
                visited += current
                val neighbours = current.neighbours(true, xRange, yRange, zRange) { p -> p !in lava }
                neighbours.forEach {
                    water += it
                    frontier += it
                }
            }
        }
        val edge = water.sumOf { w -> lava.count { it.shareSides(w) } }
        return edge
    }
    //-----------------------------------------------------------
}