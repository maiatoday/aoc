package days


typealias Day22ReturnType = Int
typealias Day22InputType = List<String>

typealias PasswordMap = List<String>

var  iii = 0
object Day22 : Day<Day22ReturnType, Day22InputType> {
    override val number: Int = 22
    override val expectedPart1Test: Day22ReturnType = 6032
    override val expectedPart2Test: Day22ReturnType = -1
    const val debug = true
    override var useTestData = true
    lateinit var map: List<String>
    lateinit var markedMap: MutableList<String>
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
            state = state(i, map)
//            if (index >= 1253)
//                debug(state)
        }

        debug(state)

        return state.toAnswer()
    }

    override fun part2(input: Day22InputType): Day22ReturnType {
        return expectedPart2Test
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

    data class State(val x: Int, val y: Int, val direction: Direction) {
        operator fun invoke(i: Instruction, map: PasswordMap): State =
            when (i) {
                is Move -> this.move(i.number, map)
                is Turn -> this.copy(direction = direction + i.clockwise)
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
        // TODO check
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
        while (y >= 0 && (x > this[y].length-1 || this[y][x] == ' ')) {
            y--
        }
        return y
    }

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
