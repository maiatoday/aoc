package days

object Day04 : Day<Long, List<String>> {
    override val number: Int = 4
    override val expectedPart1Test: Long = 13L
    override val expectedPart2Test: Long = 30L
    override var useTestData = true
    override val debug = false

    override fun part1(input: List<String>): Long =
            input.mapIndexed { i, c ->
                Card(i, c)
            }.sumOf {
                it.score()
            }.toLong()

    override fun part2(input: List<String>): Long =
            forTheWin(input.mapIndexed { i, c -> Card(i, c) })
                    .sumOf { it }
                    .toLong()

    data class Card(val id: Int, val numbers: List<Int>, val winningNumbers: Set<Int>) {
        val matchCount = (numbers intersect winningNumbers).count()
        private val scoreTable = mapOf(
                0 to 0,
                1 to 1,
                2 to 2,
                3 to 4,
                4 to 8,
                5 to 16,
                6 to 32,
                7 to 64,
                8 to 128,
                9 to 256,
                10 to 512,
        )

        fun score(): Int = scoreTable[matchCount] ?: 0
    }

    private fun Card(id: Int, input: String): Card {
        val numbers = input.split(":")[1].split("|")[0].extractNumbers()
        val winningNumbers = input.split(":")[1].split("|")[1].extractNumbers()
        return Card(id, numbers, winningNumbers.toSet())
    }

    private fun String.extractNumbers() =
            this.trim().split(" ").filter { it.isNotEmpty() }.map { it.toInt() }

    private fun forTheWin(input: List<Card>): IntArray {
        val cardScores = input.associate { it.id to it.matchCount }
        val tally = IntArray(cardScores.size) { 1 }
        for (i in tally.indices) {
            val score = cardScores[i] ?: 0
            val currentCount = tally[i]
            val end = (i + score).coerceAtMost(cardScores.size - 1)
            if (i + 1 < cardScores.size)
                for (zz in i + 1..end) {
                    tally[zz] += currentCount
                }
        }
        return tally
    }
}
