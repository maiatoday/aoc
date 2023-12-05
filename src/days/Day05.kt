package days


object Day05 : Day<Long, List<String>> {
    override val number: Int = 5
    override val expectedPart1Test: Long = -1L
    override val expectedPart2Test: Long = -1L
    override var useTestData = true
    override val debug = false

    override fun part1(input: List<String>): Long {
        val (seeds, category) = readAlamanac(input)
        return seeds.minOf { seed ->
            category.fold(seed) { l, t -> t.convert(l) }
        }
    }

    override fun part2(input: List<String>): Long {
        return -1L
    }

    private fun readAlamanac(input: List<String>): Pair<List<Long>, List<Category>> {
        val seeds = input[0].readLongs()
//        val tables = buildList<Table> {
//            input.drop(2)
//                .map            
//        }
//        input.drop(2).
        return seeds to emptyList()
    }

    data class Category(val tables: List<Table>) {
        fun convert(source: Long): Long {
            val match = tables.filter { source in it.sourceRange }
            return if (match.size == 1) {
                val (sourceRange, destinationOffset) = match.first()
                destinationOffset + sourceRange.last - source
            } else {
                source
            }
        }
    }

    fun Category(input: List<String>): Category =
            Category(input.map { Table(it) })

    data class Table(val sourceRange: LongRange, val destinationOffset: Long)

    fun Table(input: String): Table {
        val (destinationOffset, sourceStart, sourceLength) = input.readLongs()
        return Table(sourceStart..sourceStart + sourceLength, destinationOffset)
    }

    private fun String.readLongs() = Regex("""\d+""").findAll(this)
            .map(MatchResult::value)
            .map(String::toLong)
            .toList()
}
