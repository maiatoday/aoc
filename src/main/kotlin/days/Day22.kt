package days


typealias Day22ReturnType = Int
typealias Day22InputType = List<String>

typealias PasswordMap = List<String>

data class Side(val xRange: IntRange, val yRange: IntRange, val map: List<String>)
typealias PasswordCube = List<Side>

var iii = 0

object Day22 : Day<Day22ReturnType, Day22InputType> {
    override val number: Int = 22
    override val expectedPart1Test: Day22ReturnType = 6032
    override val expectedPart2Test: Day22ReturnType = 5031
    const val debug = true
    override var useTestData = true
    lateinit var map: PasswordMap
    private lateinit var markedMap: MutableList<String>
    private lateinit var sides: PasswordCube
    private const val SIDE_LENGTH = 50
    val SIDE_RANGE = 0 until SIDE_LENGTH
    override fun part1(input: Day22InputType): Day22ReturnType {
        val instructions = input.last().toInstructions()
        val last = instructions.last()
        println("last instruction $last")
        map = input.dropLast(2)
        markedMap = map.toMutableList()
        var state = State(map[0].portToLeft(), 0, Direction.RIGHT)
        instructions.forEachIndexed { index, i ->
            iii = index
//            if (index >= 1253)
//                i.debug(index)
            state = state(i, map, false)
//            if (index >= 1253)
//                debug(state)
        }

        debug(state)

        return state.toAnswer()
    }

    override fun part2(input: Day22InputType): Day22ReturnType {

        val instructions = input.last().toInstructions()
        map = input.dropLast(2)
        markedMap = map.toMutableList()
        sides = map.toCube()

        var state = State(x = 0, 0, Direction.RIGHT, side = 0)
        println("Start state $state")
        instructions.forEachIndexed { index, i ->
            iii = index
//            if (index >= 1253)
            i.debug(index)
            state = state(i, map, true, sides)
            println("$state")
//            if (index >= 1253)
//                debug(state)
        }

        println("Instruction completed $iii")
      //  debug(state)

        return state.toAnswer3D()

    }

    //<-----------------------------------------------------------------
    private fun String.toInstructions() = buildList {
        var digitString = ""
        for (c in this@toInstructions) {
            when (c) {
                'R', 'L', 'U', 'D' -> {
                    add(Move(digitString.toInt()))
                    digitString = ""
                    add(c.toTurn())
                }

                else -> digitString += c
            }
        }
        if (digitString.isNotEmpty()) add(Move(digitString.toInt()))
    }


    sealed class Instruction {
        fun debug(index: Int) {
            if (debug) {
                println("Instruction at index $index: $this")
            }
        }
    }

    data class Move(val number: Int) : Instruction()
    data class Turn(val clockwise: Boolean) : Instruction()

    private fun Char.toTurn(): Turn {
        val clockwise = when (this) {
            'R' -> true
            'L' -> false
            else -> error("Oops $this")
        }
        return Turn(clockwise)
    }

    enum class Direction(val score: Int) {
        RIGHT(0), DOWN(1), LEFT(2), UP(3);

        operator fun plus(clockwise: Boolean): Direction =
            if (clockwise) {
                when (this) {
                    RIGHT -> DOWN
                    DOWN -> LEFT
                    LEFT -> UP
                    UP -> RIGHT
                }
            } else {
                when (this) {
                    RIGHT -> UP
                    UP -> LEFT
                    LEFT -> DOWN
                    DOWN -> RIGHT
                }
            }
    }

    data class State(val x: Int, val y: Int, val direction: Direction, val side: Int = 0) {
        operator fun invoke(
            i: Instruction,
            map: PasswordMap,
            onCube: Boolean = false,
            passwordCube: PasswordCube = listOf()
        ): State =
            when (i) {
                is Move -> if (!onCube) this.move(i.number, map) else this.move3D(i.number, passwordCube)
                is Turn -> this.copy(direction = direction + i.clockwise)
            }

