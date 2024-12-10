package days

import util.Area
import util.Point
import util.neighbours

object Day10 : Day<Long, List<String>> {
    override val number: Int = 10
    override val expectedPart1Test: Long = 36L
    override val expectedPart2Test: Long = 81L
    override var useTestData = true
    override val debug = false

    override fun part1(input: List<String>): Long = findTrailScores(parse(input), Area(input)).toLong()

    override fun part2(input: List<String>): Long = findTrailScores(parse(input), Area(input), true).toLong()

    const val START = 0
    const val END = 9
    fun findTrailScores(map: Map<Point, Int>, area: Area, part2: Boolean = false): Int =
        map.filter { it.value == START }.map { it.key }
            .map {
                if (part2) it to search(it, START + 1, map, area)
                else it to search(it, START + 1, map, area).distinct()
            }.sumOf { it.second.size }


    fun search(p: Point, value: Int, map: Map<Point, Int>, area: Area): List<Point> =
        if (value <= END) {
            buildList {
                val ppp =
                    p.neighbours(maxX = area.xRange.last + 1, maxY = area.yRange.last + 1) //neighbours use < NOT <=
                        .filter { map[it] == value }
                        .flatMap { search(it, value + 1, map, area) }
                addAll(ppp)
            }
        } else {
            if (map[p] == END) listOf(p) else emptyList()
        }

    fun parse(input: List<String>): Map<Point, Int> = buildMap {
        for (y in input.indices) for (x in input[0].indices) {
            if (input[y][x] == '.') put(Point(x, y), -1)
            else put(Point(x, y), input[y][x].digitToInt())
        }
    }
}