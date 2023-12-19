package days

import kotlinx.coroutines.currentCoroutineContext
import util.Point

object Day18 : Day<Long, List<String>> {
    override val number: Int = 18
    override val expectedPart1Test: Long = 62L
    override val expectedPart2Test: Long = -1L
    override var useTestData = true
    override val debug = false

    override fun part1(input: List<String>): Long {
        val digPlan = readPlan(input)
        val trench = dig(digPlan)
        draw(trench)
        val area = measure(trench)
        return area.toLong()
    }

    private fun readPlan(input: List<String>) =
            input.map { Instruction(it) }

    private fun dig(digPlan: List<Instruction>) = buildList {
        var current = Point(0, 0)
        for (i in digPlan) {
            for (s in 0..<i.steps) {
                add(current)
                current = i.step(current)
            }
        }
    }

    private fun draw(trench: List<Point>) {
        val minX = trench.minOf { it.x }
        val maxX = trench.maxOf { it.x }
        val minY = trench.minOf { it.y }
        val maxY = trench.maxOf { it.y }
        for (y in minY - 1..maxY + 1) {
            var crossingCount = 0
            var inside = false
            for (x in minX - 1..maxX + 1) {
                val current = Point(x, y)
                val previous = Point(x - 1, y)
                if (previous in trench && current !in trench) crossingCount++
                inside = if (crossingCount % 2 != 0) true else false
                if (current in trench) print("#") else if (inside) print("x") else print(".")
            }
            println(crossingCount)
        }

    }

    private fun measure(trench: List<Point>): Int {
        val minX = trench.minOf { it.x }
        val maxX = trench.maxOf { it.x }
        val minY = trench.minOf { it.y }
        val maxY = trench.maxOf { it.y }
        var count = 0

        for (y in minY - 1..maxY + 1) {
            var inside = false
            var crossingCount = 0
            for (x in minX - 1..maxX + 1) {
                if (inside) print("#") else print(".")
                val current = Point(x, y)
                val previous = Point(x - 1, y)
                if ((current in trench && previous !in trench) || (current !in trench && previous in trench)) crossingCount++
                if (inside) count++
                inside = crossingCount % 2 != 0
            }
            println()
        }
        return count
    }

    data class Instruction(val direction: Direction, val steps: Int, val color: String) {
        fun step(current: Point) =
                when (direction) {
                    Direction.U -> current.copy(y = current.y - 1)
                    Direction.D -> current.copy(y = current.y + 1)
                    Direction.L -> current.copy(x = current.x - 1)
                    Direction.R -> current.copy(x = current.x + 1)
                }
    }

    private fun Instruction(s: String): Instruction {
        val (direction, steps, color) = s.split(' ')
        return Instruction(Direction.valueOf(direction), steps.toInt(), color)
    }

    enum class Direction {
        U, D, L, R;
    }

    override fun part2(input: List<String>): Long {
        return -1L
    }
}
