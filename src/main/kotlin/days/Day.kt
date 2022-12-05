package days

interface Day {
    val number:Int
    val expectedPart1Test:Long
    val expectedPart2Test:Long
    fun part1(input: List<String>): Long = -1L
    fun part2(input: List<String>): Long = -1L
    fun part1S(input: String): String = "String"
    fun part2S(input: String): String = "sss"
}