package days

import util.readLongs
import kotlin.math.pow

object Day07 : Day<Long, List<String>> {
    override val number: Int = 7
    override val expectedPart1Test: Long = 3749L
    override val expectedPart2Test: Long = -1L //11387L
    override var useTestData = true
    override val debug = false

    override fun part1(input: List<String>): Long {
        val equations = parse(input)
        val answer = equations.filter { it.isOk() }.sumOf { (a, _) -> a }
        return answer
    }

    fun parse(input: List<String>): List<Equation> =
        input.map {
            it.substringBefore(":").toLong() to it.substringAfter(":").readLongs()
        }

    fun Equation.isOk(): Boolean {
        val (a, n) = this
        // based on n.length make combos
        val comboMax = 2.toDouble().pow(n.size.toDouble()).toLong()
        for (j in 0L..comboMax) {// j is the bits of operator combos
            val o = toOperators(j, n.size)
            val test = n.reduceIndexed { i, acc, n -> o[i].apply(acc, n) }
            return if (test == a) return true
            else continue
        }
        return false
    }

    fun toOperators(theNumber: Long, s: Int): List<Operator> = buildList {
        // convert bit pattern to list of operators
        for (i in 0..s) {
            if ((theNumber shr i and 0x01) == 0L) {
                add(Operator.PLUS)
            } else {
                add(Operator.MUL)
            }
        }
    }


    enum class Operator(value: Int) {
        PLUS(0) {
            override fun apply(first: Long, second: Long): Long = first + second
        },
        MUL(1) {
            override fun apply(first: Long, second: Long): Long = first * second
        };


        abstract fun apply(first: Long, second: Long): Long
    }

    override fun part2(input: List<String>): Long {
        return -1L
    }
}

typealias Equation = Pair<Long, List<Long>>