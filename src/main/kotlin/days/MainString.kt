package days
import readInputString
import kotlin.system.measureTimeMillis

fun main() {
    val today = Day05
    println("***** Day${today.number} *****")
    val testInput = readInputString(today.number,"Day_test")
    var part1Test = ""
    val part1TestMillis = measureTimeMillis {
        part1Test = today.part1S(testInput)
    }
    check(part1Test == today.expectedPart1STest)
    println("✅ one with ${today.expectedPart1STest} ✅ in $part1TestMillis ms")
    var part2Test = ""
    val part2TestMillis = measureTimeMillis {
        part2Test = today.part2S(testInput)
    }
    check(part2Test == today.expectedPart2STest)
    println("✅ two with ${today.expectedPart2STest} ✅ in $part2TestMillis ms")
    println("*****************")

    val input = readInputString(today.number, "Day")
    var part1 = ""
    val part1Millis = measureTimeMillis {
        part1 = today.part1S(input)
    }
    var part2 = ""
    val part2Millis = measureTimeMillis {
        part2 = today.part2S(input)
    }
    println("🌟 one : $part1 in $part1Millis ms")
    println("🌟 two : $part2 in $part2Millis ms")
    println("*****************")
}