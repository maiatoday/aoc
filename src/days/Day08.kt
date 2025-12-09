package days

import util.Point3
import util.toPoint3
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

object Day08 : Day<Long, List<String>> {
    override val number: Int = 8
    override val expectedPart1Test: Long = 40L // 10 circuits - test, 1000 circuits real data
    override val expectedPart2Test: Long = -1L
    override var useTestData = true
    override val debug = false
    var maxConnections = 10
    private fun setMaxCircuits() {
        if (useTestData) {
            maxConnections = 10
        } else {
            maxConnections = 1000
        }
    }

    override fun part1(input: List<String>): Long {
        setMaxCircuits()
        val junctions = input.map { it.toPoint3() }
        val pairs = junctions.withIndex().flatMap { (i1, j1) ->
            junctions.withIndex().filter { (i2, _) -> i1 != i2 }
                .map { (_, j2) -> j1 to j2 }
        }
        val edges = pairs.map { Edge(it) }.sortedBy { it.distance }

        // connect $maxConnections closest junctions together
        val topClosestEdges = edges.take(maxConnections)

        val circuits = buildList {
            val closestJunctions = topClosestEdges.flatMap { listOf(it.first, it.second) }.toSet()
            val firstSweep = buildList {
                for (j in closestJunctions) {
                    add(topClosestEdges.filter { j == it.first || j == it.second })
                }
            }
            //TODO this is wrong need to build the circuits
            firstSweep.forEach { s ->
                val cc = s.flatMap { point ->
                    firstSweep.filter { point in it }
                }
                add(cc.distinct())
            }
        }
        val sortedCircuits = circuits.sortedBy { it.size }.reversed().take(3)

        // calculate the size of the circuits
        // add together the size of the 3 largest circuits
        return sortedCircuits.fold(1) { total, item -> total * item.size}.toLong()
    }

    override fun part2(input: List<String>): Long {
        return -1L
    }

    fun Edge(pair: Pair<Point3, Point3>): Edge = Edge(pair.first, pair.second, pair.first.distance(pair.second))
    data class Edge(val first: Point3, val second: Point3, val distance: Double)

    fun Point3.distance(other: Point3) = sqrt(
        abs(this.x.toDouble() - other.x).pow(2) +
                abs(this.y.toDouble() - other.y).pow(2) +
                abs(this.z.toDouble() - other.z)
    )
}