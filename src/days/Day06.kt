package days

import util.Point
import util.listFromGrid

object Day06 : Day<Long, List<String>> {
    override val number: Int = 6
    override val expectedPart1Test: Long = 41L
    override val expectedPart2Test: Long = 6L
    override var useTestData = true
    override val debug = true

    override fun part1(input: List<String>): Long {
        val guard = Guard(input)
        val visited = guard.startWalking()
        return visited.map { it.where }.distinct().size.toLong()
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
        return guard
    }

    override fun part2(input: List<String>): Long {
        val guard = Guard(input)
        val answer = guard.findObstructions()
        return answer.toLong()
    }


    class Guard(val start: Point, val obstacles: Set<Point>, val xBoundary: IntRange, val yBoundary: IntRange) {

        fun startWalking(allObstacles: Set<Point> = obstacles): MutableSet<Spot> {
            val visited: MutableSet<Spot> = mutableSetOf()
            var current = Spot(start, Direction.UP)
            while (current.where.x in xBoundary && current.where.y in yBoundary) {
                visited.add(current)
                current = walk(current, allObstacles)
            }
            return visited
        }

        private fun walk(now: Spot, oo: Set<Point>): Spot {
            var next = now.where + now.direction.d
            var nextDirection = now.direction
            if (next in oo) {
                nextDirection = now.direction.turn()
                next = now.where + nextDirection.d
            }
            return Spot(next, nextDirection)
        }

        //1563 bad 1564 bad
        fun findObstructions(): Int {
            // start with the known path
            val originalPath = startWalking().map { it.where }.distinct()
            val boxes: MutableSet<Point> = mutableSetOf()
//            for (x in xBoundary) for (y in yBoundary) {
//                val c = Point(x, y)
            for (o in originalPath - start) {
                val ok = startWalkingCheckLoop(obstacles + o)
                if (ok) boxes.add(o)
            }
            return boxes.size
        }

        fun startWalkingCheckLoop(allObstacles: Set<Point>): Boolean {
            val visited: MutableSet<Spot> = mutableSetOf()
            var current = Spot(start, Direction.UP)
            visited.add(current)
            while (true) {
                current = walk(current, allObstacles)
                if (current.where.x !in xBoundary || current.where.y !in yBoundary) return false
                else if (current in visited) return true
                else visited.add(current)
            }
            return false
        }

    }

    data class Spot(val where: Point, val direction: Direction)

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