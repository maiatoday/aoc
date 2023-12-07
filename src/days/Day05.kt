package days

import util.readLongs
import kotlin.math.min


object Day05 : Day<Long, String> {
    override val number: Int = 5
    override val expectedPart1Test: Long = 35L
    override val expectedPart2Test: Long = 46L
    override var useTestData = true
    override val debug = false

    override fun part1(input: String): Long {
        val (seeds, categories) = readAlmanac(input)
        val locations = seeds.map { seed ->
            categories.lookup(seed)
        }
        return locations.min()
    }

    override fun part2(input: String): Long {
        val (seedNumbers, categories) = readAlmanac(input)
        val seedRanges = seedNumbers.chunked(2).map { LongRange(it[0], it[0] + it[1] - 1) }
        val locationsMin = seedRanges.map { seedRange ->
            seedRange.reduce { prev, seed ->
                val new = categories.lookup(seed)
                min(prev, new)
            }
        }
        return locationsMin.min()
    }

    private fun readAlmanac(input: String): Pair<List<Long>, List<Category>> {
        val inputLines = input.split("\n\n").map { it.lines() }
        val seeds = inputLines[0][0].readLongs()
        val categories = inputLines.drop(1).map {
            Category(it)
        }
        return seeds to categories
    }

    fun List<Category>.lookup(seed: Long) =
            fold(seed) { l, c -> c.convert(l) }

    data class Category(val name: String, val tables: List<Table>) {
        fun convert(source: Long): Long {
            val match = tables.filter { source in it.sourceRange }
            return if (match.size == 1) {
                val (sourceRange, destinationOffset) = match.first()
                destinationOffset + source - sourceRange.first
            } else {
                source
            }
        }
    }

    private fun Category(input: List<String>): Category =
            Category(input[0], input.drop(1).filter { it.isNotEmpty() }.map { Table(it) })

    data class Table(val sourceRange: LongRange, val destinationOffset: Long)

    private fun Table(input: String): Table {
        val (destinationOffset, sourceStart, sourceLength) = input.readLongs()
        return Table(sourceStart..<sourceStart + sourceLength, destinationOffset)
    }

}
