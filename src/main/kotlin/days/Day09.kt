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
        val basins: List<List<Int>> = lowPoints.map { findBasin(it, heightMap) }
        val sizes = basins.map { it.size }.sorted().take(3)
        //{ it.size }.sorted().take(3).sum()
        return sizes.sum()
    }

    private fun findBasin(lowPoint: LowPoint, heightMap: List<List<Int>>): List<Int> {
        val basin = mutableListOf<Int>()
        var prevPoint = lowPoint.value
        for (above in lowPoint.y downTo 0) {
            val currentPoint = heightMap[above][lowPoint.x]
            if ((prevPoint<=currentPoint) and  (currentPoint != 9)) {
                basin.add(currentPoint)
                prevPoint = currentPoint
            } else {
                basin.removeFirst() // we added the currentPoint
                break
            }
        }
        prevPoint = lowPoint.value
        for (left in lowPoint.x downTo 0) {
            val currentPoint = heightMap[lowPoint.y][left]
            if ((prevPoint<=currentPoint) and  (currentPoint != 9)) {
                basin.add(currentPoint)
                prevPoint = currentPoint
            } else {
                basin.removeFirst() // we added the currentPoint
                break
            }
        }

        prevPoint = lowPoint.value
        for (below in lowPoint.y until heightMap.size) {
            val currentPoint = heightMap[below][lowPoint.x]
            if ((prevPoint<=currentPoint) and  (currentPoint != 9)) {
                basin.add(currentPoint)
                prevPoint = currentPoint
            } else {
                basin.removeFirst() // we added the currentPoint
                break
            }
        }
        prevPoint = lowPoint.value
        for (right in lowPoint.x until heightMap.first().size) {
            val currentPoint = heightMap[lowPoint.y][right]
            if ((prevPoint<=currentPoint) and  (currentPoint != 9)) {
                basin.add(currentPoint)
                prevPoint = currentPoint
            } else {
                basin.removeFirst() // we added the currentPoint
                break
            }
        }
        basin.add(lowPoint.value)
        return basin
    }
}
