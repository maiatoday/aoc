package days

import util.readInput
import kotlin.system.measureTimeMillis

const val dayNumber = 8
val day = Day08
const val dayTestAnswer1 = -1L
const val dayTestAnswer2 = -1L
fun main() {

    println("***** Day$dayNumber *****")
    val testInput = readInput(dayNumber, "Day_test")
    var part1Test = 0L
    val part1TestMillis = measureTimeMillis {
        day.useTestData = true
        part1Test = day.part1(testInput)
    }
    check(part1Test == dayTestAnswer1)
    println("✅ one with $dayTestAnswer1 ✅ in $part1TestMillis ms")
    var part2Test = 0L
    val part2TestMillis = measureTimeMillis {
        day.useTestData = true
        part2Test = day.part2(testInput)
    }
    check(part2Test == dayTestAnswer2)
    println("✅ two with $dayTestAnswer2 ✅ in $part2TestMillis ms")
    println("*****************")

    val input = readInput(dayNumber, "Day")
    var part1 = 0L
    val part1Millis = measureTimeMillis {
        day.useTestData = false
        part1 = day.part1(input)
    }
    var part2 = 0L
    val part2Millis = measureTimeMillis {
        day.useTestData = false
        part2 = day.part2(input)
    }
    println("🌟 one : $part1 in $part1Millis ms")
    println("🌟 two : $part2 in $part2Millis ms")
    println("*****************")
}