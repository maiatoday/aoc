import kotlin.system.measureTimeMillis

fun main() {

    println("***** $dayName *****")
    val testInput = readInput("${dayName}_test")
    var part1Test = 0L
    val part1TestMillis = measureTimeMillis {
        part1Test = day.part1(testInput)
    }
    check(part1Test == dayTestAnswer1)
    println("✅ one with $dayTestAnswer1 ✅ in $part1TestMillis ms")
    var part2Test = 0L
    val part2TestMillis = measureTimeMillis {
        part2Test = day.part2(testInput)
    }
    check(part2Test == dayTestAnswer2)
    println("✅ two with $dayTestAnswer2 ✅ in $part2TestMillis ms")
    println("*****************")

    val input = readInput(dayName)
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

const val dayName = "Day16"
val day = Day16
const val dayTestAnswer1 = 20L
const val dayTestAnswer2 = 1L