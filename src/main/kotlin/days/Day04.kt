package days

object Day04 : Day {

    override fun part1(input: List<String>): Long = input
        .map { line -> line.extractAreas() }
        .count { (first, second) -> checkCompleteOverlap(first, second) }.toLong()

    override fun part2(input: List<String>): Long = input
        .map { line -> line.extractAreas() }
        .count { (first, second) -> checkPartialOverlap(first, second) }.toLong()

    private val elfAreasRegex = """(\d+)-(\d+),(\d+)-(\d+)""".toRegex()

    private fun String.extractAreas(): Pair<IntRange, IntRange> {
        // can also be done by splitting strings but trying out the regexes
        val (startElf1, endElf1, startElf2, endEfl2) = elfAreasRegex
            .matchEntire(this)
            ?.destructured
            ?: throw IllegalArgumentException("Bad data $this")
        return startElf1.toInt()..endElf1.toInt() to startElf2.toInt()..endEfl2.toInt()
    }

    private fun checkCompleteOverlap(first: IntRange, second: IntRange) =
        first.first <= second.first && second.last <= first.last || second.first <= first.first && first.last <= second.last

    private fun checkPartialOverlap(first: IntRange, second: IntRange): Boolean =
        first.first <= second.first && first.last <= second.last && second.first <= first.last ||
                second.first <= first.first && second.last <= first.last && first.last <= second.first ||
                checkCompleteOverlap(first, second)

//    Alternative solution using sets
    private fun checkCompleteOverlapSet(first: Set<Int>, second: Set<Int>) =
        first.containsAll(second) || second.containsAll(first)

    private fun checkPartialOverlapSet(first: Set<Int>, second: Set<Int>) =
        (first intersect second).isNotEmpty()

    private fun IntRange.toSet(): Set<Int> {
        // this solution favours ease of code and learning libs not execution speed or memory use
        // it seems wasteful to allocate memory for every element of the set
        return buildSet {
            // there is a retainAll
            for (i in this@toSet)
                add(i)
        }
    }
}
