import days.Day03
import kotlin.system.measureTimeMillis

const val dayNumber = 3
val day = Day03()
const val dayTestAnswer1 = 157L
const val dayTestAnswer2 = 70L
fun main() {

    println("***** Day$dayNumber *****")
   // val testInput = readInput(dayNumber,"Day_test")
//    var part1Test:Long
//    val part1TestMillis = measureTimeMillis {
//        part1Test = day.part1()
//    }
//    check(part1Test == dayTestAnswer1)
//    println("✅ one with $dayTestAnswer1 ✅ in $part1TestMillis ms")
//    var part2Test:Long
//    val part2TestMillis = measureTimeMillis {
//        part2Test = day.part2()
//    }
//    check(part2Test == dayTestAnswer2)
//    println("✅ two with $dayTestAnswer2 ✅ in $part2TestMillis ms")
//    println("*****************")

//    val input = readInput(dayNumber, "Day")
    day.setUp()
    var part1:Long
    val part1Millis = measureTimeMillis {
        part1 = day.part1()
    }
    var part2:Long
    val part2Millis = measureTimeMillis {
        part2 = day.part2()
    }
    println("🌟 one : $part1 in $part1Millis ms")
    println("🌟 two : $part2 in $part2Millis ms")
    println("*****************")
}