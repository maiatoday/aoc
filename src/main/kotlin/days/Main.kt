package days
import readInput
import kotlin.system.measureTimeMillis

val today = Day04
fun main() {

    println("***** Day${Day04.number} *****")
    val testInput = readInput(Day04.number,"Day_test")
    var part1Test = 0L
    val part1TestMillis = measureTimeMillis {
        part1Test = Day04.part1(testInput)
    }
    check(part1Test == Day04.expectedPart1Test)
    println("✅ one with ${Day04.expectedPart1Test} ✅ in $part1TestMillis ms")
    var part2Test = 0L
    val part2TestMillis = measureTimeMillis {
        part2Test = Day04.part2(testInput)
    }
    check(part2Test == Day04.expectedPart2Test)
    println("✅ two with ${Day04.expectedPart2Test} ✅ in $part2TestMillis ms")
    println("*****************")

    val input = readInput(Day04.number, "Day")
    var part1 = 0L
    val part1Millis = measureTimeMillis {
        part1 = Day04.part1(input)
    }
    var part2 = 0L
    val part2Millis = measureTimeMillis {
        part2 = Day04.part2(input)
    }
    println("🌟 one : $part1 in $part1Millis ms")
    println("🌟 two : $part2 in $part2Millis ms")
    println("*****************")
}