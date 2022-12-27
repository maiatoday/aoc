package days

typealias Day18ReturnType = Int
typealias Day18InputType = List<String>

object Day18 : Day<Day18ReturnType, Day18InputType> {
    override val number: Int = 18
    override val expectedPart1Test: Day18ReturnType = -1
    override val expectedPart2Test: Day18ReturnType = -1
    override var useTestData = true
    override val debug = false

    override fun part1(input: Day18InputType): Day18ReturnType {
        return expectedPart1Test
    }

    override fun part2(input: Day18InputType): Day18ReturnType {
        return expectedPart2Test
    }
}