package days

import util.*


object Day16 : Day<Long, List<String>> {
    override val number: Int = 16
    override val expectedPart1Test: Long = 7036L //11048
    override val expectedPart2Test: Long = -1L
    override var useTestData = true
    override val debug = false

    override fun part1(input: List<String>): Long {
        val (s, e, maze) = readMaze(input)
        val area = Area(input)
        val shortest = findShortestPath(s, e, maze, area)
        return shortest.toLong()
    }

    private fun findShortestPath(
        start: Point,
        end: Point,
        maze: List<Point>,
        area: Area
    ): Int {
        val path = maze.toPath(area).toSet()
        val spotsToVisit = path.toMutableSet()
        val visitedCosts = mutableMapOf<Point, Int>()
        visitedCosts[start] = 0

        val q = mutableListOf<Step>()
        start.neighboursOnPath(path).forEach { q.add(Step(start, it, Direction.EAST)) }
        while (spotsToVisit.isNotEmpty()) {
           // println("q size ${q.size}")
            val (src, dest, dir) = q.removeFirst()
            val deltaCost = (src to dest).cost(dir)

            // remember the lowest cost
            val costSrc = visitedCosts[src] ?: throw Exception("Oops, no source cost, reindeer disqualified")
            val oldCost = visitedCosts[dest] ?: Int.MAX_VALUE
            if (costSrc + deltaCost < oldCost) {
               // println("$dir $src to $dest cost is ${costSrc + deltaCost}")
                visitedCosts[dest] = costSrc + deltaCost
            }

            if (q.none { it.src == src }) {
               // println("remove $src")
                spotsToVisit.remove(src)
            }

            // add new spots to the list to check
            val nextDirection = (src to dest).nextDirection(dir)
            dest.neighboursOnPath(path)
                .filter { it in spotsToVisit }
                .forEach { q.add(Step(dest, it, nextDirection)) }
        }
        return visitedCosts[end] ?: throw Exception("Oops, no end in sight, reindeer wandering aimlessly")
    }

    data class Step(val src: Point, val dest: Point, val direction: Direction)

    private fun readMaze(input: List<String>): Triple<Point, Point, List<Point>> {
        val maze = input.listFromGrid("#")
        val start = input.listFromGrid("S").first()
        val end = input.listFromGrid("E").first()
        return Triple(start, end, maze)
    }

    fun Point.neighboursOnPath(path: Set<Point>): List<Point> = this.neighbours().filter { it in path }

    fun Pair<Point, Point>.cost(direction: Direction): Int =
        if (this.first + direction.d == this.second) 1
        else if ((this.first + direction.turnC().d == this.second)
            || (this.first + direction.turnA().d == this.second)
        ) 1001
        else if (this.first + direction.uTurn().d == this.second) 2001
        else throw Exception("Oops teleportation detected! Reinder disqualified!")

    fun Pair<Point, Point>.nextDirection(direction: Direction): Direction =
        if (this.first + direction.d == this.second) direction
        else if (this.first + direction.turnC().d == this.second) direction.turnC()
        else if (this.first + direction.turnA().d == this.second) direction.turnA()
        else if (this.first + direction.uTurn().d == this.second) direction.uTurn()
        else throw Exception("Oops U- turn  detected! Reinder disqualified!")

    enum class Direction(val d: Point) {
        NORTH(Point(0, -1)),
        EAST(Point(1, 0)),
        SOUTH(Point(0, 1)),
        WEST(Point(-1, 0));

        fun turnC(): Direction = // clockwise 90 degrees
            when (this) {
                NORTH -> EAST
                EAST -> SOUTH
                SOUTH -> WEST
                WEST -> NORTH
            }

        fun turnA(): Direction = // Anti-clockwise 90 degrees
            when (this) {
                NORTH -> WEST
                WEST -> SOUTH
                SOUTH -> EAST
                EAST -> NORTH
            }

        fun uTurn(): Direction =
            when (this) {
                NORTH -> SOUTH
                WEST -> EAST
                SOUTH -> NORTH
                EAST -> WEST
            }
    }

    override fun part2(input: List<String>): Long {
        return -1L
    }

}

private fun List<Point>.toPath(area: Area) = toList(area) - this

private fun toList(area: Area) = buildList {
    for (y in area.yRange) for (x in area.xRange) add(Point(x, y))
}

