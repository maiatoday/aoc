package days

import util.readInts
import kotlin.math.abs

object Day02 : Day<Long, List<String>> {
    override val number: Int = 2
    override val expectedPart1Test: Long = 2L
    override val expectedPart2Test: Long = 4L
    override var useTestData = true
    override val debug = false

    override fun part1(input: List<String>): Long {
        val reports = input.parse()
        val safe = reports.count {
            it.isSafe().first
        }
        return safe.toLong()
    }

    override fun part2(input: List<String>): Long {
        val reports = input.parse()
        val safe = reports.count {
            it.isSafeWithDamp()
        }
        return safe.toLong()
    }

    fun part1Alt(input: List<String>): Long = -1L
    fun part2Alt(input: List<String>): Long = -1L

    private fun List<String>.parse() = this.map { s -> s.readInts() }
    private fun List<Int>.isSafeWithDamp(problemDampLevel: Int = 1): Boolean {
        val (isSafe, errorIndex) = isSafe()
        return if (!isSafe) {
            val newList = toMutableList()
            newList.removeAt(errorIndex)
            newList.isSafe().first
        } else {
            true
        }
    }

    private fun List<Int>.isSafe(): Pair<Boolean, Int> {
        var isSafe = true
        val increasing = (this[1] > this[0])
        var errorIndex = size
        for (i in 1..<size) {
            if (increasing) {
                if (this[i] <= this[i - 1]) {
                    isSafe = false
                    errorIndex = i
                    break
                }
            } else if (this[i] >= this[i - 1]) {
                isSafe = false
                errorIndex = i
                break
            }
            if (abs(this[i] - this[i - 1]) !in 1..3) {
                isSafe = false
                errorIndex = i
                break
            }

        }
        return isSafe to errorIndex
    }
}
