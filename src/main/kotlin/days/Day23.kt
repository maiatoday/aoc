package days

import util.*

typealias Day23ReturnType = Int
typealias Day23InputType = List<String>

typealias ElfMap = Set<Point>

object Day23 : Day<Day23ReturnType, Day23InputType> {
    override val number: Int = 23
    override val expectedPart1Test: Day23ReturnType = 110
    override val expectedPart2Test: Day23ReturnType = -1
    override var useTestData = true
    private const val debug = false

    override fun part1(input: Day23InputType): Day23ReturnType {
        var elfMap: ElfMap = input.listFromGrid("#").toSet()
        val elves = elfMap.mapIndexed { i, it -> Elf(i, it.x, it.y) }.also { if (debug) println("$it ") }
        elfMap.show()
        var direction = RoughCompass.NORTH
        repeat(10) { round ->
            if (debug) println("Direction $direction")
            val allProposedList = elves.mapNotNull { e -> e.half1(direction, elfMap) }
            val allProposed = allProposedList.groupingBy { it }.eachCount()
            elves.forEach { e -> e.half2(allProposed) }
            elfMap = elves.map { e -> e.toPoint() }.toSet()
            elfMap.show((round + 1).toString())
            direction = direction.next()
        }
        val dimensions = elfMap.smallestSpace()
        return dimensions.first * dimensions.second - elves.size
    }

    override fun part2(input: Day23InputType): Day23ReturnType {
        return expectedPart2Test
    }

    //<-----------------------------------------
    data class Elf(val id: Int, var x: Int, var y: Int) {
        private var proposed: Point? = null

        fun half1(direction: RoughCompass, map: ElfMap): Point? {
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
            if (debug) println("$id - ($x, $y) Half1: final direction $d proposed $proposed  loopCount $loopCount")
            return proposed
        }

        fun half2(allProposed: Map<Point, Int>) {
            if (debug) print("$id-($x, $y) Half2: ")
            proposed?.let {
                val proposedCount = allProposed[proposed] ?: 0
                if (proposedCount == 1) {
                    // we can move
                    if (debug) println("can move to $proposed")
                    x = it.x
                    y = it.y
                } else {
                    if (debug) println("can't move count $proposedCount")
                }
            } //?: println("$proposed no move")
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
        if (debug) {
            println("== End of Round $title ==")
            this.toList().debug()
        }
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

