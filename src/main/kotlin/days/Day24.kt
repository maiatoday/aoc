package days

import util.Point
import util.boundaries
import util.neighbours
import java.util.*
import kotlin.math.abs

typealias Day24ReturnType = Int
typealias Day24InputType = List<String>

object Day24 : Day<Day24ReturnType, Day24InputType> {
    override val number: Int = 24
    override val expectedPart1Test: Day24ReturnType = 18
    override val expectedPart2Test: Day24ReturnType = -1
    override var useTestData = true
    override val debug = false

    private var mapWidth: Int = -1
    private var mapHeight: Int = -1
    private val blizzardTypes = setOf('v', '^', '>', '<')
    override fun part1(input: Day24InputType): Day24ReturnType {
        mapWidth = input[0].length - 2 // -2 to cut off walls
        mapHeight = input.size - 2 // -2 to cut off walls
        val blizzardList = input.toBlizzardList()
        blizzardList.tick()  //one minute so that we can   start at 0,0 and the gap opens
        val minutes = findShortestPath(Point(0, 0), Point(mapWidth - 1, mapHeight - 1), blizzardList, 1)
        return minutes
    }

    override fun part2(input: Day24InputType): Day24ReturnType {
        return expectedPart2Test
    }
    //>--------------------------------------------------

    private fun List<String>.toBlizzardList(): List<Blizzard> =
        this.flatMapIndexed { y, s -> s.mapIndexed { x, c -> c.toBlizzard(x - 1, y - 1) } }
            .filterNotNull() //  -1 because not counting walls

    private fun Char.toBlizzard(x: Int, y: Int): Blizzard? = if (this in blizzardTypes) Blizzard(this, Point(x, y)) else null

    data class Blizzard(val direction: Char, val start: Point) {
        var position: Point = start

        fun tick() {
            position = position.blow(direction)
        }
    }

    private fun Point.blow(direction: Char): Point =
        when (direction) {
            '>' -> Point((x + 1).mod(mapWidth), y)
            '<' -> Point((x - 1).mod(mapWidth), y)
            'v' -> Point(x, (y + 1).mod(mapHeight))
            '^' -> Point(x, (y - 1).mod(mapHeight))
            else -> error("oops $direction")
        }

    private fun List<Blizzard>.tick(): Unit = this.forEach { it.tick() }

    private fun List<Blizzard>.occupiedSet(): Set<Point> = this.map { b -> b.position }.toSet()

    private fun List<Blizzard>.toMap(): Map<Point, Blizzard> = buildMap {
        this@toMap.forEach {
            put(it.position, it)
        }
    }

    class BlizzardState(private val blizzards: List<Blizzard>) {
        private val cache = mutableMapOf<Int, Set<Point>>()
        private var lastTick = 1
        fun occupiedAt(tick: Int): Set<Point> {
            if (tick !in cache) {
                for (t in lastTick..tick) {
                    blizzards.tick()
                    cache[t] = blizzards.occupiedSet()
                }
                lastTick = tick
            }
            return cache[tick] ?: error("Missing blizzard")
        }
    }

    private fun Point.validNeighbours(occupied: Set<Point>): List<Point> {
        val neighbours = this.neighbours(
            maxY = mapHeight,
            maxX = mapWidth,
            diagonal = false,
            includeSelf = true,
        )
        // add some preference to down, right and self, self lets you wait some compareBy sort
        return neighbours.filter { it !in occupied }
    }

    // the recursive  version but it is tricky to debug
    private fun search(tick: Int, position: Point, blizzardState: BlizzardState, end: Point): Int {
        var t = tick
        if (position == end) return t
        t++
        val nt = mutableListOf<Int>()
        val neighbours = position.validNeighbours(blizzardState.occupiedAt(t))
        for (n in neighbours) {
            val tt = search(t, n, blizzardState, end)
            if (tt != -1) nt.add(tt)
        }
        if (nt.isEmpty()) return -1
        return nt.min()
    }

    private fun findShortestPath(
        start: Point,
        end: Point,
        blizzardList: List<Blizzard>,
        startTime: Int
    ): Day24ReturnType {
        val blizzardState = BlizzardState(blizzardList)
        val visited: MutableSet<IndexedValue<Point>> = mutableSetOf()
        visited.add(IndexedValue(startTime, start))
        val frontier: ArrayDeque<IndexedValue<Point>> = ArrayDeque()
//        val frontier =  PriorityQueue(compareBy(IndexedValue<IndexedValue<Point>>::index))
//        frontier.add(IndexedValue(0, IndexedValue(startTime, start)))
        frontier.add(IndexedValue(startTime, start))

        while (frontier.isNotEmpty()) {
//            println("Frontier $frontier")
//            val (time, current) = frontier.remove().value
            val (time, current) = frontier.removeLast()
            if (current == end) return time
            val neighbours = current.validNeighbours(blizzardState.occupiedAt(time+1))
            for (n in neighbours) {
                // TIL the add method on a set will return true if it adds it. I can use this instead of checking if the set contains the value
                if (visited.add(IndexedValue(time+1, n)))
//                    frontier.add(IndexedValue(time+1,IndexedValue(time+1, n)))
                    frontier.add(IndexedValue(time+1, n))
            }

        }
        return error ("I am wandering around in a snowstorm")
    }
//>--------------------------------------------------

    fun List<Blizzard>.debug() {
        val points = this.occupiedSet().toList()
        val boundaries = points.boundaries()
        val map = this.toMap()
        log {
            for (y in boundaries.second) {
                for (x in boundaries.first) {
                    if (Point(x, y) in points) {
                        print(map[Point(x, y)]?.direction)
                    } else print('.')
                }
                println()
            }
        }
    }
}

