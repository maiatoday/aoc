package days

import util.Point
import util.listFromGrid

object Day07 : Day<Long, List<String>> {
    override val number: Int = 7
    override val expectedPart1Test: Long = 21L
    override val expectedPart2Test: Long = 40L
    override var useTestData = true
    override val debug = false

    override fun part1(input: List<String>): Long {
        val (start, diagram) = parseManifoldDiagram(input)
        return countSplits(start, diagram, input)
    }

    private fun countSplits(start: Point, diagram: List<Point>, input: List<String>): Long {
        var count = 0
        var beams = listOf(start)
        repeat(input.size) { y ->
            val splittersX = diagram.filter { it.y == y }.map { it.x }
            val newBeams = buildList {
                for (b in beams) {
                    if (b.x in splittersX) {
                        add(Point(y = y, x = b.x - 1))
                        add(Point(y = y, x = b.x + 1))
                        count++
                    } else {
                        add(Point(y = y, x = b.x))
                    }
                }
            }
            beams = newBeams.distinct()
        }
        return count.toLong()
    }

    private fun parseManifoldDiagram(input: List<String>): Pair<Point, List<Point>> {
        val start = Point(y = 0, x = input.first().indexOf('S'))
        val diagram = input.listFromGrid("^")
        return start to diagram
    }

    override fun part2(input: List<String>): Long {
        val (start, dd) = parseManifoldDiagram(input)
        diagram = dd
        maxY = input.size
        return countPaths(start)
    }

    var diagram: List<Point> = emptyList()
    var maxY = 0

    val cache = mutableMapOf<Point, Long>()

    private fun countPaths(start: Point): Long {
        val count = cache.getOrPut(start) {
            if (start.y == maxY) {
                1L
            } else {
                val splittersX = diagram.filter { it.y == start.y }.map { it.x }
                if (start.x in splittersX) {
                    countPaths(Point(start.x - 1, start.y + 1)) +
                            countPaths(Point(start.x + 1, start.y + 1))
                } else {
                    countPaths(Point(start.x, start.y + 1))
                }
            }
        }
        return count
    }
}