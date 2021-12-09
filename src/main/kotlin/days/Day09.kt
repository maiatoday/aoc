// Day object template
object Day09 {

    fun part1(input: List<String>): Int {
        val heightMap = input.map { it.trim().toCharArray().map { c -> c.toString().toInt() } }
        val lowPoints = findLowPoints(heightMap)
        return lowPoints.sumOf { it.value + 1 }
    }

    fun part2(input: List<String>): Int {
        val heightMap = input.map { it.trim().toCharArray().map { c -> c.toString().toInt() } }
        val lowPoints = findLowPoints(heightMap)
        val basins: List<Set<Location>> = lowPoints.map { findRootBasin(it, heightMap) }
        val sizes = basins.map { it.size }.sorted().takeLast(3)
        return sizes.fold(1) { acc, num -> acc * num }
    }

    private fun findLowPoints(heightMap: List<List<Int>>): List<Location> {
        val lowPoints = mutableListOf<Location>()
        val rowWidth = heightMap.first().size
        for (y in heightMap.indices) {
            for (x in heightMap.first().indices) {
                val up = if (y == 0) Integer.MAX_VALUE else heightMap[y - 1][x]
                val down = if (y == heightMap.size - 1) Integer.MAX_VALUE else heightMap[y + 1][x]
                val left = if (x == 0) Integer.MAX_VALUE else heightMap[y][x - 1]
                val right = if (x == rowWidth - 1) Integer.MAX_VALUE else heightMap[y][x + 1]
                val point = heightMap[y][x]
                if ((point < up) and (point < down) and (point < left) and (point < right)) {
                    lowPoints.add(Location(point, x, y))
                }
            }
        }
        return lowPoints
    }

    data class Location(val value: Int, val x: Int, val y: Int)
    private const val BORDER = 9

    private fun findRootBasin(lowPoint: Location, heightMap: List<List<Int>>): Set<Location> {
        val basin = mutableSetOf<Location>()
        return findBasin(heightMap, listOf(lowPoint), basin)
    }

    private fun findBasin(
        heightMap: List<List<Int>>,
        points: List<Location>,
        basin: MutableSet<Location>
    ): Set<Location> =
        if (points.isEmpty()) basin
        else {
            val newNeighbours = mutableListOf<Location>()
            for (point in points) {
                basin.add(point)
                newNeighbours.addAll(basinNeighbours(heightMap, point, basin))
            }
            findBasin(heightMap, newNeighbours, basin)
        }


    private fun basinNeighbours(heightMap: List<List<Int>>, point: Location, basin: Set<Location>): List<Location> {
        val neighbours = buildList<Location> {
            add(
                Location(
                    value = if (point.y == 0) BORDER else heightMap[point.y - 1][point.x],
                    x = point.x,
                    y = point.y - 1
                )
            )
            add(
                Location(
                    value = if (point.y == heightMap.size - 1) BORDER else heightMap[point.y + 1][point.x],
                    x = point.x,
                    y = point.y + 1
                )
            )
            add(
                Location(
                    value = if (point.x == 0) BORDER else heightMap[point.y][point.x - 1],
                    x = point.x - 1,
                    y = point.y
                )
            )
            add(
                Location(
                    value = if (point.x == heightMap.first().size - 1) BORDER else heightMap[point.y][point.x + 1],
                    x = point.x + 1,
                    y = point.y
                )
            )
        }
        return neighbours.filter { (it.value != 9) and (!basin.contains(it)) }
    }

}
