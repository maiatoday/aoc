package days

//import days.Day99
import util.readInput
import kotlin.system.measureTimeMillis

const val dayNumber = 5
val day = Day05
fun main() {

    println("***** Day$dayNumber *****")
    val testInput = readInput(dayNumber, "Day_test")
    var part1Test = 0L
    val part1TestMillis = measureTimeMillis {
        part1Test = day.part1(testInput)
    }
    check(part1Test == day.expectedPart1Test)
    println("✅ one with ${day.expectedPart1Test} ✅ in $part1TestMillis ms")
    var part2Test = 0L
    val part2TestMillis = measureTimeMillis {
        part2Test = day.part2(testInput)
    }
    check(part2Test == day.expectedPart2Test)
    println("✅ two with ${day.expectedPart2Test} ✅ in $part2TestMillis ms")
    println("*****************")

    val input = readInput(dayNumber, "Day")
    var part1 = 0L
    val part1Millis = measureTimeMillis {
        part1 = day.part1(input)
    }
    var part2 = 0L
    val part2Millis = measureTimeMillis {
        part2 = day.part2(input)
    }
    println("🌟 one : $part1 in $part1Millis ms")
    println("🌟 two : $part2 in $part2Millis ms")
    println("*****************")
}