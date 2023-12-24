package days

import util.readInts
import util.readLongs

object Day24 : Day<Long, List<String>> {
    override val number: Int = 24
    override val expectedPart1Test: Long = -1L
    override val expectedPart2Test: Long = -1L
    override var useTestData = true
    override val debug = false

    val testArea = if (useTestData) 7L..27L else 200000000000000L..400000000000000L

    override fun part1(input: List<String>): Long {
        val hailstones = parseReadings(input)
        val testArea = 7..27
        return -1L
    }

    fun parseReadings(input: List<String>) =
            input.map { HailStone(it) }

    data class Line(val one: Pair<Long, Long>, val two: Pair<Long, Long>)
    data class HailStone(val position: List<Long>, val velocity: List<Int>) {
//        fun lineInTestSpace(space: LongRange): Line {
//
//        }
    }

    //{\displaystyle {\begin{aligned}P_{x}&={\frac {(x_{1}y_{2}-y_{1}x_{2})(x_{3}-x_{4})-(x_{1}-x_{2})(x_{3}y_{4}-y_{3}x_{4})}{(x_{1}-x_{2})(y_{3}-y_{4})-(y_{1}-y_{2})(x_{3}-x_{4})}}\\[4px]P_{y}&={\frac {(x_{1}y_{2}-y_{1}x_{2})(y_{3}-y_{4})-(y_{1}-y_{2})(x_{3}y_{4}-y_{3}x_{4})}{(x_{1}-x_{2})(y_{3}-y_{4})-(y_{1}-y_{2})(x_{3}-x_{4})}}\end{aligned}}}
    // https://en.wikipedia.org/wiki/Line%E2%80%93line_intersection#More_than_two_lines
    fun intersect(first: Line, second: Line): Boolean {
        return false
    }


    fun HailStone(input: String) =
            input.split("@").zipWithNext().first().let { (p, v) -> HailStone(p.readLongs(), v.readInts()) }

    override fun part2(input: List<String>): Long {
        return -1L
    }
}
