package days

import util.Direction
import util.Point
import util.plus
import java.util.PriorityQueue

object Day17 : Day<Long, List<String>> {
    override val number: Int = 17
    override val expectedPart1Test: Long = 102L
    override val expectedPart2Test: Long = -1L
    override var useTestData = true
    override val debug = false

    override fun part1(input: List<String>): Long {
        val cityMap = parse(input)
        val result = findShortestPath(cityMap, Point(0, 0), Point(input.first().indices.last, input.indices.last))
        return result.toLong()
    }

    private fun findShortestPath(cityMap: Map<Point, Int>, start: Point, end: Point): Int {
        val seen = mutableMapOf<Point, Int>()
        val q = PriorityQueue<Spot> { one, other ->
            one.cost - other.cost
        }

        q.add(Spot(start, 0, Direction.Right, 0))
        while (q.isNotEmpty()) {
            val current = q.remove()
            if (current.position == end) return current.cost

            if (current.position !in seen || current.cost < (seen[current.position] ?: Int.MAX_VALUE)) {
                seen[current.position] = current.cost
                val newSpots = current.next(cityMap, current.cost)
                q.addAll(newSpots)
            }
        }
        // We shouldn't get here since it should have jumped out in the loop
        return seen.getValue(end)
    }

    data class Spot(val position: Point, val cost: Int, val direction: Direction, val stepCount: Int) {
        fun next(cityMap: Map<Point, Int>, cost: Int, maxSteps: Int = 3): List<Spot> =
                Direction.compass()
                        .filter { !direction.isBackWards(it) }
                        .mapNotNull { possibleDirection ->
                            val newPosition = position + possibleDirection.p
                            val newStepCount = if (possibleDirection == direction) stepCount + 1 else 0
                            if (newPosition in cityMap && newStepCount <= maxSteps) {
                                Spot(newPosition, cost + cityMap.getValue(newPosition), possibleDirection, newStepCount)
                            } else null
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
