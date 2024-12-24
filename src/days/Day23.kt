package days

object Day23 : Day<Long, List<String>> {
    override val number: Int = 23
    override val expectedPart1Test: Long = 7L
    override val expectedPart2Test: Long = -1L
    override var useTestData = true
    override val debug = false

    override fun part1(input: List<String>): Long {
        val pairs = input.map { it.split("-") }
        val computers = pairs.flatMap { it }.sorted()
        val historianComputers = computers.filter { it.startsWith("t") }
        val historianConnections = pairs
            .filter { it[0] in historianComputers || it[1] in historianComputers }
            .map { if (it[1].startsWith("t")) listOf(it[1], it[0]) else it }
        val matchedConnections = historianConnections.groupBy { it[0] }
        return -1L
    }

    override fun part2(input: List<String>): Long {
        return -1L
    }
}