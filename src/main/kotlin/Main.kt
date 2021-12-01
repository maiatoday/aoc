fun main() {

    val dayName = "Day01"
    val day = Day01
    val dayTestAnswer1 = 7
    val dayTestAnswer2 = 5

    println("***** $dayName *****")
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("${dayName}_test")
    check(day.part1(testInput) == dayTestAnswer1)
    println("✅ one with $dayTestAnswer1 ✅")
    check(day.part2(testInput) == dayTestAnswer2)
    println("✅ two with $dayTestAnswer2 ✅")
    println("*****************")

    val input = readInput(dayName)
    println("🌟 one : ${day.part1(input)}")
    println("🌟 two : ${day.part2(input)}")
    println("*****************")
}