fun main() {

    val dayName = "Day02"
    val day = Day02
    val dayTestAnswer1 = 150
    val dayTestAnswer2 = 900

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