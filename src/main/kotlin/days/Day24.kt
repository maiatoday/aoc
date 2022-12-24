package days

import days.Day24.debug
import days.Day24.occupiedSet
import days.Day24.toMap
import util.Point
import util.boundaries
import util.debug

typealias Day24ReturnType = Int
typealias Day24InputType = List<String>

object Day24 : Day<Day24ReturnType, Day24InputType> {
    override val number: Int = 24
    override val expectedPart1Test: Day24ReturnType = -1
    override val expectedPart2Test: Day24ReturnType = -1
    override var useTestData = true
    var mapWidth: Int = -1
    var mapHeight: Int = -1
    val blizzardTypes = setOf('v', '^', '>', '<')
    override fun part1(input: Day24InputType): Day24ReturnType {
        mapWidth = input[0].length - 2 // -2 to cut off walls
        mapHeight = input.size - 2 // -2 to cut off walls
        val blizzardList = input.flatMapIndexed { y, s -> s.mapIndexed { x, c -> c.toBlizzard(x-1, y-1) } }.filterNotNull() //  -1 because not counting walls
        blizzardList.debug()
        repeat(5) {
            blizzardList.forEach { it.tick() }
            println("After minute ${it+1}")
            blizzardList.debug()

        }
        return expectedPart1Test
    }

    override fun part2(input: Day24InputType): Day24ReturnType {
        return expectedPart2Test
    }
    //>--------------------------------------------------

    fun Char.toBlizzard(x: Int, y: Int): Blizzard? = if (this in blizzardTypes) Blizzard(this, Point(x, y)) else null

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

    private fun List<Blizzard>.occupiedSet(): Set<Point> = this.map { b -> b.position }.toSet()

    private fun List<Blizzard>.toMap(): Map<Point, Blizzard> = buildMap {
        this@toMap.forEach {
            put(it.position, it)
        }
    }

    fun List<Blizzard>.debug() {
        val points = this.occupiedSet().toList()
        val boundaries = points.boundaries()
        val map = this.toMap()
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


