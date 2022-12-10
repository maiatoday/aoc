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
        // expand out the clock ticks making a list of values to add to x register
        val instExpanded: List<List<Int>> = instructions.map { i ->
            buildList {
                repeat(i.ticks - 1) { this.add(0) }
                this.add(i.inc)
            }
        }
        // it needs to be flattened
        val instFlatExpanded: List<Int> = instructions.flatMap { i ->
            buildList {
                repeat(i.ticks - 1) { this.add(0) }
                this.add(i.inc)
            }
        }

        // calculate the x values
        val xRegisterAtTick = instFlatExpanded.runningFold(1) { acc, i -> acc + i }

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