package days

import util.*

object Day14 : Day<Long, List<String>> {
    override val number: Int = 14
    override val expectedPart1Test: Long = 12L
    override val expectedPart2Test: Long = -1L
    override var useTestData = true
    override val debug = false

    val TEST_AREA = Area(0..10, 0..6)
    val REAL_AREA = Area(0..100, 0..102)
    var area: Area = TEST_AREA
    fun part1Test(input: List<String>): Long = part1(input)
    fun part1Real(input: List<String>): Long {
        area = REAL_AREA
        return part1(input)
    }

    override fun part1(input: List<String>): Long {
        val robots = input.map { Robot(it, area) }
        repeat(100) {
            robots.forEach { it.step() }
        }
        val quadrantRanges = listOf(
            Area(0..(area.xRange.last / 2 - 1), 0..(area.yRange.last / 2 - 1)),
            Area((area.xRange.last / 2 + 1)..area.xRange.last, 0..(area.yRange.last / 2 - 1)),
            Area(0..(area.xRange.last / 2 - 1), (area.yRange.last / 2 + 1)..area.yRange.last),
            Area((area.xRange.last / 2 + 1)..area.xRange.last, (area.yRange.last / 2 + 1)..area.yRange.last)
        )
        val robotsInQuadrants = quadrantRanges.map { range ->
            robots.filter { it.current in range }
        }
        val safetyFactor = robotsInQuadrants.fold(1) { acc, r -> acc * r.size }
        return safetyFactor.toLong()
    }

    fun Robot(specs: String, area: Area): Robot {
        val (p, v) = specs.readIntsSign()
            .chunked(2)
            .map { Point(it[0], it[1]) }
        return Robot(p = p, v = v, area = area)
    }

    class Robot(val p: Point, val v: Point, val area: Area) {
        var current = p
        fun step() {
            current = (current + v).wrap(area)
        }
    }

    fun Point.wrap(area: Area): Point {
        val newX = when {
            x > area.xRange.last -> x - (area.xRange.last + 1)
            x < 0 -> (area.xRange.last + 1) + x
            else -> x
        }
        val newY = when {
            y > area.yRange.last -> y - (area.yRange.last + 1)
            y < 0 -> (area.yRange.last + 1) + y
            else -> y
        }
        return Point(newX, newY)
    }

    fun List<Robot>.debug(area: Area) {
        this.map { it.current }.debug(area = area)
    }

    fun List<Robot>.xmasTree(): Boolean {
        val points = this.map { it.current }.toSet()
        return points.size == this.size
    }

    override fun part2(input: List<String>): Long {
        area = REAL_AREA
        val robots = input.map { Robot(it, area) }
        var stepCount = 0L
        while (!robots.xmasTree()) {
            robots.forEach { it.step() }
            stepCount++
        }
        robots.debug(area)
        return stepCount
    }

}
