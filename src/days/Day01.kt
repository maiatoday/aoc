package days

// Day object template
object Day01 : Day<Long, List<String>> {
    override val number: Int = 1
    override val expectedPart1Test: Long = 209L //142L
    override val expectedPart2Test: Long = 281L
    override var useTestData = true
    override val debug = false

    override fun part1(input: List<String>): Long =
            input.sumOf {
                it.onlyDigits().extractNumber()
            }.toLong()


    override fun part2(input: List<String>): Long =
            input.sumOf {
                it.transform(mashup).transform(numberDigits).onlyDigits().extractNumber()
            }.toLong()

    private fun String.onlyDigits() = this.filter { it.isDigit() }

    private fun String.extractNumber(): Int = "${(this.firstOrNull() ?: "")}${this.lastOrNull() ?: "0"}".toInt()

    private val numberWords = listOf(
            "one",
            "two",
            "three",
            "four",
            "five",
            "six",
            "seven",
            "eight",
            "nine",
    )

    private val numberDigits = numberWords.mapIndexed { i, w -> w to (i + 1).toString() }

    private val mashup = listOf(
            "oneight" to "18",
            "twone" to "21",
            "threeight" to "38",
            "fiveeight" to "58",
            "eightwo" to "82",
            "eighthree" to "83",
            "nineight" to "98",
    )

    private fun String.transform(transformMap: List<Pair<String, String>>): String =
            transformMap
                    .fold(this) { r, tt ->
                        r.replace(tt.first, tt.second)
                    }

}


