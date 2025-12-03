package days

object Day03 : Day<Long, List<String>> {
    override val number: Int = 3
    override val expectedPart1Test: Long = 357L
    override val expectedPart2Test: Long = -1L
    override var useTestData = true
    override val debug = false

    override fun part1(input: List<String>): Long {
        val banks = input.toBanks()
        val joltages = banks.map { findMaxJoltage(it) }
        return joltages.sum().toLong()
    }

    override fun part2(input: List<String>): Long {
        return -1L
    }

    fun List<String>.toBanks() = this.map { bank -> bank.map { it.digitToInt() } }

    fun findMaxJoltage(bank: List<Int>): Int {
        val positionLookup = buildPositionLookup(bank)
        val joltage= findMaxJoltage(positionLookup)
        return joltage
    }

    fun buildPositionLookup(bank: List<Int>) = buildMap {
        for (digitToMatch in 9 downTo 1) {
            val digitIndices = buildList {
                bank.mapIndexed { index, digit -> if (digit == digitToMatch) add(index) }
            }
            put(digitToMatch, digitIndices.sortedDescending())
        }
    }

    private fun findMaxJoltage(positionLookup: Map<Int, List<Int>>): Int {
        val possibleJoltages = buildList {
            for (tensDigit in 9 downTo 1) {
                for (unitDigit in 9 downTo 1) {
                    val tensIndexList = positionLookup[tensDigit]
                    val unitsIndexList = positionLookup[unitDigit]
                    tensIndexList?.let { tens ->
                        unitsIndexList?.let { units ->
                            if (tens.isNotEmpty() && units.isNotEmpty())
                                if (tens.first() < units.first()) {
                                    add(( tensDigit to unitDigit).toJoltage())
                                }
                        }
                    }
                }
            }
        }

        return possibleJoltages.max()
    }

    // 17303 too low

    fun Pair<Int,Int>.toJoltage() = (this.first.toString() + this.second.toString()).toInt()

}