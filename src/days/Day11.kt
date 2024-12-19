package days

import util.readInts

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
            stoneRow = stoneRow.flatMap { doMagic(it) }
        }
        return stoneRow
    }

    fun Stone(initial: Int) = Stone(initial.toLong())
    data class Stone(val value: Long) {

        internal fun flip(): List<Stone> = listOf(Stone(1))

        internal fun isEven(): Boolean = (value.toString().length % 2) == 0

        internal fun split(): List<Stone> {
            val ss = value.toString()
            val s1 = ss.substring(0..<ss.length / 2)
            val s2 = ss.substring(ss.length / 2)
            return listOf(Stone(s1.toLong()), Stone(s2.toLong()))
        }

        internal fun timesBig(): List<Stone> = listOf(Stone(value * 2024L))
    }

    fun doMagic(s: Stone): List<Stone> =
        when {
            s.value == 0L -> s.flip()
            s.isEven() -> s.split()
            else -> s.timesBig()
        }

    override fun part2(input: List<String>): Long {
        val startingStones = input.first().split(" ")
        val blinkCount = if (useTestData) 25 else 75
        return applyRulesDifferently(startingStones, blinkCount)
    }

    private fun applyRulesDifferently(stones: List<String>, blinks: Int): Long {
        // keep a map of only the counts of each type of stone I have at each blink step
        var tally: MutableMap<Int, StoneTallyOneBlink> = mutableMapOf()
        // put the initial counts of the starting stones into the map
        val initialTallies = mutableMapOf<Long, Long>()
        for (s in stones) {
            initialTallies.put(s.toLong(), 1)
        }
        tally[0] = initialTallies
        repeat(blinks) { blink ->
            val currentTallies = tally[blink]
            currentTallies?.let { t ->
                // take the current blink tallies and work out
                // what stone types and how many in the next blink
                var tempTallies = mutableMapOf<Long, Long>()
                for ((currentStone, count) in currentTallies) {
                    tempTallies = doMagicDifferently(currentStone, count, tempTallies)
                }
                tally.put(blink + 1, tempTallies)
            } ?: error("Ooops")
        }

        // only the last blink level numbers get added up
        return tally[blinks]?.values?.sum() ?: -1L
    }

    fun doMagicDifferently(stone: Long, count: Long, blinkTally: StoneTallyOneBlink): StoneTallyOneBlink {
        val s = stone.toString()
        when {
            // if there isn't a "1" already put one in at tally 0 and add 1 else just add one to the existing
            stone == 0L -> blinkTally.put(1L, (blinkTally[1L] ?: 0) + count)
            (s.length % 2 == 0) -> {
                val s1 = s.substring(0..<s.length / 2).toLong()
                val s2 = s.substring(s.length / 2).toLong()
                blinkTally.put(s1, (blinkTally[s1] ?: 0) + count)
                blinkTally.put(s2, (blinkTally[s2] ?: 0) + count)
            }

            else -> {
                val sBig = (s.toLong() * 2024)
                blinkTally.put(sBig, (blinkTally[sBig] ?: 0) + count)
            }
        }
        return blinkTally
    }
}
typealias StoneTallyOneBlink = MutableMap<Long, Long>