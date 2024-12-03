package days

import util.readInts

object Day03 : Day<Long, List<String>> {
    override val number: Int = 3
    override val expectedPart1Test: Long = 161L
    override val expectedPart2Test: Long = 48L
    override var useTestData = true
    override val debug = false

    override fun part1(input: List<String>): Long = extractMulNumbers(input).sumOf { it[0].toLong() * it[1].toLong() }

    override fun part2(input: List<String>): Long {
        val validCommands = extractEnabledCommands(input)
        return extractMulNumbers(validCommands).sumOf { it[0].toLong() * it[1].toLong() }
    }

    private fun extractEnabledCommands(input: List<String>) =
        // join and prefix with do to start enabled
        input.joinToString("", prefix = """do()""")
            // split on do's this means each section starts enabled
            .split(doRegex)
            // drop everything after the first don't - it will also drop multiple don'ts
            .map {
                it.substringBefore("""don't()""")
            }

    private fun extractMulNumbers(input: List<String>) =
        input.flatMap {
            mulRegex.toRegex()
                .findAll(it)
                .map(MatchResult::value)
                .toList()
                .map(String::readInts)
        }


    val mulRegex = """mul\(\d{1,3},\d{1,3}\)"""
    val doRegex = """do\(\)""".toRegex()
    val dontRegex = """don't\(\)""".toRegex()

    /* This way round does not work
    private fun extractEnabledCommandsBork(input: List<String>) =
        // join and prefix with do to start enabled
        input.joinToString("", prefix = """do()""")
            // split on don't this means each section starts disabled
            .split(dontRegex)
            // drop everything up to the first do
            .map {
                it.substringAfter("""do()""")
            }
    */
}