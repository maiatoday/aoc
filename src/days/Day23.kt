package days

import util.Point
import util.neighbours

object Day23 : Day<Long, List<String>> {
    override val number: Int = 23
    override val expectedPart1Test: Long = 94L
    override val expectedPart2Test: Long = -1L
    override var useTestData = true
    override val debug = false

    override fun part1(input: List<String>): Long {
        val terrain = parseMap(input)
        val start = Point(1, 0)
        val end = Point(terrain.values.first().maxX - 2, terrain.values.first().maxY - 1)
        val distanceToEnd = hike(terrain, start, end)
        return distanceToEnd.toLong()
    }

    private fun hike(terrain: Map<Point, HikePoint>, start: Point, end: Point): Int {
        val forest = terrain.values.filter { it.type == '#' }.map { it.point }
        val seen = mutableSetOf<Point>()
        val distances = mutableMapOf<Point, Int>()
        val deque = ArrayDeque<Pair<HikePoint, Int>>()
        deque.add(terrain.getValue(start) to 0)

        while (deque.isNotEmpty()) {
            val (current, step) = deque.removeFirst()
            if (current.point !in seen) {
                distances[current.point] = step
                seen.add(current.point)
            } else {
                if (distances.getValue(current.point) < step) distances[current.point] = step
            }
            if (current.point != end)
                deque.addAll(current.next(forest).filter { it !in seen }.map { terrain.getValue(it) to step + 1 })
        }
        return distances.getValue(end)
    }


    override fun part2(input: List<String>): Long {
        return -1L
    }

    data class HikePoint(val point: Point, val type: Char, val maxX: Int, val maxY: Int) {
        fun next(forest: List<Point>) = when (type) {
            '>' -> listOf(Point(point.x + 1, point.y))
            '<' -> listOf(Point(point.x - 1, point.y))
            'v' -> listOf(Point(point.x, point.y + 1))
            '^' -> listOf(Point(point.x, point.y - 1))
            else -> point.neighbours()
        }.filter { it !in forest }
    }

    private fun parseMap(input: List<String>): Map<Point, HikePoint> = buildMap {
        val maxX = input.first().length
        val maxY = input.size
        for (y in input.indices) for (x in input.first().indices)
            this[Point(x, y)] = HikePoint(Point(x, y), input[y][x], maxX, maxY)
    }
}
