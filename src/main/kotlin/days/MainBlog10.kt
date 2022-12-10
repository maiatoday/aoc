package days

import readInput

fun main() {
    val crtW = 40

    data class Instruction(val ticks: Int = 1, val inc: Int = 0)

    fun String.toInstruction() = when (substringBefore(" ")) {
        "noop" -> Instruction()
        "addx" -> Instruction(ticks = 2, inc = substringAfter(" ").toInt())
        else -> error("oops")
    }

    fun Instruction.expandInstruction(): List<Int> =
        buildList {
            repeat(this@expandInstruction.ticks - 1) { this.add(0) }
            this.add(this@expandInstruction.inc)
        }

    fun Int.toPixel(index:Int):String = if (index % crtW in this - 1..this + 1) "█" else "."

    fun crtScan(input: List<String>): List<String> {
        //transform a list of Strings to a list of Instructions
        val instructions: List<Instruction> = input.map { it.toInstruction() }
        // expand out the clock ticks making a list of values to add to x register
        // it needs to be flattened
        val instructionsFlatExpanded: List<Int> = instructions.flatMap { i ->
            i.expandInstruction()
        }

        // calculate the x values
        val xRegisterAtTick = instructionsFlatExpanded.runningFold(1) { acc, i -> acc + i }
        val pixels = xRegisterAtTick.mapIndexed { index, x -> x.toPixel(index) }
        val lines = pixels.chunked(40).map { it.joinToString("") }
        return lines

    }

    fun crtScanCondensed(input: List<String>): List<String> =
        input.map { it.toInstruction() }// converts input to instruction
            .flatMap { i -> i.expandInstruction() } // expands multi tick instructions
            .runningFold(1) { x, i -> x + i } // runs through the instructions accumulating x
            .mapIndexed { index, x -> x.toPixel(index) } // converts index and x register to a pixel
            .chunked(40).map { it.joinToString("") } // spilt into lines for the screen


    fun List<String>.display() { // side effect method
        this.forEach {
            println(it)
        }
    }

    val testInput = readInput(10, "Day_test")
    println(" ============== test input =============")
    crtScan(testInput).display()
    println("============== real input ==============")
    val input = readInput(10, "Day")
    crtScan(input).display()
    crtScanCondensed(input).display()
    println("\n\n")
}