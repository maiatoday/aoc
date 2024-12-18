package days

import util.*

object Day18 : Day<Long, List<String>> {
    override val number: Int = 18
    override val expectedPart1Test: Long = 22L
    override val expectedPart2Test: Long = -1L
    override var useTestData = true
    override val debug = false

    val TEST_AREA = Area(0..6, 0..6)
    val ACTUAL_AREA = Area(0..70, 0..70)
    val TEST_TICK_MAX = 12
    val ACTUAL_TICK_MAX = 1024
    override fun part1(input: List<String>): Long {
        val area = if (useTestData) TEST_AREA else ACTUAL_AREA
        val tickMax = if (useTestData) TEST_TICK_MAX else ACTUAL_TICK_MAX
        val bytes = readFallingBytes(input)
        val steps = calculatePath(area, bytes, tickMax)
        return steps.toLong()
    }

    private fun calculatePath(area: Area, bytes: List<TickPoint>, tickMax: Int): Int {
        val end = Point(area.xRange.last, area.yRange.last)
        val start = Point(0, 0)
        val visited = mutableListOf<Point>()
        val q = ArrayDeque<TickPoint>()
        q.add(TickPoint(0, start))
        val mazePoints = bytes.filter { it.tick < tickMax }
        var pathLength = 0
        while (q.isNotEmpty()) {
            val (tick, point) = q.removeLast()
            println("Checking point $point at tick $tick")
            bytes.filter { it.tick < tick }.map { it.p }.debug(area = area)
            visited.add(point)
            if (point == end) {
                pathLength = tick
                break
            }
            val neighbours = point.neighbours(area)
                .filter { p -> (p !in visited && p !in mazePoints.filter { it.tick < tick + 1 }.map { it.p }) }
            q.addAll(neighbours.map { TickPoint(tick + 1, it) })
        }
        return pathLength
    }

    private fun readFallingBytes(input: List<String>): List<TickPoint> =
        input.mapIndexed { i, it ->
            val (x, y) = it.readInts()
            TickPoint(i, Point(x, y))
        }

    data class TickPoint(val tick: Int, val p: Point)

    override fun part2(input: List<String>): Long {
        return -1L
    }
}