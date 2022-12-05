package days

interface Day<T, I> {
    val number: Int
    val expectedPart1Test: T
    val expectedPart2Test: T
    fun part1(input: I): T
    fun part2(input: I): T
}