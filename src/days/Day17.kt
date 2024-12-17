package days

import days.OperandType.LITERAL
import util.readInts
import util.splitByBlankLine
import kotlin.math.pow

object Day17 : Day<String, List<String>> {
    override val number: Int = 17
    override val expectedPart1Test: String = "4,6,3,5,6,3,5,2,1,0"
    override val expectedPart2Test: String = ""
    override var useTestData = true
    override val debug = false

    override fun part1(input: List<String>): String {
        val littleComputer = LittleComputer(input)
        val output = littleComputer.run()
        return output
    }

    override fun part2(input: List<String>): String {
        val littleComputer = LittleComputer(input)
        return ""
    }

    fun LittleComputer(input: List<String>): LittleComputer {
        val (r, i) = input.splitByBlankLine()
        val registersList = r.flatMap { it.readInts() }
        val registers = Registers(registersList[0], registersList[1], registersList[2])
        val instructions = i.joinToString("").readInts().chunked(2).map { Instruction(Pair(it[0], it[1])) }
        return LittleComputer(instructions, registers, i.joinToString(""))
    }

    class LittleComputer(val instructions: List<Instruction>, val initialRegisters: Registers, val originalProgram:String) {

        var registers: Registers = initialRegisters

        var output = ""

        fun run(): String {
            var instructionPointer = 0
            var endlessLoopDetector = 0
            while (instructionPointer in instructions.indices && endlessLoopDetector < 1000) {
                endlessLoopDetector++
                try {
                    //println("IP = $instructionPointer executing ${instructions[instructionPointer]}")
                    val (o, regs) = instructions[instructionPointer](registers)
                    o?.let { output += ",$o" }
                    registers = regs
                    instructionPointer++
                } catch (jump: JumpException) {
                    //println("Jump to ${jump.jumps}")
                    instructionPointer = jump.jumps / 2
                }
            }
            return if (endlessLoopDetector > 1000) "HALT: endless loop" else output.drop(1)
        }
    }

    fun Instruction(values: Pair<Int, Int>): Instruction =
        Instruction(OpCode.fromValue(values.first), Operand(values.second))

    data class Instruction(val opCode: OpCode, val operand: Operand) {
        operator fun invoke(registers: Registers): Pair<String?, Registers> =
            when (opCode) {
                OpCode.ADV -> {
                    val nomenator = registers.a()
                    val denomenator = 2.0.pow(operand.getValue(registers))
                    val answer = (nomenator / denomenator).toInt()
                    null to registers.copy(first = answer)
                }

                OpCode.BXL -> {
                    val answer = registers.b() xor operand.getValue(registers, LITERAL)
                    null to registers.copy(second = answer)
                }

                OpCode.BST -> {
                    val answer = operand.getValue(registers) % 8
                    null to registers.copy(second = answer)
                }

                OpCode.JNZ -> {
                    if (registers.a() == 0) null to registers
                    else throw JumpException(operand.getValue(registers, LITERAL))
                }

                OpCode.BXC -> {
                    val answer = registers.b() xor registers.c()
                    null to registers.copy(second = answer)
                }

                OpCode.OUT -> {
                    (operand.getValue(registers) % 8).toString() to registers
                }

                OpCode.BDV -> {
                    val nomenator = registers.a()
                    val denomenator = 2.0.pow(operand.getValue(registers))
                    val answer = (nomenator / denomenator).toInt()
                    null to registers.copy(second = answer)
                }

                OpCode.CDV -> {
                    val nomenator = registers.a()
                    val denomenator = 2.0.pow(operand.getValue(registers))
                    val answer = (nomenator / denomenator).toInt()
                    null to registers.copy(third = answer)
                }
            }
    }

    enum class OpCode(val opCodeValue: Int) {
        ADV(0),
        BXL(1),
        BST(2),
        JNZ(3),
        BXC(4),
        OUT(5),
        BDV(6),
        CDV(7);

        companion object {
            fun fromValue(value: Int): OpCode = when (value) {
                0 -> ADV
                1 -> BXL
                2 -> BST
                3 -> JNZ
                4 -> BXC
                5 -> OUT
                6 -> BDV
                7 -> CDV
                else -> error("weird instruction value $value")
            }
        }
    }

    data class Operand(val value: Int) {
        fun getValue(registers: Registers, operandType: OperandType = OperandType.COMBO): Int =
            if (operandType == OperandType.LITERAL) value
            else when (value) {
                in 0..3 -> value
                4 -> registers.a()
                5 -> registers.b()
                6 -> registers.c()
                else -> error("Oops you used a reserved operand")
            }
    }

    fun Registers.a() = this.first
    fun Registers.b() = this.second
    fun Registers.c() = this.third

    class JumpException(val jumps: Int) : Exception("Jump little computer, jump!")
}

enum class OperandType {
    LITERAL, COMBO
}
typealias Registers = Triple<Int, Int, Int>