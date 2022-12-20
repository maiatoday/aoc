package days

import util.Point
import kotlin.math.min

typealias Day17ReturnType = Int
typealias Day17InputType = String

object Day17 : Day<Day17ReturnType, Day17InputType> {
    override val number: Int = 17
    override val expectedPart1Test: Day17ReturnType = 3068
    override val expectedPart2Test: Day17ReturnType = -1
    override var useTestData = true

    const val CAVE_WIDTH = 7
    val debug = false

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
            print("turn ***** $turn")
            val height = cave.highestRock()
            val rock = when (turn % 5) {
                0 -> Flat(height)
                1 -> Plus(height)
                2 -> Ell(height)
                3 -> Line(height)
                4 -> Block(height)
                else -> error("Oops missing block type")
            }
            println(" Starting rock ~~~~~${rock.name} ${rock.position}")

            while (rock.canMove) {
                val jet = jets[jetIndex]
                val jetstring = if (jet > 0) ">" else "<"
                if (debug) println("~~~~~~~$jet  $jetstring")
                rock.jet(jet, cave)
                jetIndex = (jetIndex + 1) % jets.size
                rock.down(cave)
                if (debug) println("down ~~~~~$rock")
            }
            fillCount += rock.fill
            val actualFill = cave.sumOf {
                it.count { c -> c == '#' }
            }
            println("Fill count actual $actualFill expected $fillCount")
            turn++
            // cave.debug()
        }

        //   cave.debug2()
        return cave.highestRock()
    }

    override fun part2(input: Day17InputType): Day17ReturnType {
        return expectedPart2Test
    }

    //----------------------------------------------------------

    private fun List<String>.debug2() {
        val highest = this.highestRock()
        println("========== highest rock = $highest ======")
        for (y in highest - 1 downTo 500) {
            print("|")
            print(this[y])
            println("|")
        }
    }

    private fun List<String>.debug() {
        val highest = this.highestRock()
        println("========== highest rock = $highest ======")
        for (y in highest - 1 downTo 0) {
            print("|")
            print(this[y])
            println("|")
        }
    }

    private fun List<String>.highestRock() = this.size

    sealed class Shape(height: Int, val name: String, private val shape: List<String>, val fill: Int) {
        private val start = Point(2, 3 + height)
        var canMove: Boolean = true
        var position: Point = start
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
            val shapeBlocks = shape.map { if (direction == 1) it[width - 1] else it[0] }
            val rowBlocks =
                (y..y + height).mapNotNull {
                    if (it < cave.size) {
                        if (direction == 1) {
                            cave[it].substring(x + width - 1, x + width - 1 + 1).first()
                        } else {
                            cave[it].substring(x, x + 1).first()
                        }
                    } else null
                }
            val combo = shapeBlocks.zip(rowBlocks)
            return combo.any { it.first == '#' && it.second == '#' }
        }

        fun down(cave: MutableList<String>) {
            val newY = position.y - 1
            val canGoDown = if (newY >= cave.size) {
                true
            } else if (newY < 0) {
                false
            } else if (this is Plus && newY < cave.size - 1) {
                // have to check the whole of the block for all the blocks not just only plus
                val secondRow = shape[1]
                val rowBlocks = cave[newY + 1].substring(position.x, position.x + width)
                val combo = secondRow.zip(rowBlocks)
                combo.none { it.first == '#' && it.second == '#' }
            } else if (newY <= cave.size - 1) {
                val shapeBlocks = shape.first()
                val rowBlocks = cave[newY].substring(position.x, position.x + width)
                val combo = shapeBlocks.zip(rowBlocks)
                /// the borkiness is here because of the godforsaken plus TODO check the  second line
                combo.none { it.first == '#' && it.second == '#' }
            } else {
                true
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
                    (it.first == '_' && it.second == '_') -> '_'
                    (it.first == '_' && it.second == '#') -> '#'
                    (it.first == '#' && it.second == '_') -> '#'
                    else -> error("bad replace undetected collision")
                }
            }.joinToString("")

        private fun concatLine(l: String, s: String, x: Int): String =
            l.substring(0, x) + s + l.substring(x + s.length)

        private fun buildLine(s: String, x: Int): String =
            concatLine("_".repeat(CAVE_WIDTH), s, x)


        override fun toString(): String =
            "Shape $name - position $position canMove $canMove"

    }

    class Flat(height: Int) : Shape(height, "Flat", listOf("####"), 4)

    class Plus(height: Int) : Shape(height, "Plus", listOf("_#_", "###", "_#_"), 5)

    class Ell(height: Int) : Shape(height, "Ell", listOf("###", "__#", "__#"), 5)

    class Line(height: Int) : Shape(height, "Line", listOf("#", "#", "#", "#"), 4)

    class Block(height: Int) : Shape(height, "Block", listOf("##", "##"), 4)

}