        private fun move3D(i: Int, passwordCube: PasswordCube): State {
            var newX = x
            var newY = y
            var xx = newX
            var yy = newY
            var newSideInfo = SideSwapInfo(direction, side, xx, yy)
            var nsInfo = newSideInfo
            repeat(i) {
                when (newSideInfo.direction) {
                    Direction.RIGHT -> {
                        newX += 1
                        if (newX !in SIDE_RANGE) {
                            // new side,  new direction, x y swamp?
                            newSideInfo = swapSides(newSideInfo.side, newSideInfo.direction, newX, newY)
                            newX = newSideInfo.x
                            newY = newSideInfo.y
                        }
                    }

                    Direction.DOWN -> {
                        newY += 1
                        if (newY !in SIDE_RANGE) {
                            // new side,  new direction, x y swamp?
                            newSideInfo = swapSides(newSideInfo.side, newSideInfo.direction, newX, newY)
                            newX = newSideInfo.x
                            newY = newSideInfo.y
                        }
                    }

                    Direction.LEFT -> {
                        newX -= 1
                        if (newX !in SIDE_RANGE) {
                            // new side,  new direction, x y swamp?
                            newSideInfo = swapSides(newSideInfo.side, newSideInfo.direction, newX, newY)
                            newX = newSideInfo.x
                            newY = newSideInfo.y
                        }
                    }

                    Direction.UP -> {
                        newY -= 1
                        if (newY !in SIDE_RANGE) {
                            // new side,  new direction, x y swamp?
                            newSideInfo = swapSides(newSideInfo.side, newSideInfo.direction, newX, newY)
                            newX = newSideInfo.x
                            newY = newSideInfo.y
                        }
                    }
                }
                if (passwordCube[newSideInfo.side].map[newY][newX] == '#') {
                    newX = xx
                    newY = yy
                    newSideInfo = nsInfo
                } else {
                    //backup the good state in case we hit a wall in the next repeat
                    xx = newX
                    yy = newY
                    nsInfo = newSideInfo
                }
            }
            return this.copy(x = newX, y = newY, side = newSideInfo.side, direction = newSideInfo.direction)
        }


        private fun move(i: Int, map: PasswordMap): State {
            var newX = x
            var newY = y
            var xx = newX
            var yy = newY
            repeat(i) {
                when (direction) {
                    Direction.RIGHT -> {
                        newX += 1
                        if (newX == map[y].length) { // no space check end of line
                            newX = map[y].portToLeft()
                        }
                    }

                    Direction.DOWN -> {
                        newY += 1
                        if (newY == map.size || x >= map[newY].length || map[newY][x] == ' ') {
                            newY = map.portToTop(x)
                        }
                    }

                    Direction.LEFT -> {
                        newX -= 1
                        if (newX == -1 || map[y][newX] == ' ') {
                            newX = map[y].portToRight()
                        }
                    }

                    Direction.UP -> {
                        newY -= 1
                        if (newY == -1 || x >= map[newY].length || map[newY][x] == ' ') {
                            newY = map.portToBottom(x)
                        }
                    }
                }

                if (map[newY][newX] == '#') {
                    newX = xx
                    newY = yy
                } else {
                    xx = newX
                    yy = newY
                }
                markTheMap(this.copy(x = newX, y = newY))
            }
            return this.copy(x = newX, y = newY)

        }

        fun toAnswer(): Day22ReturnType = 1000 * (y + 1) + 4 * (x + 1) + direction.score // could have used ordinal

        fun debug(): String =
            when (direction) {
                Direction.RIGHT -> ">"
                Direction.DOWN -> "v"
                Direction.LEFT -> "<"
                Direction.UP -> "^"
            }

        fun toAnswer3D(): Day22ReturnType {
            val yOffset = 50 // hardcoded for side 2
            val xOffset = 50 // hardcoded for side 2
           // 1000 * (y + yOffset + 1) + 4 * (x +xOffset+ 1) + direction.score
            println("State $this")
            return 1000 * (y +yOffset + 1) + 4 * (x +xOffset+ 1) + direction.score
        }

    }

    private fun String.portToLeft(): Int {
        // find a stdlib for this?
        var x = 0
        while (x < this.length && this[x] == ' ') {
            x++
        }
        return x
    }

    private fun String.portToRight(): Int {
        var x = this.length - 1
        while (x >= 0 && this[x] == ' ') {
            x--
        }
        return x
    }

    private fun PasswordMap.portToTop(x: Int): Int {
        var y = 0
        while (y < this.size && this[y][x] == ' ') {
            y++
        }
        return y
    }

    private fun PasswordMap.portToBottom(x: Int): Int {
        var y = this.size - 1
        while (y >= 0 && (x > this[y].length - 1 || this[y][x] == ' ')) {
            y--
        }
        return y
    }

