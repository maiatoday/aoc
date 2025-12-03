package days

object Day03 : Day<Long, List<String>> {
    override val number: Int = 3
    override val expectedPart1Test: Long = 357L
    override val expectedPart2Test: Long = 3121910778619L
    override var useTestData = true
    override val debug = false

    override fun part1(input: List<String>): Long {
        val banks = input.toBanks()
        val joltages = banks.map { findMaxJoltage(it) }
        return joltages.sum().toLong()
    }

    fun List<String>.toBanks() = this.map { bank -> bank.map { it.digitToInt() } }

    fun findMaxJoltage(bank: List<Int>): Int {
        val positionLookup = buildPositionLookup(bank)
        // val joltage = findMaxJoltageFromLookup(positionLookup)
        val joltage = findMaxJoltageFromLookup(positionLookup)
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

    private fun findMaxJoltageFromLookup(positionLookup: Map<Int, List<Int>>): Int {
        for (tensDigit in 9 downTo 1) {
            for (unitDigit in 9 downTo 1) {
                val tensIndexList = positionLookup[tensDigit]
                val unitsIndexList = positionLookup[unitDigit]
                tensIndexList?.let { tensIndices ->
                    unitsIndexList?.let { unitsIndices ->
                        if (tensIndices.isNotEmpty() && unitsIndices.isNotEmpty())
                            if (tensIndices.any { tens -> unitsIndices.any { tens < it } }) {
                                return (tensDigit to unitDigit).toJoltage()
                            }
                    }
                }
            }
        }
        return 0
    }

    fun Pair<Int, Int>.toJoltage() = (this.first.toString() + this.second.toString()).toInt()

    //region part 2
    override fun part2(input: List<String>): Long {
        val joltages = input.map { findMaxBigJoltage(it) }
        return joltages.sum()
    }

    private fun findMaxBigJoltage(bankString: String, numDigits: Int = 12): Long {
        // need to break out early. can't build the entire list
        // need to build up the list from the top for 12 digits

        // region mental churn
        // is there a 9 with 11 digits to the right? i.e. positionLookup[9].filter { it > bankSize - 11 }
        // if yes take candidates convert to joltage and take the biggest
        // if no is there an 8 or 7 or 6 ...

//        for (digit in 9 downTo 1) {
//            if (positionLookup[digit]?.isNotEmpty() == true) {
//                val candidates = positionLookup[digit]?.filter { it < bank.size - 12 }
//                if (candidates?.isNotEmpty() == true) {
//                    val theOne = candidates.maxOfOrNull { bankString.substring(it, it + 12).toLong() }
//                    if (theOne != null) return theOne
//                }
//            }
//        }

        //!!!!! NOOOO there can be gaps....
        //endregion

        // start again

        // build a new string from digits

        // 9 with 11 to the right
        // if yes take it + evaluate what is left , < ---- what happens if there is two 9's that are valid? evaluate both

        // if no 8 with 11 to the right, take it
        // if yes take it + evaluate what is left

        // if no 7 with 11 to the right etc etc

        // What is `evaluate what is left?` start at 9 again but check for 10 to the right
        // and only work on the rest of the string from the position of the 9

        return nextBiggest(bankString, numDigits, "")
    }

    private fun nextBiggest(bankString: String, position: Int, maxString: String): Long {
        if (position == 0) return maxString.toLong()
        for (digitToMatch in 9 downTo 1) {
            val digitChar = digitToMatch.digitToChar()
            val candidates = validCandidates(digitChar, bankString, position)
            if (candidates.isNotEmpty()) {
                return candidates.maxOfOrNull {
                    nextBiggest(bankString.substring(it + 1), position - 1, maxString + digitChar)
                } ?: 0L
            }
        }
        return 0L
    }

    // indices of the numbers where they appear in the bank, only if they have enough digits left
    private fun validCandidates(digitToMatch: Char, bank: String, count: Int) =
        buildList {
            bank.mapIndexed { index, digit -> if (digit == digitToMatch) add(index) }
        }.filter { it <= bank.length - count }

    //endregion
}
