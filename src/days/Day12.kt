package days

object Day12 : Day<Long, List<String>> {
    override val number: Int = 12
    override val expectedPart1Test: Long = 21L
    override val expectedPart2Test: Long = -1L
    override var useTestData = true
    override val debug = false

    override fun part1(input: List<String>): Long {
        val springRecords = input.map { Record(it) }
        return springRecords.sumOf { it.guessArrangements() }.toLong()
    }

    data class Record(val row: String, val workingOrder: List<Int>)

    private fun Record.guessArrangements(): Int {
        return 0
    }

    fun Record(input: String): Record {
        val (row, countString) = input.split(" ")
        val workingOrder = countString.trim().split(",").map { it.toInt() }
        return Record(row, workingOrder)
    }

    override fun part2(input: List<String>): Long {
        return -1L
    }
}
