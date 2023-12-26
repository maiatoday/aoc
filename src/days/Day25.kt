package days

import io.github.rchowell.dotlin.graph
import java.io.File

object Day25 : Day<Long, List<String>> {
    override val number: Int = 25
    override val expectedPart1Test: Long = 54L
    override val expectedPart2Test: Long = -1L
    override var useTestData = false
    override val debug = false

    val dotFileName = if (useTestData) "test25.dot" else "Day25.dot"

    val nodesToCut = if (useTestData) {
        // hfx/pzl, the wire between bvb/cmg, and the wire between nvd/jqt
        listOf(
                "hfx" to "pzl",
                "bvb" to "cmg",
                "nvd" to "jqt",
        )
    } else {
        listOf(
                "zhb" to "vxr",
                "jbx" to "sml",
                "szh" to "vqj",
        )
    }

    // using graphviz to get the nodes to cut and then mapping on the edges
    // something like neato -Tpng Day25.dot -o Day25.png
    // open Day25.png
    override fun part1(input: List<String>): Long {
        val circuit = parse(input)
        // val graph = circuit.toDot()
        // File(dotFileName).writeText(graph.dot())
        //  val orig = circuit.toEdges(emptyList())
        val edges = circuit.toEdges(nodesToCut)
        // File("split.dot").writeText(edges.toDot().dot())
        return (edges.connected(nodesToCut.first().first).size * edges.connected(nodesToCut.first().second).size).toLong()
    }

    private fun parse(input: List<String>) = buildMap {
        for (s in input) {
            val (key, rest) = s.split(":")
            val connections = rest.trim().split(" ")
            put(key, connections)
        }
    }

    private fun Map<String, List<String>>.toDot() =
            graph {
                val edges = this@toDot.toEdges(emptyList())
                for ((k, n) in edges)
                    k - n
            }

    private fun List<Pair<String, String>>.toDot() =
            graph {
                for ((k, n) in this@toDot)
                    k - n
            }


    private fun Map<String, List<String>>.toEdges(cuts: List<Pair<String, String>>) = buildList {
        for ((k, v) in this@toEdges) {
            for (n in v) {
                if (k to n !in cuts && n to k !in cuts)
                    add(k to n)
            }
        }
    }

    private fun List<Pair<String, String>>.connected(node: String): Set<String> {
        val seen = mutableListOf<Pair<String, String>>()
        val start = this.filter { it.first == node || it.second == node }
        val queue = mutableListOf<Pair<String, String>>()
        queue.addAll(start)
        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            if (current !in seen) {
                seen.add(current)
                queue.addAll(this.filter { sharedNode(current, it) })
            }
        }
        val connected = seen.flatMap { it.toList() }.toSet()
        return connected
    }


    private fun sharedNode(one: Pair<String, String>, other: Pair<String, String>): Boolean =
            (one.first == other.first && one.second != other.second) ||
                    (one.second == other.first && one.first != other.second) ||
                    (one.first == other.second && one.second != other.first) ||
                    (one.second == other.second && one.first != other.first)


    override fun part2(input: List<String>): Long {
        return -1L
    }
}
