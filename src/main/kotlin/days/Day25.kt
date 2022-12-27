package days

import kotlin.math.pow

typealias Day25ReturnType = String
typealias Day25InputType = List<String>

object Day25 : Day<Day25ReturnType, Day25InputType> {
    override val number: Int = 25
    override val expectedPart1Test: Day25ReturnType = "2=-1=0"
    override val expectedPart2Test: Day25ReturnType = "-1"
    override var useTestData = true
    override val debug = false

    override fun part1(input: Day25InputType): Day25ReturnType {
        val bleargh = input.map {
            it.fromSnafuToDec().sum()
        }
        val sum = bleargh.sumOf { it.toLong() }
        return sum.fromDecToSnafu()
    }

    private fun String.fromSnafuToDec() = reversed().mapIndexed() { i, c ->
        when {
            c == '-' -> -1 * (5.0.pow(i))
            c == '=' -> -2 * (5.0.pow(i))
            c.isDigit() -> c.digitToInt() * (5.0.pow(i))
            else -> error("Oops unknown digit")
        }
    }

    private fun Long.fromDecToSnafu(): String {
        var workingNumber = this
        var remainder = 0
        val snafu = mutableListOf<Int>()
        while (workingNumber > 0) {
            remainder = workingNumber.mod(5L).toInt()
            snafu.add(remainder)
            workingNumber /= 5
        }
        snafu.add(0)
        for ((i, s) in snafu.withIndex()) {
            when (s) {
                3 -> {
                    snafu[i] = -2
                    bumpHigher(snafu, i + 1)
                }

                4 -> {
                    snafu[i] = -1
                    bumpHigher(snafu, i + 1)
                }

                else -> {}// the number is ok
            }

        }

        if (snafu.last() == 0) snafu.removeLast()

        val answer = snafu.joinToString("") { n ->
            when (n) {
                -1 -> "-"
                -2 -> "="
                else -> n.toString()
            }
        }.reversed()

        return answer
    }

    private fun bumpHigher(newSnafu: MutableList<Int>, i: Int) {
        when (val nn = newSnafu[i] + 1) {
            5 -> {
                newSnafu[i] = 0
                bumpHigher(newSnafu, i + 1)
            }

            4 -> {
                newSnafu[i] = -1
                bumpHigher(newSnafu, i + 1)
            }

            3 -> {
                newSnafu[i] = -2
                bumpHigher(newSnafu, i + 1)
            }

            else -> newSnafu[i] = nn
        }
    }

    override fun part2(input: Day25InputType): Day25ReturnType {
        return expectedPart2Test
    }
}
