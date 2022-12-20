package days

import util.Point
import kotlin.math.min

typealias Day17ReturnType = Int
typealias Day17InputType = String

object Day17 : Day<Day17ReturnType, Day17InputType> {
    override val number: Int = 17
    override val expectedPart1Test: Day17ReturnType = 3068
    override val expectedPart2Test: Day17ReturnType = -1 // 1514285714288
    override var useTestData = true

    const val CAVE_WIDTH = 7
    const val debug = false

    override fun part1(input: Day17InputType): Day17ReturnType {
        val jets = input.map { c ->
            when (c) {
                '<' -> -1
                '>' -> 1
                else -> error("oops $c")
            }
        }
        val cave: MutableList<String> = mutableListOf()

        var turn = 0
        var jetIndex = 0
        var fillCount = 0
        repeat(2022) {
            val height = cave.highestRock()
            val rock = when (turn % 5) {
                0 -> Flat(height)
                1 -> Plus(height)
                2 -> Ell(height)
                3 -> Line(height)
                4 -> Block(height)
                else -> error("Oops missing block type")
            }

            while (rock.canMove) {
                val jet = jets[jetIndex]
                val jetDirection = if (jet > 0) ">" else "<"
                if (debug) println("~~~~~~~$jet  $jetDirection")
                rock.jet(jet, cave)
                jetIndex = (jetIndex + 1) % jets.size
                rock.down(cave)
                if (debug) println("down ~~~~~$rock")
            }
//            fillCount += rock.fill
//            val actualFill = cave.sumOf {
//                it.count { c -> c == '#' }
//            }
            turn++
        }

        return cave.highestRock()
    }

    override fun part2(input: Day17InputType): Day17ReturnType {
        return expectedPart2Test
    }

    //----------------------------------------------------------

    private fun List<String>.highestRock() = this.size

    sealed class Shape(height: Int, private val name: String, private val shape: List<String>, val fill: Int) {
        private val start = Point(2, 3 + height)
        var canMove: Boolean = true
        private var position: Point = start
        private val width = shape[0].length
        private val height = shape.size

        fun jet(direction: Int, cave: List<String>) {
            val newX = position.x + direction
            val collision = checkCollision(direction, cave, newX, position.y)
            if (!collision) position = position.copy(x = newX)
            if (debug) println("~~~~~~~~ $position because collision=$collision")
        }

        private fun checkCollision(direction: Int, cave: List<String>, x: Int, y: Int): Boolean {
            // boundary checks
            if (direction == 1 && (x + width > CAVE_WIDTH)) return true //right edge
            if (direction == -1 && x < 0) return true //left  edge
            if (y >= cave.size) return false // still falling through space

            //  compare whole block because of +
            val caveXRange = (x until x + width)
            val caveYRange = y..min(y + height - 1, cave.size - 1)
            val shapeBlocks = shape.flatMap { it.toList() }
            val caveRanges = getCaveRanges(caveYRange, caveXRange)
            val caveBlocks = caveRanges.map { cave[it.first][it.second] }
            val combo = shapeBlocks.zip(caveBlocks)
            return combo.any { it.first == '#' && it.second == '#' }
        }

        fun down(cave: MutableList<String>) {
            val newY = position.y - 1
            val canGoDown = if (newY >= cave.size) {
                // falling through space
                true
            } else if (newY < 0) {
                // hit the floor
                false
            } else {
                val shapeBlocks = shape.flatMap { it.toList() }
                val caveXRange = position.x until position.x + width
                val caveYRange = newY..min(newY + height - 1, cave.size - 1)
                val caveRanges = getCaveRanges(caveYRange, caveXRange)
                val caveBlocks = caveRanges.map { cave[it.first][it.second] }
                val combo = shapeBlocks.zip(caveBlocks)
                combo.none { it.first == '#' && it.second == '#' }
            }

            if (canGoDown) {
                position = position.copy(y = newY)
                canMove = true
            } else {
                canMove = false
                if (position.y <= cave.size - 1) {
                    for ((yOffset, s) in shape.withIndex()) {
                        val y = position.y + yOffset
                        if (y < cave.size) {
                            cave[y] = replaceLine(cave[y], buildLine(s, position.x))
                        } else {
                            cave.add(buildLine(s, position.x))
                        }
                    }
                } else {
                    for (s in shape) {
                        cave.add(buildLine(s, position.x))
                    }
                }
            }
        }

        private fun replaceLine(l: String, s: String): String =
            l.zip(s).map {
                when {
                    (it.first == '~' && it.second == '~') -> '~'
                    (it.first == '~' && it.second == '#') -> '#'
                    (it.first == '#' && it.second == '~') -> '#'
                    else -> error("bad replace undetected collision")
                }
            }.joinToString("")

        private fun concatLine(l: String, s: String, x: Int): String =
            l.substring(0, x) + s + l.substring(x + s.length)

        private fun buildLine(s: String, x: Int): String =
            concatLine("~".repeat(CAVE_WIDTH), s, x)


        override fun toString(): String =
            "Shape $name - position $position canMove $canMove"

    }

    private fun getCaveRanges(
        caveYRange: IntRange,
        caveXRange: IntRange
    ) = caveYRange.flatMap { i -> caveXRange.map { j -> i to j } }

    class Flat(height: Int) : Shape(height, "Flat", listOf("####"), 4)

    class Plus(height: Int) : Shape(height, "Plus", listOf("~#~", "###", "~#~"), 5)

    class Ell(height: Int) : Shape(height, "Ell", listOf("###", "~~#", "~~#"), 5)

    class Line(height: Int) : Shape(height, "Line", listOf("#", "#", "#", "#"), 4)

    class Block(height: Int) : Shape(height, "Block", listOf("##", "##"), 4)

    private fun List<String>.debug() {
        if (debug) {
            val highest = this.highestRock()
            println("========== highest rock = $highest ======")
            for (y in highest - 1 downTo 0) {
                print("|")
                print(this[y])
                println("|")
            }
        }
    }

}