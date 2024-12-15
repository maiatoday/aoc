package days

import util.*

object Day15 : Day<Long, List<String>> {
    override val number: Int = 15
    override val expectedPart1Test: Long = 10092L //10092 2028L
    override val expectedPart2Test: Long = 9021L
    override var useTestData = true
    override val debug = false

    override fun part1(input: List<String>): Long {
        val (moves, storageMap) = readMapMoves(input)
        val boxes = determineBoxPosition(moves, storageMap)
        val answer = boxes.sumOf { it.y * 100 + it.x }
        return answer.toLong()
    }

    private fun determineBoxPosition(moves: List<Move>, storageMap: StorageMap): List<Point> {
        var currentBoxPositions = storageMap.boxes
        var currentRobotPosition = storageMap.start
        val movesToMake = ArrayDeque<Pair<Point, Move>>()
        for (m in moves) {
            debug(currentRobotPosition, m, currentBoxPositions, storageMap.walls)
            movesToMake.add(currentRobotPosition to m)
            while (movesToMake.isNotEmpty()) {
                val (pp, m) = movesToMake.removeLast()
                val nextStep = pp + m.step
                if (canMove(nextStep, currentBoxPositions, storageMap.walls)) {
                    if (pp == currentRobotPosition) {
                        // it is a robot
                        currentRobotPosition = nextStep
                    } else if (pp in currentBoxPositions) {
                        // it is a box
                        currentBoxPositions = currentBoxPositions - pp + nextStep
                    }
                } else if (nextStep in storageMap.walls) {
                    continue
                } else if (nextStep in currentBoxPositions) {
                    // couldn't do the original move so put it back on the stack only if there is a space after all boxes
                    val listToAdd = mutableListOf(nextStep)
                    var andNext = nextStep + m.step
                    while (andNext in currentBoxPositions) {
                        listToAdd.add(andNext)
                        andNext = andNext + m.step
                    }
                    if (andNext in storageMap.walls) continue
                    else {
                        movesToMake.addLast(pp to m)
                        listToAdd.map { it to m }.forEach { movesToMake.addLast(it) }
                    }
                }
            }
        }
        return currentBoxPositions
    }

    fun canMove(p: Point, boxes: List<Point>, walls: List<Point>): Boolean = (p !in boxes && p !in walls)

    fun debug(robot: Point, m: Move, boxes: List<Point>, walls: List<Point>) {
        val boundaries = walls.boundaries()
        if (debug) {
            println("=========== $m =========")
            for (y in boundaries.second) {
                for (x in boundaries.first) {
                    when {
                        Point(x, y) == robot -> print("@")
                        Point(x, y) in walls -> print("#")
                        Point(x, y) in boxes -> print("O")
                        else -> print(".")
                    }
                }
                println()
            }

        }
    }

    private fun readMapMoves(input: List<String>): Pair<List<Move>, StorageMap> {
        val (a, b) = input.splitByBlankLine()
        val moves = b.joinToString("").toCharArray().map { Move(it) }
        val walls = a.listFromGrid("#")
        val robot = a.listFromGrid("@").first()
        val boxes = a.listFromGrid("O")
        return moves to StorageMap(robot, walls, boxes)
    }

    data class StorageMap(val start: Point, val walls: List<Point>, val boxes: List<Point>)

    fun Move(c: Char): Move =
        when (c) {
            '^' -> Move.UP
            '>' -> Move.RIGHT
            'v' -> Move.DOWN
            '<' -> Move.LEFT
            else -> error("Oops bad move $c")
        }

    enum class Move(val step: Point) {
        UP(Point(0, -1)),
        RIGHT(Point(1, 0)),
        DOWN(Point(0, 1)),
        LEFT(Point(-1, 0))
    }

    override fun part2(input: List<String>): Long {
        val (moves, storageMap) = readMapMovesXL(input)
        val boxes = determineBoxPositionXL(moves, storageMap)
//        val answer = boxes.sumOf { it.y * 100 + it.x }
//        return answer.toLong()
        return -1L
    }

    private fun readMapMovesXL(input: List<String>): Pair<List<Move>, StorageMapXL> {
        val (oldA, b) = input.splitByBlankLine()
        val moves = b.joinToString("").toCharArray().map { Move(it) }
        val a = superSize(oldA)
        val walls = a.listFromGrid("#")
        val robot = a.listFromGrid("@").first()
        val boxesLeft = a.listFromGrid("[")
        val boxesRight = a.listFromGrid("]")
        val boxes = boxesLeft zip boxesRight
        return moves to StorageMapXL(robot, walls, boxes)
    }

    fun superSize(input: List<String>): List<String> =
        input.map {
            it.replace("#", "##")
            it.replace("O", "[]")
            it.replace(".", "..")
            it.replace(".", "..")
            it.replace("@", "@.")
        }

    private fun determineBoxPositionXL(moves: List<Move>, storageMap: StorageMapXL): List<BigBox> {
        var currentBoxPositions = storageMap.boxes
        var currentRobotPosition = storageMap.start
        val movesToMake = ArrayDeque<Pair<Point, Move>>()
        for (m in moves) {
            debugXL(currentRobotPosition, m, currentBoxPositions, storageMap.walls)
            movesToMake.add(currentRobotPosition to m)
//            while (movesToMake.isNotEmpty()) {
//                val (pp, m) = movesToMake.removeLast()
//                val nextStep = pp + m.step
//                if (canMoveXL(nextStep, currentBoxPositions, storageMap.walls)) {
//                    if (pp == currentRobotPosition) {
//                        // it is a robot
//                        currentRobotPosition = nextStep
//                    } else if (pp in currentBoxPositions) {
//                        // it is a box
//                        currentBoxPositions = currentBoxPositions - pp + nextStep
//                    }
//                } else if (nextStep in storageMap.walls) {
//                    continue
//                } else if (nextStep in currentBoxPositions) {
//                    // couldn't do the original move so put it back on the stack only if there is a space after all boxes
//                    val listToAdd = mutableListOf(nextStep)
//                    var andNext = nextStep + m.step
//                    while (andNext in currentBoxPositions) {
//                        listToAdd.add(andNext)
//                        andNext = andNext + m.step
//                    }
//                    if (andNext in storageMap.walls) continue
//                    else {
//                        movesToMake.addLast(pp to m)
//                        listToAdd.map { it to m }.forEach { movesToMake.addLast(it) }
//                    }
//                }
//            }
        }
        return currentBoxPositions
    }

    data class StorageMapXL(val start: Point, val walls: List<Point>, val boxes: List<BigBox>)

    fun canMoveXL(p: Point, boxes: List<BigBox>, walls: List<Point>): Boolean = (!boxes.contains(p) && p !in walls)

    fun debugXL(robot: Point, m: Move, boxes: List<BigBox>, walls: List<Point>) {
        val boundaries = walls.boundaries()
        if (debug) {
            println("=========== $m =========")
            for (y in boundaries.second) {
                for (x in boundaries.first) {
                    when {
                        Point(x, y) == robot -> print("@")
                        Point(x, y) in walls -> print("#")
                        Point(x, y) in boxes.map { it.first} -> print("[")
                        Point(x, y) in boxes.map { it.second} -> print("]")
                        else -> print(".")
                    }
                }
                println()
            }

        }
    }

    fun List<BigBox>.contains(p: Point) = p in this.map { it.first } || p in this.map { it.second }
}

typealias BigBox = Pair<Point, Point>

