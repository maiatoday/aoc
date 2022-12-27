package util


import kotlin.math.abs
import kotlin.math.sign

typealias PPoint = Pair<Int, Int>

fun PPoint.neighbours(
    maxM: Int = Int.MAX_VALUE,
    maxN: Int = Int.MAX_VALUE,
    diagonal: Boolean = false,
    includeSelf: Boolean = false,
    onlyPositive: Boolean = true,
    stayBelowMax: Boolean = true
): List<PPoint> {
    val mRange = first - 1..first + 1
    val nRange = second - 1..second + 1
    val points = mutableListOf<PPoint>()
    for (m in mRange) for (n in nRange) {
        if (!includeSelf && this == m to n) continue // jump over self
        if (onlyPositive && (m < 0 || n < 0)) continue
        if (stayBelowMax && (m >= maxM || n >= maxN)) continue
        if (!diagonal && (m != first && n != second)) continue
        points.add(m to n)
    }
    return points
}

fun List<String>.findInGrid(p: String): PPoint {
    for (index in this.indices) {
        if (this[index].contains(p)) return (index to this[index].indexOf(p))
    }
    return (-1 to -1)
}

fun List<String>.findAllInGrid(p: String): List<PPoint> {
    val returnList = mutableListOf<PPoint>()
    val c = p.first()
    for (m in this.indices) for (n in this.first().indices) {
        if (this[m][n] == c) returnList.add(m to n)
    }
    return returnList
}


data class Point(val x: Int, val y: Int) {
    infix fun manhattanDistanceTo(other: Point) =
        abs(this.x - other.x) + abs(this.y - other.y)

    fun lineTo(other: Point): List<Point> {
        val dx = (other.x - x).sign
        val dy = (other.y - y).sign
        val steps = maxOf(abs(x - other.x), abs(y - other.y))
        return (1..steps).scan(this) { last, _ -> Point(last.x + dx, last.y + dy) }
    }

}

fun Point.neighbours(
    maxY: Int = Int.MAX_VALUE,
    maxX: Int = Int.MAX_VALUE,
    diagonal: Boolean = false,
    includeSelf: Boolean = false,
    onlyPositive: Boolean = true,
    stayBelowMax: Boolean = true
): List<Point> {
    val xRange = x + 1  downTo  x- 1
    val yRange = y + 1 downTo y - 1
    val points = mutableListOf<Point>()
    for (yy in yRange) for (xx in xRange) {
        if (!includeSelf && this == Point(xx, yy)) continue // jump over self
        if (onlyPositive && (yy < 0 || xx < 0)) continue
        if (stayBelowMax && (yy >= maxY || xx >= maxX)) continue
        if (!diagonal && (yy != y && xx != x)) continue
        points.add(Point(xx, yy))
    }
    return points
}

fun Point.roseNeighbours(): List<Point> = this.neighbours(diagonal = true, onlyPositive = false, stayBelowMax = false)

fun List<String>.listFromGrid(p: String): List<Point> {
    val returnList = buildList {
        val c = p.first()
        for (y in this@listFromGrid.indices) for (x in this@listFromGrid.first().indices) {
            if (this@listFromGrid[y][x] == c) add(Point(x, y))
        }
    }
    return returnList
}

fun List<Point>.boundaries(): Pair<IntRange, IntRange> {
    val xMin = minOf { it.x }
    val xMax = maxOf { it.x }
    val yMin = minOf { it.y }
    val yMax = maxOf { it.y }
    return (xMin..xMax to yMin..yMax)
}

fun List<Point>.debug(filled:String = "#", empty:String = ".") {
    val boundaries = boundaries()
    for (y in boundaries.second) {
        for (x in boundaries.first) {
            if (Point(x,y) in this) print(filled) else print(empty)
        }
        println()
    }

}