package days

import util.Point
import util.listFromGrid

object Day06 : Day<Long, List<String>> {
    override val number: Int = 6
    override val expectedPart1Test: Long = 41L
    override val expectedPart2Test: Long = 6L
    override var useTestData = true
    override val debug = false

    override fun part1(input: List<String>): Long {
        val guard = Guard(input)
        return guard.uniqueVisited().toLong()
    }

    private fun Guard(input: List<String>): Guard {
        val start = input.listFromGrid("^")
        val obstacles = input.listFromGrid("#")
        val guard = Guard(
            start = start.first(),
            obstacles = obstacles.toSet(),
            xBoundary = input[0].indices,
            yBoundary = input.indices
        )
        guard.startWalking()
        return guard
    }

    override fun part2(input: List<String>): Long {
        val guard = Guard(input)

        return -1L
    }


    class Guard(val start: Point, val obstacles: Set<Point>, val xBoundary: IntRange, val yBoundary: IntRange) {
        val visited: MutableList<Pair<Point, Direction>> = mutableListOf()
        private val visitCount: MutableMap<Point, Int> = mutableMapOf()
        private var direction = Direction.UP
        fun startWalking() {
            var current = start
            while (current.x in xBoundary && current.y in yBoundary) {
                visited.add(current to direction)
                val c = visitCount.getOrDefault(current, 0)
                visitCount[current] = c + 1
                current = walk(current)
            }
        }

        private fun walk(now: Point): Point {
            var next = now + direction.d
            if (next in obstacles) {
                direction = direction.turn()
                next = now + direction.d
            }
            return next
        }

        fun uniqueVisited() = visited.map { (point, _) -> point }.toSet().size
    }

    enum class Direction(val d: Point) {
        UP(Point(0, -1)),
        RIGHT(Point(1, 0)),
        DOWN(Point(0, 1)),
        LEFT(Point(-1, 0));

        fun turn(): Direction = // clockwise 90 degrees
            when (this) {
                UP -> RIGHT
                RIGHT -> DOWN
                DOWN -> LEFT
                LEFT -> UP
            }
    }

    operator fun Point.plus(d: Point) = Point(x + d.x, y + d.y)

}