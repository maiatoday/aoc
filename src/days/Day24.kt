package days

import util.splitByBlankLine
import java.math.BigInteger

object Day24 : Day<String, List<String>> {
    override val number: Int = 24
    override val expectedPart1Test: String = "2024"
    override val expectedPart2Test: String = "-1"
    override var useTestData = true
    override val debug = false

    override fun part1(input: List<String>): String {
        val answer = evaluate(parseCircuit(input))
        return answer
    }

    private fun parseCircuit(input: List<String>): Pair<List<Wire>, List<Connection>> {
        val (i, c) = input.splitByBlankLine()
        val wires = i.map { Wire(it) }
        val connections = c.map { Connection(it) }
        return wires to connections
    }

    fun Wire(config: String): Wire {
        val (l, i) = config.split(": ")
        return Wire(l, i.toInt())
    }

    data class Wire(val label: String, val initial: Int)

    fun Connection(config: String): Connection {
        val (ii, output) = config.split(" -> ")
        val inputs = ii.split(" ").toMutableList()
        val operation = inputs[1].toOperation()
        return Connection(inputLabel = listOf(inputs[0], inputs[2]), operation = operation, outputLabel = output)
    }

    val state: MutableMap<String, Int> = mutableMapOf()

    data class Connection(val inputLabel: List<String>, val outputLabel: String, val operation: Operation) {

        fun evaluate() {
            if (state[inputLabel[0]] != null && state[inputLabel[1]] != null) {
                val i = state[inputLabel[0]] ?: error("oops")
                val j = state[inputLabel[1]] ?: error("oops")
                if (debug) println("evaluating ${inputLabel[0]}  $operation ${inputLabel[1]}")
                state.getOrPut(outputLabel) { operation.doIt(listOf(i, j)) }
            } else {
                error("Oops Oops Oops")
            }
        }
    }

    enum class Operation {
        AND, OR, XOR;

        fun doIt(operands: List<Int>): Int =
            when (this) {
                AND -> operands[0] and operands[1]
                OR -> operands[0] or operands[1]
                XOR -> (operands[0] xor operands[1]) and 1
            }
    }

    fun String.toOperation() =
        when (this) {
            Operation.AND.name -> Operation.AND
            Operation.OR.name -> Operation.OR
            Operation.XOR.name -> Operation.XOR
            else -> error("Unknown operation")
        }

    fun evaluate(setup: Pair<List<Wire>, List<Connection>>): String {
        val (wires, connections) = setup
        for (w in wires) {
            state[w.label] = w.initial
        }
        val initialisedWires = wires.map { it.label }
        val q = mutableListOf<Connection>()
        connections.filter { it.inputLabel[0] in initialisedWires && it.inputLabel[1] in initialisedWires }
            .forEach { q.add(it) }
        while (q.isNotEmpty()) {
            val peek = q.last()
            val i = peek.inputLabel[0]
            val j = peek.inputLabel[1]
            if (state[i] != null && state[j] != null) {
                val current = q.removeLast()
                try {
                    current.evaluate()
                    connections.filter { it.inputLabel[0] == current.outputLabel || it.inputLabel[1] == current.outputLabel }
                        .forEach { q.add(it) }
                } catch (e: Exception) {
                    PRINTLN("SOME WEIRDNESS")
                }
            } else {
                val x = q.removeLast()
                q.add(0, x)
            }

        }
        val theOutputs = state.keys.filter { it.startsWith("z") }.sorted().reversed()
        println("The outputs $theOutputs")
        println("The ouputs values ${theOutputs.map { state[it] }}")
        val theAnswer = theOutputs.joinToString("") { state[it].toString() }
        println(theAnswer)
        val bi = BigInteger(theAnswer, 2)
        println("The answer in decimal $bi")
        return bi.toString(10)
        // 48510377256936 too high 48510377256936
    }

    override fun part2(input: List<String>): String {
        return "-1"
    }
}


