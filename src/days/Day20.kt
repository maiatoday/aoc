package days

import util.*

object Day20 : Day<Long, List<String>> {
    override val number: Int = 20
    override val expectedPart1Test: Long = -1L
    override val expectedPart2Test: Long = -1L
    override var useTestData = true
    override val debug = false

    lateinit var area: Area
    lateinit var walls: List<Point>
    lateinit var maze: Set<Point>

    override fun part1(input: List<String>): Long {
        area = Area(input)
        val start = input.listFromGrid("S").first()
        val end = input.listFromGrid("E").first()
        walls = input.listFromGrid("#")
        maze = walls.toPath(area).toSet()
        val answer = race(start, end)
        return -1L //answer.count { it > 100 }.toLong()
    }

    private fun race(
        start: Point,
        end: Point
    ): List<Int> {
        // need to implement the cheets
        return listOf(findPath(start, end, 0))
    }

    val findPathCache: MutableMap<Triple<Point, Point, Int>, Int> = mutableMapOf()
    private fun findPath(
        start: Point,
        end: Point,
        cheat: Int
    ): Int = findPathCache.getOrPut(Triple(start, end, cheat)) {
        var distance: MutableMap<Point, Int> = mutableMapOf()
        var q = maze.toMutableSet()
        q.forEach { distance[it] = Int.MAX_VALUE }
        distance[start] = 0
        val previous = mutableMapOf<Point, Point>()

        while (q.isNotEmpty()) {
            val point = q.minByOrNull { distance[it] ?: 0 } // greedy for lowest distance
            q.remove(point)
            if (point == end) {
                // do end things?
                break
            } else {
                point?.let {
                    it.neighbours(area)
                        .filter { p -> p !in walls }
                        .forEach { frontier ->
                            val newDist = (distance[point] ?: 0) + 1
                            if ((distance[frontier] ?: 0) > newDist) {
                                distance[frontier] = newDist
                                previous[frontier] = point
                            }
                        }
                }
            }
        }
        distance[end] ?: 0
    }

    override fun part2(input: List<String>): Long {
        return -1L
    }
}

