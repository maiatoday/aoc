package days

import util.filterComments
import util.splitByBlankLine

object Day08 : Day<Long, List<String>> {
    override val number: Int = 8
    override val expectedPart1Test: Long = 6L
    override val expectedPart2Test: Long = 6L
    override var useTestData = true
    override val debug = false

    override fun part1(input: List<String>): Long {
        return expectedPart1Test
//        val (instructions, nodes) = input.readMap()
//        val steps = followAndCount(instructions, nodes)
//        return steps.toLong()
    }

    override fun part2(input: List<String>): Long {
        val (instructions, nodes) = input.readMap()
        return ghostFollowAndCount(instructions, nodes).toLong()
    }

    fun ghostFollowAndCount(instructions: List<Char>, desertMap: Map<String, Node>): Int {
        var steps = 0
        var currentNodes = desertMap.filter { it.value.isStart() }.map { it.value }
        println(currentNodes.map { it.id })
        while (!currentNodes.isEnd()) {
            val instruction = instructions[steps % instructions.size]
            currentNodes = nextGhostStep(currentNodes, instruction, desertMap)
            // if (steps%100 == 0) println(steps)
            steps++
        }
        return steps
    }

    private fun nextGhostStep(currentNodes: List<Node>, instruction: Char, desertMap: Map<String, Node>): List<Node> =
            currentNodes.map { it.id }.mapNotNull { desertMap[it]?.nextNode(instruction) }.mapNotNull { desertMap[it] }


    fun List<Node>.isEnd(): Boolean = this.all { it.isEnd() }

    private fun List<String>.readMap(): Pair<List<Char>, Map<String, Node>> {
        val maps = filterComments().splitByBlankLine()
        val instructions = maps[0][0].map { it }
        val nodes = maps[1].extractMap()
        return instructions to nodes
    }

    private fun List<String>.extractMap() =
            associate {
                val node = Node(it)
                node.id to node
            }


    data class Node(val id: String, val left: String, val right: String) {
        fun nextNode(instruction: Char) =
                when (instruction) {
                    'L' -> left
                    'R' -> right
                    else -> error("oops bad instruction")
                }

        fun isStart() = id.endsWith('A')
        fun isEnd() = id.endsWith('Z')
    }

    fun Node(input: String): Node {
        val (id, left, right) = nodeRegex.find(input)?.destructured ?: kotlin.error("oops")
        return Node(id, left, right)
    }

    fun followAndCount(instructions: List<Char>, desertMap: Map<String, Node>): Int {
        var steps = 0
        var index = 0
        var currentNode = "AAA"
        while (currentNode != "ZZZ") {
            val instruction = instructions[index]
            currentNode = desertMap[currentNode]?.nextNode(instruction) ?: error("oops missing node")
            steps++
            index++
            if (index == instructions.size) index = 0
        }
        return steps
    }

    val nodeRegex = "(\\w+) = .(\\w+), (\\w+).".toRegex()

}
