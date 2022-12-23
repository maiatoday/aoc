package days

import util.*

typealias Day23ReturnType = Int
typealias Day23InputType = List<String>

typealias ElfMap = Set<Point>

object Day23 : Day<Day23ReturnType, Day23InputType> {
    override val number: Int = 23
    override val expectedPart1Test: Day23ReturnType = 110
    override val expectedPart2Test: Day23ReturnType = 20
    override var useTestData = true
    private const val debug = false

    override fun part1(input: Day23InputType): Day23ReturnType {
        var elfMap: ElfMap = input.listFromGrid("#").toSet()
        val elves = elfMap.mapIndexed { i, it -> Elf(i, it.x, it.y) }
        var direction = RoughCompass.NORTH
        repeat(10) { round ->
            elfMap = elfMap.doRound(elves, direction)
            direction = direction.next()
        }
        val dimensions = elfMap.smallestSpace()
        return dimensions.first * dimensions.second - elves.size
    }

    override fun part2(input: Day23InputType): Day23ReturnType {
        var elfMap: ElfMap = input.listFromGrid("#").toSet()
        val elves = elfMap.mapIndexed { i, it -> Elf(i, it.x, it.y) }
        var direction = RoughCompass.NORTH
        var round = 0
        var moving = true
        while (moving) {
            round++
            elfMap = elfMap.doRound(elves, direction)
            direction = direction.next()
            moving = elves.any { it.move }
        }
        println("Stopped at round $round")
        return round
    }

    private fun ElfMap.doRound(
        elves: List<Elf>,
        direction: RoughCompass
    ): ElfMap {
        val allProposedList = elves.mapNotNull { e -> e.half1(direction, this) }
        val allProposed = allProposedList.groupingBy { it }.eachCount()
        elves.forEach { e -> e.half2(allProposed) }
        return elves.map { e -> e.toPoint() }.toSet()
    }

    //<-----------------------------------------
    data class Elf(val id: Int, var x: Int, var y: Int, var move: Boolean = false) {
        private var proposed: Point? = null

        fun half1(direction: RoughCompass, map: ElfMap): Point? {
            move = false
            val spot = Point(x, y)
            val neighbours = spot.roseNeighbours().toSet()
            var d = direction
            var loopCount = 0
            proposed = if ((neighbours intersect map).isEmpty()) {
                null // do nothing there is enough space
            } else {
                var open: Point? = null
                do {
                    val checkPoints = spot.check(d)
                    if ((checkPoints intersect map).isEmpty()) {
                        open = spot.go(d)
                    } else {
                        d = d.next()
                        if (d == direction) break
                    }
                    loopCount++
                } while (open == null)
                open
            }
            return proposed
        }

        fun half2(allProposed: Map<Point, Int>) {
            proposed?.let {
                val proposedCount = allProposed[proposed] ?: 0
                if (proposedCount == 1) {
                    // we can move
                    move = true
                    x = it.x
                    y = it.y
                }
            }
        }
    }

    enum class RoughCompass { NORTH, SOUTH, WEST, EAST }

    fun RoughCompass.next() = RoughCompass.values()[(ordinal + 1).mod(4)]

    private fun Elf.toPoint(): Point = Point(x, y)

    private fun ElfMap.smallestSpace(): Pair<Int, Int> {
        val xMin = minOf { it.x }
        val xMax = maxOf { it.x }
        val yMin = minOf { it.y }
        val yMax = maxOf { it.y }
        return ((xMax - xMin) + 1 to (yMax - yMin) + 1)
    }

    private fun ElfMap.show(title: String = "start") {
        // if (debug) {
        println("== End of Round $title ==")
        this.toList().debug()
        //  }
    }

    fun Point.check(direction: RoughCompass): List<Point> = when (direction) {
        RoughCompass.NORTH -> listOf(Point(x - 1, y - 1), Point(x, y - 1), Point(x + 1, y - 1))
        RoughCompass.SOUTH -> listOf(Point(x - 1, y + 1), Point(x, y + 1), Point(x + 1, y + 1))
        RoughCompass.WEST -> listOf(Point(x - 1, y + 1), Point(x - 1, y), Point(x - 1, y - 1))
        RoughCompass.EAST -> listOf(Point(x + 1, y + 1), Point(x + 1, y), Point(x + 1, y - 1))
    }

    fun Point.go(direction: RoughCompass, step: Int = 1): Point = when (direction) {
        RoughCompass.NORTH -> Point(x, y - step)
        RoughCompass.SOUTH -> Point(x, y + step)
        RoughCompass.WEST -> Point(x - step, y)
        RoughCompass.EAST -> Point(x + step, y)
    }

}

