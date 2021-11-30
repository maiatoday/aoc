fun main() {

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(Day01.part1(testInput) == 1)

    val input = readInput("Day01")
    println(Day01.part1(input))
    println(Day01.part2(input))
}