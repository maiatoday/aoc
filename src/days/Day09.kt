package days

import util.filterComments
import util.readLongs

object Day09 : Day<Long, List<String>> {
    override val number: Int = 9
    override val expectedPart1Test: Long = 114L
    override val expectedPart2Test: Long = 2L
    override var useTestData = true
    override val debug = false

    override fun part1(input: List<String>): Long {
        val readings = input.filterComments().map { it.readLongs() }
        return readings.sumOf { predictNext(it) }
    }

    override fun part2(input: List<String>): Long {
        val readings = input.filterComments().map { it.readLongs() }
        return readings.sumOf { inferFirst(it) }
    }

    private fun List<Long>.deltas() = windowed(2).map { it[1] - it[0] }

    private fun buildSieve(sequence: List<Long>) = buildList<List<Long>> {
        add(sequence)
        while (!this.last().all { it == 0L }) {
            add(this.last().deltas())
        }
    }

    private fun predictNext(sequence: List<Long>): Long = buildSieve(sequence).predict()

    private fun List<List<Long>>.predict(): Long = this.asReversed()
            .fold(0L) { a, row -> a + row.last() }

    private fun inferFirst(sequence: List<Long>): Long = buildSieve(sequence).infer()

    private fun List<List<Long>>.infer(): Long = this.asReversed()
            .fold(0L) { a, row -> row.first() - a }
}
