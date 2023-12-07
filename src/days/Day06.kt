package days

import util.readInts
import util.readLongs

object Day06 : Day<Long, List<String>> {
    override val number: Int = 6
    override val expectedPart1Test: Long = 288L
    override val expectedPart2Test: Long = 71503L
    override var useTestData = true
    override val debug = false

    override fun part1(input: List<String>): Long {
        val numbers = input.map { it.readLongs() }
        val races = numbers[0] zip numbers[1]
        return races.fold(1) { acc, race ->
            acc * race.winningChargeTimes().count()
        }
    }

    override fun part2(input: List<String>): Long {
        val numbersLong = input.map { it.readLongs() }
        val numbers = numbersLong.map { ll ->
            ll.fold("") { p, t ->
                p + t.toString()
            }
        }.map { it.toLong() }
        input.map { }
        val race = numbers[0] to numbers[1]
        val times = race.winningChargeTimes()

        return times.count().toLong()
    }

    fun Pair<Long, Long>.winningChargeTimes(): List<Long> {
        val (time, distanceToBeat) = this
        return (1..time).fold(mutableListOf<Long>()) { acc, chargeTime ->
            val raceDistance = (time - chargeTime) * chargeTime
            if (raceDistance > distanceToBeat) {
                acc.add(chargeTime)
            }
            acc
        }
    }
}
