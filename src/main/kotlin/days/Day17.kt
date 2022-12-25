package days

import util.Point
import kotlin.math.min

typealias Day17ReturnType = Long
typealias Day17InputType = String

object Day17 : Day<Day17ReturnType, Day17InputType> {
    override val number: Int = 17
    override val expectedPart1Test: Day17ReturnType = 3068
    override val expectedPart2Test: Day17ReturnType = 1514285714288L
    override var useTestData = true

    const val CAVE_WIDTH = 7
    const val debug = false

    override fun part1(input: Day17InputType): Day17ReturnType {
        val jets = input.toJets()
        val cave: MutableList<String> = mutableListOf()
        var turn = 0L
        var jetIndex = 0
        repeat(2022) {
            jetIndex = dropOneRock(turn, jetIndex, jets, cave)
            turn++
        }
        return cave.highestRock().toLong()
    }

    data class State(
        val uniqueCaveState: List<Int>,
        val blockIndex: Int, //blockIndex = turn % 5
        val jetIndex: Int // jetIndex we get back from how many jets were used in the drop
    )

    override fun part2(input: Day17InputType): Day17ReturnType {

        val jets = input.toJets()
        val cave: MutableList<String> = mutableListOf()
        val allTheTurns: Long = 1000000000000

        val seenCaveState = mutableMapOf<State, Pair<Long, Int>>()
        var repeatState: State? = null

        var turn = 0L
        var jetIndex = 0
        while (repeatState == null) {
            jetIndex = dropOneRock(turn, jetIndex, jets, cave)
            val state = cave.getState(turn, jetIndex)
            if (state in seenCaveState) {
                repeatState = state
            } else {
                seenCaveState[state] = (turn to cave.highestRock())
                turn++
            }
        }
        // turn time forward
        println("Before repeat cave.highest, ${cave.highestRock()}")
        println("turn $turn  jetIndex $jetIndex")
        println("repeat state $repeatState")
        val (turnAtRepeatStart, heightAtRepeatStart) = seenCaveState.getValue(repeatState)
        println("turn at repeatState $turnAtRepeatStart, height at repeatState $heightAtRepeatStart")
        val loopHeight = cave.highestRock() - heightAtRepeatStart // height of rows added just for the repeat
        val loopTurns = turn - turnAtRepeatStart
        val remainingTurns = ((allTheTurns  - turnAtRepeatStart) % loopTurns).toInt()

        turn = allTheTurns - remainingTurns
        jetIndex = repeatState.jetIndex
        repeat(remainingTurns) {
            jetIndex = dropOneRock(turn, jetIndex, jets, cave)
            turn++
        }

        println("=========== after last loops")
        val repeatsSkipped =
            (allTheTurns / loopTurns)-1 // minus one because we already did one loop to get the repeat pattern
        println("cave.highest, ${cave.highestRock()}     repeatsSkipped:$repeatsSkipped     loopHeight:$loopHeight")
        val calculatedSize = cave.highestRock() + (repeatsSkipped * loopHeight)

        println("difference ${1514285714288L - calculatedSize}")

        return calculatedSize
    }

    private fun dropOneRock(
        turn: Long,
        jetIndex: Int,
        jets: List<Int>,
        cave: MutableList<String>
    ): Int {
        val height = cave.highestRock()
        val rock = makeRock(turn, height)
        return dropRock(rock, jets, jetIndex, cave)
    }

    private fun dropRock(
        rock: Shape,
        jets: List<Int>,
        jetIndex: Int,
        cave: MutableList<String>
    ): Int {
        var ji = jetIndex
        while (rock.canMove) {
            val jet = jets[ji]
            rock.jet(jet, cave)
            rock.down(cave)
            ji = (ji + 1) % jets.size
            if (debug) println("down ~~~~~$rock")
        }
        return ji
    }

    private fun makeRock(turn: Long, height: Int) = when ((turn % 5).toInt()) {
        0 -> Flat(height)
        1 -> Plus(height)
        2 -> Ell(height)
        3 -> Line(height)
        4 -> Block(height)
        else -> error("Oops missing block type")
    }

    private fun List<String>.getState(turn: Long, jetIndex: Int): State {
        val listy: List<Int> = buildList {
            for (i in 0 until CAVE_WIDTH) {
                var count = 0
                var stillEmpty = true
                while (stillEmpty) {
                    val cc = this@getState.size - 1 - count
                    if (cc < 0) {
                        stillEmpty = false
                        add(count)
                    } else if (this@getState[cc][i] == '#') {
                        stillEmpty = false
                        add(count)
                    } else {
                        count++
                    }
                }
            }
        }
        return State(listy, (turn % 5).toInt(), jetIndex)
    }

    //----------------------------------------------------------
    private fun String.toJets() = this.map { c ->
        when (c) {
            '<' -> -1
            '>' -> 1
            else -> error("oops $c")
        }
    }

    private fun List<String>.highestRock() = this.size

    private fun getCaveRanges(
        caveYRange: IntRange,
        caveXRange: IntRange
    ) = caveYRange.flatMap { i -> caveXRange.map { j -> i to j } }

    sealed class Shape(height: Int, private val name: String, private val shape: List<String>) {
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

    class Flat(height: Int) : Shape(height, "Flat", listOf("####"))

    class Plus(height: Int) : Shape(height, "Plus", listOf("~#~", "###", "~#~"))

    class Ell(height: Int) : Shape(height, "Ell", listOf("###", "~~#", "~~#"))

    class Line(height: Int) : Shape(height, "Line", listOf("#", "#", "#", "#"))

    class Block(height: Int) : Shape(height, "Block", listOf("##", "##"))

    //----------------------------------------------------------

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