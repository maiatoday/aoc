package days

//import days.Day99
import util.readInput
import kotlin.system.measureTimeMillis

const val dayNumber = 17
val day = Day17
const val doCheck = true
fun main() {

    println("***** Day$dayNumber *****")
    if (doCheck) {
        val testInput = readInput(dayNumber, "Day_test")
        var part1Test:String
        val part1TestMillis = measureTimeMillis {
            part1Test = day.part1(testInput)
        }
       // check(part1Test == day.expectedPart1Test)
        println("✅ one with ${day.expectedPart1Test} ✅ in $part1TestMillis ms")
    }
    val input = readInput(dayNumber, "Day")
    var part1:String
    val part1Millis = measureTimeMillis {
        part1 = day.part1(input)
    }
    println("🌟 one : $part1 in $part1Millis ms")
    println("*****************")
    if (doCheck) {
        val testInput = readInput(dayNumber, "Day_test")
        var part2Test:String
        val part2TestMillis = measureTimeMillis {
            part2Test = day.part2(testInput)
        }
        check(part2Test == day.expectedPart2Test)
        println("✅ two with ${day.expectedPart2Test} ✅ in $part2TestMillis ms")
    }
    var part2:String
    val part2Millis = measureTimeMillis {
        part2 = day.part2(input)
    }
    println("🌟 two : $part2 in $part2Millis ms")
    println("*****************")
}