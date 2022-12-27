package days

object Day10 : Day<Long, List<String>> {
    override val number: Int = 10
    override val expectedPart1Test: Long = 13140L
    override val expectedPart2Test: Long = -1L
    override var useTestData = true
    override val debug = false

    enum class OpCode(val ticks: Int) {
        NOOP(1), ADDX(2)
    }

    data class Instruction(val opcode: OpCode, val inc: Int = 0)

    private fun String.toInstruction(): Instruction =
        when (val opCode = OpCode.valueOf(substringBefore(" ").uppercase())) {
            OpCode.NOOP -> Instruction(opCode)
            OpCode.ADDX -> Instruction(opCode, substringAfter(" ").toInt())
        }

    override fun part1(input: List<String>): Long {
        val instructions = input.map { it.toInstruction() }
        val measurePoints = listOf(20, 60, 100, 140, 180, 220)
        val graph: List<Int> = instructions.run()
        val samples = measurePoints.map { p -> graph[p - 1] * p }
        return samples.sum().toLong()
    }

    private fun List<Instruction>.run(): List<Int> =
        buildList {
            var x = 1
            this@run.forEach { i ->
                when (i.opcode) {
                    OpCode.NOOP -> this.add(x)
                    OpCode.ADDX -> {
                        repeat(i.opcode.ticks) {
                            this.add(x)
                        }
                        x += i.inc
                    }
                }
            }
            this.add(x)
        }

    override fun part2(input: List<String>): Long {
        val instructions = input.map { it.toInstruction() }
        val graph: List<Int> = instructions.run()
        drawScreen(graph)
        return -1L
    }

    private const val crtW = 40
    private const val crtH = 6
    private fun drawScreen(graph: List<Int>) {
        for (i in 0..(crtW * crtH)) {
            if (i.mod(crtW) == 0) print("\n")
            print(scanPixel(i, graph))
        }
    }

    private fun scanPixel(i: Int, graph: List<Int>): String =
        if (i.mod(crtW) in graph[i] - 1..graph[i] + 1) "█" else "."

}