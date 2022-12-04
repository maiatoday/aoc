package days

interface Day {
    val number:Int
    val expectedPart1Test:Long
    val expectedPart2Test:Long
    fun part1(input: List<String>): Long
    fun part2(input: List<String>): Long
}