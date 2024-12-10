package days

import util.Area
import util.Point
import util.filterComments
import util.neighbours

object Day10 : Day<Long, List<String>> {
    override val number: Int = 10
    override val expectedPart1Test: Long = 36L
    override val expectedPart2Test: Long = 81L
    override var useTestData = true
    override val debug = false

    const val START = 0
    const val END = 9
    override fun part1(input: List<String>): Long {
        val map = parse(input.filterComments())
        val area = Area(input)
        val scores = findTrailScores(map, area)
        return scores.toLong()
    }

    fun findTrailScores(map: Map<Point, Int>, area: Area, part2: Boolean = false): Int {
        val possible = map.filter { it.value == START }.map { it.key }
        if (part2) {
            val answer = possible.map {
                it to search(it, START + 1, map, area)
            }
            val zz = answer
                .sumOf { it.second.size }
            return zz
        } else {
            val answer = possible.map {
                it to search(it, START + 1, map, area).distinct()
            }
            val zz = answer
                .sumOf { it.second.size }
            return zz
        }
    }

    fun search(p: Point, value: Int, map: Map<Point, Int>, area: Area): List<Point> =
        if (value <= END) {
            buildList {
                val ppp = p.neighbours(maxX = area.xRange.last + 1, maxY = area.yRange.last + 1)
                val nnn = ppp.filter { map[it] == value }
                val zzz = nnn.flatMap { search(it, value + 1, map, area) }
                addAll(zzz)
            }
            //  }
        } else {
            if (map[p] == END) listOf(p) else emptyList()
        }


    fun parse(input: List<String>): Map<Point, Int> = buildMap {
        for (y in input.indices) for (x in input[0].indices) {
            if (input[y][x] == '.') put(Point(x, y), -1)
            else put(Point(x, y), input[y][x].digitToInt())
        }
    }

    override fun part2(input: List<String>): Long {
        val map = parse(input)
        val area = Area(input)
        val scores = findTrailScores(map, area, true)
        return scores.toLong()
    }
}