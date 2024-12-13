package days

import util.Area
import util.Point
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
        for ((point, crop) in garden) {
            // one of the existing regions -> add it to the list of points in that region
            // is this a new region -> make a new one
            if (regions.none { it.cropType == crop }) {
                regions.add(Region(crop).also { it.points.add(point) })
            }
            var searchPoint = point
            val connectedPoints: MutableList<Point> = mutableListOf()
            while (garden[searchPoint] == crop) { // if I go outside the garden this will fail so we will break
                connectedPoints += searchPoint.neighbours(area, includeSelf = true).filter { garden[it] == crop }
                searchPoint = searchPoint.copy(x = searchPoint.x + 1)
            }
            val connectedRegion = regions.firstOrNull() {
                it.cropType == crop && it.points.any { it in connectedPoints }
            } //  massive assumption that my code works! and there is a region and only one that is connected
            if (connectedRegion == null) {
                // there was a region of the same crop type but we are not connected to it
                // so this is a brand new one
                regions.add(Region(crop).also { it.points.add(point) })
            } else {
                //println("Adding ${connectedPoints.size} connecting points to region ${connectedRegion.cropType}")
                connectedRegion.points.addAll(connectedPoints)
            }
        }
        return regions
    }

    //1419673 too low

    data class Region(val cropType: Char) {
        // do I need a list of points? yes
        val points = mutableSetOf<Point>()
        var regionArea = 0
        var perimeter = 0

        fun calculate(garden: Garden, gardenArea: Area): Int {
            if (perimeter == 0) {
                for (p in points) {
                    val nonCropNeighbours = p.neighbours(
                        includeSelf = false,
                        onlyPositive = false,
                    ).filter { garden[it] != cropType || it.x !in gardenArea.xRange || it.y !in gardenArea.yRange }
                    perimeter += p.neighbours(
                        includeSelf = false,
                        onlyPositive = false,
                    ).count { garden[it] != cropType || it.x !in gardenArea.xRange || it.y !in gardenArea.yRange }

                }
            }
            regionArea = points.size
           // println("A region of ${cropType} plants with price $regionArea * $perimeter = ${regionArea * perimeter}.")
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