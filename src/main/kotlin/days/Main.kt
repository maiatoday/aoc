package days

import readInput
import kotlin.system.measureTimeMillis

fun main() {

    val today = Day07

    println("***** Day${today.number} *****")
    val testInput = readInput(today.number, "Day_test")
    var part1Test = 0L
    val part1TestMillis = measureTimeMillis {
        part1Test = today.part1(testInput)
    }
    check(part1Test == today.expectedPart1Test)
    println("✅ one with ${today.expectedPart1Test} ✅ in $part1TestMillis ms")
    var part2Test = 0L
    val part2TestMillis = measureTimeMillis {
        part2Test = today.part2(testInput)
    }
    check(part2Test == today.expectedPart2Test)
    println("✅ two with ${today.expectedPart2Test} ✅ in $part2TestMillis ms")
    println("*****************")

    val input = readInput(today.number, "Day")
    var part1 = 0L
    val part1Millis = measureTimeMillis {
        part1 = today.part1(input)
    }
    var part2 = 0L
    val part2Millis = measureTimeMillis {
        part2 = today.part2(input)
    }
    println("🌟 one : $part1 in $part1Millis ms")
    println("🌟 two : $part2 in $part2Millis ms")
    println("*****************")
}
