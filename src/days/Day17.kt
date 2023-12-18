package days

import util.Point
import util.neighbours

object Day17 : Day<Long, List<String>> {
    override val number: Int = 17
    override val expectedPart1Test: Long = 102L
    override val expectedPart2Test: Long = -1L
    override var useTestData = true
    override val debug = false

    override fun part1(input: List<String>): Long {
        val cityMap = parse(input)
        val result = findShortestPath(cityMap, Point(0, 0), Point(input.first().indices.last, input.indices.last))
        val lowestHeatLoss = result.lowestCost()?.toLong() ?: -1L
        return lowestHeatLoss
    }

    private fun findShortestPath(cityMap: Map<Point, Int>, start: Point, end: Point): TraverseResult {
        val maxX = cityMap.keys.toList().maxOfOrNull { it.x } ?: end.x
        val maxY = cityMap.keys.toList().maxOfOrNull { it.y } ?: end.y
        val cost = mutableMapOf<Point, Int>()
        val previous = mutableMapOf<Point, Point?>()
        cityMap.keys.forEach { p ->
            cost[p] = Integer.MAX_VALUE
            previous[p] = null
        }
        cost[start] = 0

        val q = start.neighbours(maxY, maxX).toMutableSet()
        q.forEach {
            previous[it] = start
        }

        while (q.isNotEmpty()) {
            val current = q.first()
            q.remove(current)
            val prev = previous[current]


            if (current == end) {
                break // Found shortest path to target
            }
            val testCost = (cityMap[current] ?: 0) + (cost[prev] ?: 0)
            if (testCost < (cost[current] ?: Int.MAX_VALUE)) {
                cost[current] = testCost
                previous[current] = prev
            }

            q.addAll(current.neighbours(maxY, maxX))

        }
        return TraverseResult(previous, cost, start, end)
    }

    fun allEdges(xRange: IntRange, yRange: IntRange, start: Point): Set<Pair<Point, Point>> = buildSet {
        start.neighbours(maxX = xRange.last, maxY = yRange.last)

    }


    class TraverseResult(val previous: Map<Point, Point?>, val cost: Map<Point, Int>, val start: Point, val end: Point) {

//        fun shortestPath(from: Node = source, to: Node = target, list: List<Node> = emptyList()): List<Node> {
//            val last = prev[to] ?: return if (from == to) {
//                list + to
//            } else {
//                emptyList()
//            }
//            return shortestPath(from, last, list) + to
//        }

        fun lowestCost(): Int? {
            val lowest = cost[end]
            if (lowest == Integer.MAX_VALUE) {
                return null
            }
            return lowest
        }
    }


    private fun parse(input: List<String>) = buildMap<Point, Int> {
        for (y in input.indices) for (x in input.first().indices) {
            this[Point(x, y)] = input[y][x].digitToInt()
        }
    }

    override fun part2(input: List<String>): Long {
        return -1L
    }
}
