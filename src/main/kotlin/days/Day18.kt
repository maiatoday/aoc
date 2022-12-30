package days

import util.Point3
import util.toList
import util.toPoint3
import kotlin.math.abs

typealias Day18ReturnType = Int
typealias Day18InputType = List<String>

object Day18 : Day<Day18ReturnType, Day18InputType> {
    override val number: Int = 18
    override val expectedPart1Test: Day18ReturnType = 64
    override val expectedPart2Test: Day18ReturnType = 58
    override var useTestData = true
    override val debug = false

    override fun part1(input: Day18InputType): Day18ReturnType {
        val cubes = input.map { it.toPoint3() }
        return cubes.sumOf { p -> 6 - cubes.count { it.shareSides(p) } }
    }

    override fun part2(input: Day18InputType): Day18ReturnType {
        return expectedPart2Test
    }

    //-----------------------------------------------------------
    fun Point3.shareSides(other: Point3): Boolean {
        val pairs = this.toList().zip(other.toList())
        val diffCount = pairs.count { abs(it.first - it.second) == 1 }
        val sameCount = pairs.count { it.first == it.second }
        return (diffCount == 1) && (sameCount == 2)
    }
    //-----------------------------------------------------------
}