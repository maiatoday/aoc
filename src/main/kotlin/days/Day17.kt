package days

import util.Point
import kotlin.math.min

typealias Day17ReturnType = Int
typealias Day17InputType = String
typealias RockColumn = MutableList<Boolean>

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
        val cave: List<RockColumn> = buildList {
            repeat(CAVE_WIDTH) {
                this.add(mutableListOf())
            }
        }
        var turn = 0
        var jetIndex = 0
        repeat(10) {
            if (debug) println("turn ***** $turn")
            val height = cave.highestRock()
            val rock = when (turn % 5) {
                0 -> Flat(height)
                1 -> Plus(height)
                2 -> Ell(height)
                3 -> Line(height)
                4 -> Block(height)
                else -> error("Oops missing block type")
            }
            println("Starting rock ~~~~~${rock.name}")

            while (rock.canMove) {
                val jet = jets[jetIndex]
                val jetstring = if (jet > 0) ">" else "<"
                println("~~~~~~~$jet  $jetstring")
                rock.jet(jet, cave)
                jetIndex = (jetIndex + 1) % jets.size
                rock.down(cave)
                if (debug) println("down ~~~~~$rock")
            }
            turn++
            cave.debug()
        }
        return cave.highestRock()
    }

    override fun part2(input: Day17InputType): Day17ReturnType {
        return expectedPart2Test
    }

    //----------------------------------------------------------

    private fun List<RockColumn>.debug() {
        val highest = this.highestRock()
        println("========== highest rock = $highest ======")
        for (y in highest - 1 downTo 0) {
            print("|")
            for (ci in this) {
                if (ci.size > y) {
                    if (ci[y]) print("#") else print(".")
                } else {
                    print("-")
                }

            }
            println("|")
        }
    }

    private fun List<RockColumn>.highestRock() =
        this[0].size // can be re placed by a special case of highestRockInRange


    enum class ShapesType { FLAT, PLUS, ELL, LINE, BLOCK }

    sealed class Shape(height: Int) {
        abstract val name: String
        abstract var canMove: Boolean
        abstract var position: Point
        abstract val shapePattern: Map<Point, Boolean>
        abstract val width: Int
        abstract val height: Int
        val start = Point(2, 3 + height)

        fun jet(direction: Int, cave: List<RockColumn>) {
            val newX = position.x + direction
            if (newX in 0..CAVE_WIDTH - width) {
                val collision = if (direction > 0) {
                    checkCollision(width - 1, cave, newX + width - 1)
                } else {
                    checkCollision(0, cave, newX)
                }
                if (!collision) position = position.copy(x = newX)
            }
        }

        private fun checkCollision(shapeEdge: Int, cave: List<RockColumn>, columnIndex: Int): Boolean {

            if (position.y >= cave[columnIndex].size) return false
            val shapePoints = (0..height).map { Point(shapeEdge, it) }
            val shapeValues = shapePoints.map { shapePattern[it] }
            val columnYIndices = position.y..min(position.y + height, cave[columnIndex].size - 1)
            val columnYValues = columnYIndices.map { cave[columnIndex][it] }
            val collision =
                columnYValues.zip(shapeValues).none { it.first && it.second ?: false }
            return collision
        }

        fun down(cave: List<RockColumn>) {
            val columnRange = position.x until position.x + width
            val newY = position.y - 1
            var localCanMove = true
            for (ci in columnRange) {
                if (newY == -1) {
                    localCanMove = false  //floor
                } else if (newY <= cave[ci].size - 1) {
                    if (shapePattern.isFilled(ci - columnRange.first, 0) && cave[ci][newY]) {
                        // we are on the edge and we can't move
                        localCanMove = false
                    }
                }

            }
            if (localCanMove) {
                position = position.copy(y = newY)
                canMove = true
            } else {
                canMove = false
                // TODO update cave...
                val numRowsToAdd: Int = height-(cave[0].size-position.y)
                repeat(numRowsToAdd) {
                    for (x in 0 until CAVE_WIDTH) {
                        cave[x].add(false)
                    }
                }
                for (y in 0 until height) {
                    for (x in 0 until CAVE_WIDTH) {
                        cave[x][position.y + y] = x >= position.x &&
                                x < position.x + width &&
                                shapePattern[Point(x - position.x, y)] == true
                    }
                }
            }
        }

        override fun toString(): String =
            "Shape $name - position $position canMove $canMove"

    }

    private fun Map<Point, Boolean>.isFilled(x: Int, y: Int): Boolean = this[Point(x, y)] ?: false

    class Flat(height: Int) : Shape(height) {
        override val name: String = "Flat"
        override var canMove: Boolean = true
        override var position: Point = start
        override val shapePattern: Map<Point, Boolean> = mapOf(
            Point(0, 0) to true,
            Point(1, 0) to true,
            Point(2, 0) to true,
            Point(3, 0) to true,
        )
        override val width = 4
        override val height = 1

    }

    class Plus(height: Int) : Shape(height) {
        override val name: String = "Plus"
        override var canMove: Boolean = true
        override var position: Point = start
        override val shapePattern: Map<Point, Boolean> = mapOf(
            Point(0, 0) to false,
            Point(1, 0) to true,
            Point(2, 0) to false,
            Point(0, 1) to true,
            Point(1, 1) to true,
            Point(2, 1) to true,
            Point(0, 2) to false,
            Point(1, 2) to true,
            Point(2, 2) to false,
        )
        override val width = 3
        override val height = 3

    }

    class Ell(height: Int) : Shape(height) {
        override val name: String = "Ell"
        override var canMove: Boolean = true
        override var position: Point = start
        override val shapePattern: Map<Point, Boolean> = mapOf(
            Point(0, 0) to true,
            Point(1, 0) to true,
            Point(2, 0) to true,
            Point(0, 1) to false,
            Point(1, 1) to false,
            Point(2, 1) to true,
            Point(0, 2) to false,
            Point(1, 2) to false,
            Point(2, 2) to true,
        )
        override val width = 3
        override val height = 3

    }

    class Line(height: Int) : Shape(height) {
        override val name: String = "Line"
        override var canMove: Boolean = true
        override var position: Point = start
        override val shapePattern: Map<Point, Boolean> = mapOf(
            Point(0, 0) to true,
            Point(0, 1) to true,
            Point(0, 2) to true,
            Point(0, 3) to true,
        )
        override val width = 1
        override val height = 4

    }

    class Block(height: Int) : Shape(height) {
        override val name: String = "Block"
        override var canMove: Boolean = true
        override var position: Point = start
        override val shapePattern: Map<Point, Boolean> = mapOf(
            Point(0, 0) to true,
            Point(1, 0) to true,
            Point(0, 1) to true,
            Point(1, 1) to true,
        )
        override val width = 2
        override val height = 2
    }

}