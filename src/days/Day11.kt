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
    fun doMagic(s: Stone): List<Stone> =
        when {
            s.value == ZERO -> s.flip()
            s.isEven() -> s.split()
            else -> s.timesBig()
        }

    override fun part2(input: List<String>): Long {
        val startingStones = input.first().split(" ")
        return applyRulesDifferently(startingStones, 25)
    }

    private fun applyRulesDifferently(stones: List<String>, blinks: Int): Long {
        // keep a map of only the counts of each type of stone I have at each blink step
        var stoneTallyAllBlinks: Map<Int, StoneTallyOneBlink> = buildMap {
            // put the initial counts of the starting stones into the map
            val initialTallies = mutableMapOf<String, Long>()
            for (s in stones) {
                initialTallies.put(s, 1)
            }
            repeat(blinks) { blink ->
                var currentTallies = getOrPut(blink) { initialTallies  }
                // take the current blink tallies and work out
                // what stone types and how many in the next blink
                val stoneTypes = currentTallies.keys
                for (currentStone in stoneTypes) {
                    currentTallies = doMagicDifferently(currentStone, currentTallies)
                }
                //
                put(blink+1, currentTallies)
            }
        }
        // only the last blink level numbers get added up
        return stoneTallyAllBlinks[blinks-1]?.values?.sum() ?: -1L
    }

    fun doMagicDifferently(s: String, tally: StoneTallyOneBlink): StoneTallyOneBlink {
        when {
            // if there isn't a "1" already put one in at tally 0 and add 1 else just add one to the existing
            s == "0" -> tally.put("1", tally.getOrPut("1") { 0 } + 1)
            (s.length % 2 == 0) -> {
                val s1 = s.substring(0..<s.length / 2)
                val s2 = s.substring(s.length / 2)
                tally.put(s1, tally.getOrPut(s1) { 0 } + 1)
                tally.put(s2, tally.getOrPut(s2) { 0 } + 1)
            }

            else -> {
                val sBig = (s.toLong() * 2024).toString()
                tally.put(sBig, tally.getOrPut(sBig) { 0 } + 1)
            }
        }
        return tally
    }
}
typealias StoneTallyOneBlink = MutableMap<String, Long>