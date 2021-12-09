// Day object template
object Day09 {

    fun part1(input: List<String>): Int {
        val heightMap = input.map { it.trim().toCharArray().map { c -> c.toString().toInt() } }
        val lowPoints = findLowPoints(heightMap)
        return lowPoints.sumOf { it.value + 1 }
    }

    private fun findLowPoints(heightMap: List<List<Int>>): List<LowPoint> {
        val lowPoints = mutableListOf<LowPoint>()
        val rowWidth = heightMap.first().size
        for (y in heightMap.indices) {
            for (x in heightMap.first().indices) {
                val up = if (y == 0) Integer.MAX_VALUE else heightMap[y - 1][x]
                val down = if (y == heightMap.size - 1) Integer.MAX_VALUE else heightMap[y + 1][x]
                val left = if (x == 0) Integer.MAX_VALUE else heightMap[y][x - 1]
                val right = if (x == rowWidth - 1) Integer.MAX_VALUE else heightMap[y][x + 1]
                val point = heightMap[y][x]
                if ((point < up) and (point < down) and (point < left) and (point < right)) {
                    lowPoints.add(LowPoint(point, x, y))
                }
            }
        }
        return lowPoints
    }

    data class LowPoint(val value: Int, val x: Int, val y: Int)

    fun part2(input: List<String>): Int {
        val heightMap = input.map { it.trim().toCharArray().map { c -> c.toString().toInt() } }
        val lowPoints = findLowPoints(heightMap)
        val basins: List<Set<LowPoint>> = lowPoints.map { findBasin(it, heightMap) }
        val sizes = basins.map { it.size }.sorted().takeLast(3)
        return sizes.fold(1) { acc, num -> acc * num }
    }

    private fun basinNeighbours(heightMap: List<List<Int>>, point: LowPoint): List<LowPoint> {
        val neighbours = buildList<LowPoint> {
            add(
                LowPoint(
                    value = if (point.y == 0) BORDER else heightMap[point.y - 1][point.x],
                    x = point.x,
                    y = point.y - 1
                )
            )
            add(
                LowPoint(
                    value = if (point.y == heightMap.size - 1) BORDER else heightMap[point.y + 1][point.x],
                    x = point.x,
                    y = point.y + 1
                )
            )
            add(
                LowPoint(
                    value = if (point.x == 0) BORDER else heightMap[point.y][point.x - 1],
                    x = point.x - 1,
                    y = point.y
                )
            )
            add(
                LowPoint(
                    value = if (point.x == heightMap.first().size - 1) BORDER else heightMap[point.y][point.x + 1],
                    x = point.x + 1,
                    y = point.y
                )
            )
        }

        return neighbours.filter { it.value != 9 }
    }

    private const val BORDER = 9

    private fun findBasin(
        lowPoint: LowPoint,
        heightMap: List<List<Int>>
    ): Set<LowPoint> {
        val basin = mutableSetOf<LowPoint>()
        val firstRow = findBasinRow(lowPoint, heightMap)
        val firstColumn = findBasinColumn(lowPoint, heightMap)
        for (point in firstRow) {
            basin.addAll(findBasinColumn(point, heightMap))
        }
        for (point in firstColumn) {
            basin.addAll(findBasinRow(point, heightMap))
        }
        return basin
    }

    private fun findBasinRow(
        lowPoint: LowPoint,
        heightMap: List<List<Int>>,
    ): Set<LowPoint> {
        val basin = mutableSetOf<LowPoint>()
        for (left in lowPoint.x downTo 0) {
            val currentPoint = LowPoint(heightMap[lowPoint.y][left], y = lowPoint.y, x = left)
            if (currentPoint.value != 9) {
                basin.add(currentPoint)
            } else {
                break
            }
        }
        for (right in lowPoint.x until heightMap.first().size) {
            val currentPoint = LowPoint(heightMap[lowPoint.y][right], x = right, lowPoint.y)
            if (currentPoint.value != 9) {
                basin.add(currentPoint)
            } else {
                break
            }
        }
        return basin
    }

    private fun findBasinColumn(
        lowPoint: LowPoint,
        heightMap: List<List<Int>>,
    ): Set<LowPoint> {
        val basin = mutableSetOf<LowPoint>()
        for (above in lowPoint.y downTo 0) {
            val currentPoint = LowPoint(heightMap[above][lowPoint.x], y = above, x = lowPoint.x)
            if (currentPoint.value != 9) {
                basin.add(currentPoint)
            } else {
                break
            }
        }
        for (below in lowPoint.y until heightMap.size) {
            val currentPoint = LowPoint(heightMap[below][lowPoint.x], x = lowPoint.x, y = below)
            if (currentPoint.value != 9) {
                basin.add(currentPoint)
            } else {
                break
            }
        }
        return basin
    }


}
