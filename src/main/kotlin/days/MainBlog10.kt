package days

import readInput
import kotlin.system.measureTimeMillis

fun main() {
    val crtW = 40
    val crtH = 6

    data class Instruction(val ticks: Int = 1, val inc: Int = 0)

    fun String.toInstruction() = when (substringBefore(" ")) {
        "noop" -> Instruction()
        "addx" -> Instruction(ticks = 2, inc = substringAfter(" ").toInt())
        else -> error("oops")
    }

    fun crtDisplay(input: List<String>) {
        //transform a list of Strings to a list of Instructions
        val instructions: List<Instruction> = input.map { it.toInstruction() }
        val xRegisterAtTick: List<Int> = buildList {
            // transform instructions to x register values
            var x = 1 // needs a running x register value
            for (i in instructions) {
                repeat(i.ticks) {
                    this.add(x)
                }
                x += i.inc
            }
            this.add(x)
        }
        for (i in 0 until (crtW * crtH)) {
            // transform xRegister values to pixels(String)
            // chop up in lines
            // side effect: print pixels
            if (i.mod(crtW) == 0) print("\n")                                                      //<============  side effect
            print(if (i.mod(crtW) in xRegisterAtTick[i] - 1..xRegisterAtTick[i] + 1) "█" else ".") //<============  side effect
        }
    }

    val testInput = readInput(10, "Day_test")
    println(" ============== test input =============")
    crtDisplay(testInput)
    println("\n\n")
    println("============== real input ==============")
    val input = readInput(10, "Day")
    crtDisplay(input)
    println("\n\n")
}