    private fun PasswordMap.toCube(): List<Side> {
        // hard code sigh!

        val side1X = 50..99
        val side1Y = 0..49
        val side2X = 100..149
        val side2Y = 0..49

        val side3X = 50..99
        val side3Y = 50..99

        val side4X = 0..49
        val side4Y = 100..149
        val side5X = 50..99
        val side5Y = 100..149

        val side6X = 0..49
        val side6Y = 150..199
        val xRanges = listOf(side1X, side2X, side3X, side4X, side5X, side6X)
        val yRanges = listOf(side1Y, side2Y, side3Y, side4Y, side5Y, side6Y)
        val ranges = xRanges.zip(yRanges)

        val sides: List<Side> = buildList {
            ranges.forEach {
                val sideMap = this@toCube.filterIndexed { i, _ -> i in it.second }.map { s -> s.substring(it.first) }
                add(Side(it.first, it.second, sideMap))
            }

        }
        return sides
    }

    data class SideSwapInfo(val direction: Direction, val side: Int, val x: Int, val y: Int)

    fun swapSides(side: Int, direction: Direction, x: Int, y: Int): SideSwapInfo =
        when (direction) {
            Direction.RIGHT -> when (side) {
                0 -> SideSwapInfo(direction = Direction.RIGHT, side = 1, 0, y)
                1 -> SideSwapInfo(direction = Direction.LEFT, side = 4, SIDE_LENGTH - 1,SIDE_LENGTH -y)
                2 -> SideSwapInfo(direction = Direction.UP, side = 1, y, SIDE_LENGTH - 1)
                3 -> SideSwapInfo(direction = Direction.RIGHT, side = 4, 0, y)
                4 -> SideSwapInfo(direction = Direction.LEFT, side = 1, SIDE_LENGTH - 1,SIDE_LENGTH -y)
                5 -> SideSwapInfo(direction = Direction.UP, side = 4, y, SIDE_LENGTH - 1)
                else -> error("Oops")
            }

            Direction.DOWN -> when (side) {
                0 -> SideSwapInfo(direction = Direction.DOWN, side = 2, x, 0)
                1 -> SideSwapInfo(direction = Direction.LEFT, side = 2, SIDE_LENGTH - 1, x)
                2 -> SideSwapInfo(direction = Direction.DOWN, side = 4, x, 0)
                3 -> SideSwapInfo(direction = Direction.DOWN, side = 5, x, 0)
                4 -> SideSwapInfo(direction = Direction.LEFT, side = 5, SIDE_LENGTH - 1, x)
                5 -> SideSwapInfo(direction = Direction.DOWN, side = 1, x, 0)
                else -> error("Oops")
            }

            Direction.LEFT -> when (side) {
                0 -> SideSwapInfo(direction = Direction.RIGHT, side = 3, 0, SIDE_LENGTH-1-y)
                1 -> SideSwapInfo(direction = Direction.RIGHT, side = 0, SIDE_LENGTH - 1, y)
                2 -> SideSwapInfo(direction = Direction.DOWN, side = 3, y, 0)
                3 -> SideSwapInfo(direction = Direction.RIGHT, side = 0, 0, SIDE_LENGTH-1-y)
                4 -> SideSwapInfo(direction = Direction.LEFT, side = 3, SIDE_LENGTH - 1, y)
                5 -> SideSwapInfo(direction = Direction.DOWN, side = 0, y, 0)
                else -> error("Oops")
            }

            Direction.UP -> when (side) {
                0 -> SideSwapInfo(direction = Direction.RIGHT, side = 5, 0, x)
                1 -> SideSwapInfo(direction = Direction.UP, side = 5, x, SIDE_LENGTH - 1)
                2 -> SideSwapInfo(direction = Direction.UP, side = 0, x, SIDE_LENGTH - 1)
                3 -> SideSwapInfo(direction = Direction.RIGHT, side = 2, 0, x)
                4 -> SideSwapInfo(direction = Direction.UP, side = 2, x, SIDE_LENGTH - 1)
                5 -> SideSwapInfo(direction = Direction.UP, side = 3, x, SIDE_LENGTH - 1)
                else -> error("Oops")
            }
        }
    //<--------------------------

    private fun debug(s: State) {
        if (debug) {
            println("State: $s")
            println("---------------")
            markTheMap(s)
            markedMap.forEach {
                println(it)
            }
        }
    }

    private fun markTheMap(s: State) {
        val sc = s.debug()
        markedMap.forEachIndexed { i, r ->
            if (i == s.y) markedMap[i] = r.replaceRange(s.x..s.x, sc)
        }
    }


}
