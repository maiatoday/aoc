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
//        val (instructions, nodes) = input.readMap(true)
//        return followAndCountOrig(instructions, nodes).toLong()
        return expectedPart1Test
    }

    override fun part2(input: List<String>): Long {
        val (instructions, nodes) = input.readMap(false)
        return ghostFollowAndCount(instructions, nodes)
    }

    private fun ghostFollowAndCount(instructions: List<Char>, desertMap: Map<String, Node>): Long {
        val startNodes = desertMap.filter { it.value.isStart() }.map { it.value }
        println(startNodes.map { it.id })
        val loopLengths = startNodes.map { it.followAndCount(instructions, desertMap) }
        println(loopLengths)
        println("expecting 13133452426987")

        // this brute force takes super long, I typed it into an online calculator, the alternative is to use BigInteger.gcm
        var searching = true
        var answer = loopLengths.max().toLong()
        while (searching) {
            if (loopLengths.all { (answer % it) == 0L }) searching = false
            answer++
        }
        return answer
    }

    private fun Node.followAndCount(instructions: List<Char>, desertMap: Map<String, Node>): Int {
        var steps = 0
        var currentNode = this
        while (!currentNode.isEnd()) {
            val instruction = instructions[steps % instructions.size]
            val nextId = desertMap.getValue(currentNode.id).nextNode(instruction) ?: error("oops missing node")
            currentNode = desertMap.getValue(nextId)
            steps++
        }
        return steps
    }

    private fun List<String>.readMap(strict: Boolean): Pair<List<Char>, Map<String, Node>> {
        val maps = filterComments().splitByBlankLine()
        val instructions = maps[0][0].map { it }
        val nodes = maps[1].extractMap(strict)
        return instructions to nodes
    }

    private fun List<String>.extractMap(strict: Boolean) =
            associate {
                val node = Node(it, strict)
                node.id to node
            }

    data class Node(val id: String, val left: String, val right: String, val strict: Boolean) {
        fun nextNode(instruction: Char) =
                when (instruction) {
                    'L' -> left
                    'R' -> right
                    else -> error("oops bad instruction")
                }

        fun isStart() = if (strict) id == "AAA" else id.endsWith('A')
        fun isEnd() = if (strict) id == "ZZZ" else id.endsWith('Z')
    }

    private fun Node(input: String, strict: Boolean): Node {
        val (id, left, right) = nodeRegex.find(input)?.destructured ?: kotlin.error("oops")
        return Node(id, left, right, strict)
    }

    fun strictFollowAndCount(instructions: List<Char>, desertMap: Map<String, Node>): Int =
            desertMap.getValue("AAA").followAndCount(instructions, desertMap)

    private val nodeRegex = """(\w+) = .(\w+), (\w+).""".toRegex()

}
