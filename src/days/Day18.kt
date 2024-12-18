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
        val distance = mutableMapOf<Point, Int>()
        val mazePoints = bytes.filter { it.tick < tickMax }
        val q = mazePoints.map { it.p }.toPath(area).toMutableSet()
        q.forEach { distance[it] = Int.MAX_VALUE }
        distance[start] = 0
        val previous = mutableMapOf<Point, Point>()

        while (q.isNotEmpty()) {
            val point = q.minByOrNull { distance[it] ?: 0 } // greedy for lowest distance
            q.remove(point)
            if (point == end) {
                // do end things?
                break
            }
            point?.let {
                it.neighbours(area)
                    .filter { p -> p !in mazePoints.map { it.p } }
                    .forEach { frontier ->
                        val newDist = (distance[point] ?: 0) + 1
                        if ((distance[frontier] ?: 0) > newDist) {
                            distance[frontier] = newDist
                            previous[frontier] = point
                        }
                    }
            }
        }
        return distance[end] ?: 0
    }

    private fun readFallingBytes(input: List<String>): List<TickPoint> =
        input.mapIndexed { i, it ->
            val (x, y) = it.readInts()
            TickPoint(i, Point(x, y))
        }

    data class TickPoint(val tick: Int, val p: Point)

    override fun part2(input: List<String>): Long {
        val area = if (useTestData) TEST_AREA else ACTUAL_AREA
        val tickMax = if (useTestData) TEST_TICK_MAX else ACTUAL_TICK_MAX
        val bytes = readFallingBytes(input)
        var tick = bytes.size - 1
        while (tick > tickMax) {
            println("searching at $tick")
            val steps = calculatePath(area, bytes, tick)
            println("Path steps $steps")
            if (steps > 0) {
                // first path after being blocked
                val xx = bytes.filter { it.tick == tick }
                println("First path possible $xx")
                break
            }
            tick--
        }
        return -1L
    }
}