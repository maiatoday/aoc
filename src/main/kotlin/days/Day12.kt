package days

import util.findAllInGrid
import util.findInGrid
import util.neighbours

typealias Point = Pair<Int, Int>
typealias Terrain = List<List<Int>>
typealias PathMap = MutableMap<Point, Int>

data class PointPath(val point: Point, val path: Int)

object Day12 : Day<Long, List<String>> {
    override val number: Int = 12
    override val expectedPart1Test: Long = 31L
    override val expectedPart2Test: Long = 29L

    override fun part1(input: List<String>): Long {
        val terrain = input.toTerrain()
        val start = input.findInGrid("S")
        val end = input.findInGrid("E")
        val pathMap = findPath(terrain, start, end)
        return pathMap[start]?.toLong() ?: -1L
    }

    private fun findPath(terrain: Terrain, start: Point, end: Point): PathMap {
        val pathMap = mutableMapOf<Point, Int>()
        val spotsToCheck: MutableList<PointPath> = mutableListOf()
        spotsToCheck.add(PointPath(end, 0))
        val maxM = terrain.size
        val maxN = terrain.first().size
        checkSpots@ while (spotsToCheck.isNotEmpty()) {
            val xSpotN = spotsToCheck.first()
            spotsToCheck.remove(xSpotN)
            val xSpot = xSpotN.point
            if (xSpot == start) break@checkSpots
            val currentPathLength = pathMap[xSpot] ?: 0
            val currentHeight = terrain[xSpot.first][xSpot.second]
            val neighbours = xSpot.neighbours(maxM, maxN)
            neighbours.filter {
                (currentHeight - terrain[it.first][it.second]) <= 1
            }
                .forEach {
                    val newPathLength = currentPathLength + 1
                    val oldPathLength = pathMap[it] ?: -1
                    if (pathMap.contains(it)) {
                        if (oldPathLength > newPathLength) {
                            pathMap[it] = newPathLength
                            spotsToCheck.add(PointPath(it, newPathLength))
                        }
                    } else {
                        pathMap[it] = newPathLength
                        spotsToCheck.add(PointPath(it, newPathLength))
                    }
                }
            // }
        }
        return pathMap
    }


    override fun part2(input: List<String>): Long {
        val end = input.findInGrid("E")
        val allStart = mutableListOf<Point>()
        allStart.add(input.findInGrid("S"))
        allStart.addAll(input.findAllInGrid("a"))
        val terrain = input.toTerrain()
        val pathLengths = allStart.map { start -> findPath(terrain, start, end)[start] ?: Int.MAX_VALUE }
        return pathLengths.min().toLong()
    }


    private fun List<String>.toTerrain(): Terrain =
        this.map { m ->
            m.map { n ->
                when (n) {
                    'S' -> 'a'.toHeight()
                    'E' -> 'z'.toHeight()
                    else -> n.toHeight()
                }
            }
        }


    private fun Char.toHeight() = this - 'a'

}
