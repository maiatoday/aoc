package days

import util.Area
import util.Point
import util.debug
import util.neighbours

object Day12 : Day<Long, List<String>> {
    override val number: Int = 12
    override val expectedPart1Test: Long = 1930L
    override val expectedPart2Test: Long = -1L
    override var useTestData = true
    override val debug = false

    override fun part1(input: List<String>): Long {
        val garden = readGardenMap(input)
        val area = Area(input)
        val regions = splitRegions(garden, area)
        val answers = regions.map { it.calculate(garden, area) }
        return answers.sum().toLong()
    }

    private fun splitRegions(garden: Map<Point, Char>, area: Area): List<Region> {
        val regions = mutableListOf<Region>()
        val q = garden.map { it.key to it.value }.toMutableList()
        while (q.isNotEmpty()) {
            val (point, crop) = q.removeFirst()
            if (regions.any { it.cropType == crop && point in it.points }) continue
            val newRegion = Region(crop).also { it.points.add(point) }

            val floodQ = mutableListOf<Point>()
            floodQ.add(point)
            val visited = mutableListOf<Point>()
            while (floodQ.isNotEmpty()) {
                val newCurrent = floodQ.removeFirst()
                if (newCurrent in visited) continue
                else visited.add(newCurrent)
                val validNeighbours = newCurrent.neighbours(area, includeSelf = false).filter { garden[it] == crop }
                newRegion.points.addAll(validNeighbours)
                floodQ.addAll(validNeighbours)
            }

            regions.add(newRegion)
//            if (!useTestData) {
//                newRegion.points.toList().debug(filled = newRegion.cropType.toString(), empty = ".", area)
//                println()
//            }
        }

        return regions
    }

    //1419673 too low 1358705 also too low 1347608 1437584 not right

    data class Region(val cropType: Char) {
        // do I need a list of points? yes
        val points = mutableSetOf<Point>()
        var regionArea = 0
        var perimeter = 0

        fun calculate(garden: Garden, gardenArea: Area): Int {
            if (perimeter == 0) {
                for (p in points) {
                    perimeter += p.neighbours(
                        includeSelf = false,
                        onlyPositive = false,
                    ).count { garden[it] != cropType || it.x !in gardenArea.xRange || it.y !in gardenArea.yRange }

                }
            }
            regionArea = points.size
            println("A region of ${cropType} plants with price $regionArea * $perimeter = ${regionArea * perimeter}.")
            this.points.toList().debug(filled = this.cropType.toString(), empty = ".", gardenArea)
            return regionArea * perimeter
        }

    }

    private fun readGardenMap(input: List<String>): Garden = buildMap {
        input.mapIndexed { y, s ->
            s.mapIndexed { x, c ->
                put(Point(x, y), c)
            }
        }
    }

    override fun part2(input: List<String>): Long {
        return -1L
    }
}
typealias Garden = Map<Point, Char>