package days

import util.Area
import util.Point
import util.RoseDirections
import util.neighbours

object Day12 : Day<Long, List<String>> {
    override val number: Int = 12
    override val expectedPart1Test: Long = 1930L
    override val expectedPart2Test: Long = 1206L
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
        }

        return regions
    }

    data class Region(val cropType: Char) {
        // do I need a list of points? yes
        val points = mutableSetOf<Point>()

        fun calculate(garden: Garden, gardenArea: Area): Int =
            points.sumOf {
                it.neighbours(includeSelf = false, onlyPositive = false)
                    .count { garden[it] != cropType || it.x !in gardenArea.xRange || it.y !in gardenArea.yRange }
            } * points.size

        fun calculateWithDiscount(garden: Garden, gardenArea: Area): Int =
            points.sumOf { it.corners(cropType, garden, gardenArea) } * points.size

    }

    private fun Point.corners(cropType: Char, garden: Garden, gardenArea: Area): Int {
        // a polygon has as many edges as it has corners
        var count = 0

        // make an array of booleans to know if the neighbours are the same crop or not. True if crop is different or edge
        val neighbourDifferent = this.neighbours(includeSelf = false, onlyPositive = false, diagonal = true)
            .map { (garden[it] != cropType || it.x !in gardenArea.xRange || it.y !in gardenArea.yRange) }

        // The test is for each direction if e.g. North and East are both different it is an outside corner
        // If e.g. North and East ist the same but the diagonal is different it is an inside corner
        if ((neighbourDifferent[NORTH] && neighbourDifferent[EAST]) ||
            (neighbourDifferent[NORTH_EAST] && !neighbourDifferent[NORTH] && !neighbourDifferent[EAST])
        ) count++

        if ((neighbourDifferent[NORTH] && neighbourDifferent[WEST]) ||
            (neighbourDifferent[NORTH_WEST] && !neighbourDifferent[NORTH] && !neighbourDifferent[WEST])
        ) count++

        if ((neighbourDifferent[SOUTH] && neighbourDifferent[EAST]) ||
            (neighbourDifferent[SOUTH_EAST] && !neighbourDifferent[SOUTH] && !neighbourDifferent[EAST])
        ) count++

        if ((neighbourDifferent[SOUTH] && neighbourDifferent[WEST]) ||
            (neighbourDifferent[SOUTH_WEST] && !neighbourDifferent[SOUTH] && !neighbourDifferent[WEST])
        ) count++

        return count
    }

    private fun readGardenMap(input: List<String>): Garden = buildMap {
        input.mapIndexed { y, s ->
            s.mapIndexed { x, c ->
                put(Point(x, y), c)
            }
        }
    }

    override fun part2(input: List<String>): Long {
        val garden = readGardenMap(input)
        val area = Area(input)
        val regions = splitRegions(garden, area)
        val answers = regions.map { it.calculateWithDiscount(garden, area) }
        return answers.sum().toLong()
    }

    val NORTH = RoseDirections.N.index
    val SOUTH = RoseDirections.S.index
    val EAST = RoseDirections.E.index
    val WEST = RoseDirections.W.index
    val NORTH_WEST = RoseDirections.NW.index
    val NORTH_EAST = RoseDirections.NE.index
    val SOUTH_WEST = RoseDirections.SW.index
    val SOUTH_EAST = RoseDirections.SE.index
}

typealias Garden = Map<Point, Char>