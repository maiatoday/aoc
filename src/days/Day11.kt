package days

import util.readInts
import java.math.BigInteger
import java.math.BigInteger.ONE
import java.math.BigInteger.ZERO

object Day11 : Day<Long, List<String>> {
    override val number: Int = 11
    override val expectedPart1Test: Long = 55312L
    override val expectedPart2Test: Long = 55312L
    override var useTestData = true
    override val debug = false

    override fun part1(input: List<String>): Long {
        val stones = input.first().readInts().map { Stone(it) }
        return applyRules(stones, 25).size.toLong()
    }

    private fun applyRules(stones: List<Stone>, blinks: Int): List<Stone> {
        var stoneRow = stones
        repeat(blinks) {
            stoneRow = stoneRow
                .flatMap { stone ->
                    doMagic(stone)
                }
        }
        return stoneRow
    }

    data class Snapshot(val stone: String)

    val bigTableInTheSky: MutableMap<Snapshot, List<Stone>> = mutableMapOf()
    fun Stone(initial: Int) = Stone(initial.toBigInteger())
    data class Stone(val value: BigInteger) {

        internal fun flip(): List<Stone> = listOf(Stone(ONE))

        internal fun isEven(): Boolean = (value.toString().length % 2) == 0

        internal fun split(): List<Stone> {
            val ss = value.toString()
            val s1 = ss.substring(0..<ss.length / 2)
            val s2 = ss.substring(ss.length / 2)
            return listOf(Stone(BigInteger(s1)), Stone(BigInteger(s2)))
        }

        internal fun timesBig(): List<Stone> = listOf(Stone(value.multiply(theNumber)))
    }
    val theNumber = 2024.toBigInteger()
    fun doMagic(s:Stone): List<Stone> =
        when {
            s.value == ZERO -> s.flip()
            s.isEven() -> s.split()
            else -> s.timesBig()
        }

    override fun part2(input: List<String>): Long {
        val stones = input.first().readInts().map { Stone(it) }
        return applyRulesDifferently(stones, 75)
    }

    private fun applyRulesDifferently(stones: List<Stone>, blinks: Int): Long {
        var runningTotal = 0L
        val stoneRow = stones.map { it to blinks }
        val stonesToProcess = ArrayDeque<Pair<Stone, Int>>(stoneRow)
        while (stonesToProcess.isNotEmpty()) {
            val (s, blink) = stonesToProcess.removeFirst()
            val newStones = doMagicAndStuff(s)
            if (blink == 1) runningTotal += newStones.size
            if (blink - 1 != 0) {
                newStones.forEach {
                    stonesToProcess.addFirst(it to blink - 1)
                }
            }
        }
        return runningTotal
    }

    fun doMagicAndStuff(stone: Stone): List<Stone> {
        val snapshot = Snapshot(stone.value.toString())
        if (bigTableInTheSky.contains(snapshot)) return bigTableInTheSky[snapshot] ?: error("Oops")
        val newStones = doMagic(stone)
        bigTableInTheSky.put(snapshot, newStones)
        return newStones
    }

}