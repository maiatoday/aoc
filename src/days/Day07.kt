package days

import util.readLongs
import kotlin.math.pow

object Day07 : Day<Long, List<String>> {
    override val number: Int = 7
    override val expectedPart1Test: Long = 3749L
    override val expectedPart2Test: Long = 11387L
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
        val comboMax = 2.toDouble().pow((n.size - 1).toDouble()).toLong()
        for (j in 0L..comboMax) {// j is the bits of operator combos
            val o = toOperators(j, n.size - 1)
            var test = n[0]
            for (xx in 0..<n.size - 1) {
                test = o[xx].apply(test, n[xx + 1])
                if (test > a) return false
            }
            if (test == a) return true
            else continue
        }
        return false
    }

    fun toOperators(theNumber: Long, s: Int): List<Operator> = buildList {
        // convert bit pattern to list of operators
        for (i in 0..<s) {
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
        },
        CONCAT(2) {
            override fun apply(first: Long, second: Long): Long = (first.toString() + second.toString()).toLong()
        },
        NOP(3) {
            override fun apply(first: Long, second: Long): Long = -1L
        };


        abstract fun apply(first: Long, second: Long): Long
    }

    override fun part2(input: List<String>): Long {
        val equations = parse(input)
        val answer = equations.filter { it.isOk3() }.sumOf { (a, _) -> a }
        return answer
    }

    // slow 116050 ms
    fun Equation.isOk3(): Boolean {
        val (a, n) = this
        //println("checking $n for answer $a")
        // based on n.length make combos
        val comboMax = 2.toDouble().pow(((n.size * 2) - 1).toDouble()).toLong()
        for (j in 0L..comboMax) {// j is the bits of operator combos
            val o = toOperators3(j, n.size - 1)
            if (Operator.NOP in o) continue
            var test = n[0]
            for (xx in 0..<(n.size - 1)) {
                test = o[xx].apply(test, n[xx + 1])
                if (test > a) return false
            }
            if (test == a) {
               // println("$a 🍬")
                return true
            } else continue
        }
        return false
    }

    fun toOperators3(theNumber: Long, s: Int): List<Operator> = buildList {
        // convert bit pattern to list of operators
        for (i in 0..<s) {
            when (theNumber shr (i * 2) and 0x03) {
                0L -> add(Operator.PLUS)
                1L -> add(Operator.MUL)
                2L -> add(Operator.CONCAT)
                3L -> add(Operator.NOP)
                else -> continue// do nothing
            }
        }
    }
}

typealias Equation = Pair<Long, List<Long>>