package days


object Day05 : Day<Long, String> {
    override val number: Int = 5
    override val expectedPart1Test: Long = 35L
    override val expectedPart2Test: Long = -1L
    override var useTestData = true
    override val debug = false

    override fun part1(input: String): Long {
        val (seeds, categories) = readAlmanac(input)
        val positions = seeds.map { seed ->
            categories.fold(seed) { l, c -> c.convert(l) }
        }
        println(positions)
        return positions.min()
    }

    override fun part2(input: String): Long {
        return -1L
    }

    private fun readAlmanac(input: String): Pair<List<Long>, List<Category>> {
        val inputLines = input.split("\n\n").map { it.lines() }
        val seeds = inputLines[0][0].readLongs()
        val categories = inputLines.drop(1).map {
            Category(it)
        }
        return seeds to categories
    }

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

    private fun String.readLongs() = Regex("""\d+""").findAll(this)
            .map(MatchResult::value)
            .map(String::toLong)
            .toList()
}
