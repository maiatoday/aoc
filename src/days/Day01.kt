package days

// Day object template
object Day01 : Day<Long, List<String>> {
    override val number: Int = 1
    override val expectedPart1Test: Long = -1L
    override val expectedPart2Test: Long = -1L
    override var useTestData = true
    override val debug = false

    override fun part1(input: List<String>): Long =
            input.sumOf { it.transform1().extractNumber() }.toLong()


    override fun part2(input: List<String>): Long = input.sumOf {
        it.transform2().transform1().extractNumber()
    }.toLong()

//    fun part1Alt(input: List<String>): Long = -1L
//    fun part2Alt(input: List<String>): Long = -1L

    private fun String.transform1() = this.filter { it.isDigit() }

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

    private val mashup = mapOf(
            "oneight" to "18",
            "twone" to "21",
            "threeight" to "38",
            "fiveeight" to "58",
            "eightwo" to "82",
            "eighthree" to "83",
            "nineight" to "98",
    )

    private fun String.transform2(): String {
        var ss = this
        mashup.forEach { e ->
            //println("replacing ${e.key}, with ${e.value}")
            ss = ss.replace(e.key, e.value)
        }
        numberWords.forEachIndexed { i, w ->
            // println("replacing $w with ${i + 1}")
            ss = ss.replace(w, (i + 1).toString())
            //println(ss)
        }
        println("transform $this to $ss")
        return ss
    }

}


