package days

import util.readInts
import kotlin.math.abs
import kotlin.math.absoluteValue

object Day02 : Day<Long, List<String>> {
    override val number: Int = 2
    override val expectedPart1Test: Long = 2L
    override val expectedPart2Test: Long = 4L
    override var useTestData = true
    override val debug = false

    override fun part1(input: List<String>): Long =
        input.parse().count {
            isSafe(it)
            //isSafeFunc(it)
        }.toLong()

    override fun part2(input: List<String>): Long {
        val reports = input.parse()
        val safe = reports.count {
            isSafeWithDampBrute(it)
        }
        return safe.toLong()
    }

    fun part1Alt(input: List<String>): Long = -1L
    fun part2Alt(input: List<String>): Long = -1L

    private fun List<String>.parse() = this.map { s -> s.readInts() }

    private fun isSafeWithDampBrute(levels: List<Int>): Boolean {
        val isSafe = isSafe(levels)
        val allSafeList = List(levels.size) { i ->
            val newList = levels.toMutableList().apply { removeAt(i) }
            isSafe(newList)
        } + isSafe
        return allSafeList.any { it }
    }

    private fun isSafe(levels: List<Int>): Boolean {
        val increasing = (levels[1] > levels[0])
        for (i in 1..<levels.size) {
            if (increasing) {
                if (levels[i] <= levels[i - 1]) {
                    return false
                }
            } else if (levels[i] >= levels[i - 1]) {
                return false
            }
            if ((levels[i] - levels[i - 1]).absoluteValue !in 1..3) {
                return false
            }
        }
        return true
    }

    // a functional version that is possibly less readable
    private fun isSafeFunc(levels: List<Int>): Boolean {
        val increasing = (levels[1] > levels[0])
        return levels.zipWithNext().map { (a, b) ->
            (abs(b - a) in 1..3) && increasing == (b > a)
        }.all { it }
    }

}
