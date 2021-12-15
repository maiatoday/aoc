import java.util.*

object Day15 {

    data class Point(val r: Int, val c: Int)
    data class Node(val cost: Int, val point: Point)

    fun part1(input: List<String>): Long {
        val caveMap = readCaveMap(input)
        return findMinPathRisk(caveMap).toLong()
    }

    fun part2(input: List<String>): Long {
        val caveMap = readCaveMap(input)
        return findMinPathRisk(caveMap, 5).toLong()
    }

    private fun findMinPathRisk(
        caveMap: List<List<Int>>,
        multiplier: Int = 1
    ): Int {
        val queue = PriorityQueue(compareBy(Node::cost))
        val visited: MutableSet<Point> = mutableSetOf()
        val maxRowOrig = caveMap.size
        val maxColOrig = caveMap.first().size
        val maxRow = maxRowOrig * multiplier
        val maxCol = maxColOrig * multiplier
        val costMap = initCostMap(maxRow, maxCol)
        queue.add(Node(0, Point(0, 0)))
        loop@ while (queue.isNotEmpty()) {
            val node = queue.poll()
            if (!visited.contains(node.point)) {
                visited.add(node.point)
                costMap[node.point.r][node.point.c] = node.cost
                if ((node.point.r == maxRow - 1) and (node.point.c == maxCol - 1)) break@loop
                findNeighbours(maxRow, maxCol, node.point).forEach {
                    queue.add(
                        Node(
                            node.cost + calculateCost(
                                caveMap,
                                it.r,
                                it.c,
                                maxRowOrig, maxColOrig
                            ), Point(it.r, it.c)
                        )
                    )
                }
            }
        }
        return costMap[maxRow - 1][maxCol - 1]
    }

    private fun calculateCost(caveMap: List<List<Int>>, r: Int, c: Int, maxR: Int, maxC: Int): Int {
        // grid of repeating blocks cost is calculated
        // first take the row and column and work back to find the original row column in the caveMap
        // then figure out how many times to increment depending on which block across or down the point is
        // each block across adds one and each block down adds 1
        // if at any time the value goes to 10 wrap it around to 1
        val origR = r % maxR
        val scaleR = r / maxR
        val origC = c % maxC
        val scaleC = c / maxC
        var cost = caveMap[origR][origC]
        repeat(scaleR) {
            cost++
            if (cost == 10) cost = 1
        }
        repeat(scaleC) {
            cost++
            if (cost == 10) cost = 1
        }
        return cost
    }

    private fun findNeighbours(maxRow: Int, maxCol: Int, point: Point): List<Point> =
        buildList {
            listOf(
                Point(0, 1),
                Point(0, -1),
                Point(1, 0),
                Point(-1, 0)
            ).forEach {
                val nr = point.r + it.r
                val nc = point.c + it.c
                // only add neighbours that are in range
                if ((nr >= 0) and (nr < maxRow) and (nc >= 0) and (nc < maxCol))
                    add(Point(nr, nc))
            }
        }

    private fun initCostMap(maxRow: Int, maxCol: Int): MutableList<MutableList<Int>> {
        val costMap = mutableListOf<MutableList<Int>>()
        repeat(maxRow) {
            val row = mutableListOf<Int>()
            repeat(maxCol) {
                row.add(0)
            }
            costMap.add(row)
        }
        return costMap
    }

    private fun readCaveMap(input: List<String>) =
        input.map { s -> s.trim().toCharArray().map { c -> c.toString().toInt() } }
    
}
