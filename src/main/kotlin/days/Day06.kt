package days

object Day06 : Day<Int, String> {
    override val number: Int = 6
    override val expectedPart1Test: Int = 11
    override val expectedPart2Test: Int = 26
    override var useTestData = true
    override val debug = false

    override fun part1(input: String): Int = signalCheck(input, 4)

    override fun part2(input: String): Int = signalCheck(input, 14)

    private fun signalCheck(input: String, window: Int): Int {
        input.windowed(window)
            .forEachIndexed { index, s ->
                if (s.allDifferent()) {
                     return index + window
                }
            }
        return -1
    }

    private fun String.allDifferent(): Boolean = this.toList().distinct().count() == length

    fun part1Alt(input: String): Int = signalCheckSet(input, 4)

    fun part2Alt(input: String): Int = signalCheckSet(input, 14)

    private fun String.allDifferentSet(): Boolean = this.toSet().size == length

    private fun signalCheckSet(input: String, window: Int): Int {
        input.windowed(window)
            .forEachIndexed { index, s ->
                if (s.allDifferentSet()) {
                     return index + window
                }
            }
        return -1
    }

}