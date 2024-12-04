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

fun PPoint.toPoint() = Point(second, first)
fun Point.toPPoint() = Pair(y, x)

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
    val xRange = x + 1 downTo x - 1
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

fun List<Point>.debug(filled: String = "#", empty: String = ".") {
    val (xRange, yRange) = boundaries()
    for (y in yRange) {
        for (x in xRange) {
            if (Point(x, y) in this) print(filled) else print(empty)
        }
        println()
    }

}

data class Point3(val x: Int, val y: Int, val z: Int)

fun Point3.neighbours(
    observeBounds: Boolean = false,
    xBoundary: IntRange = 0..Int.MAX_VALUE,
    yBoundary: IntRange = 0..Int.MAX_VALUE,
    zBoundary: IntRange = 0..Int.MAX_VALUE,
    includeDiagonal: Boolean = false,
    includeSelf: Boolean = false,
    extraFilter: (Point3) -> Boolean = { true }
): Set<Point3> =
    buildSet {
        for (zz in z + 1 downTo z - 1) for (yy in y + 1 downTo y - 1) for (xx in x + 1 downTo x - 1) {
            if (!includeSelf && Point3(x, y, z) == Point3(xx, yy, zz)) continue // jump over self
            if (observeBounds && (xx !in xBoundary || yy !in yBoundary || zz !in zBoundary)) continue
            if (!includeDiagonal && ((yy != y && xx != x) || (zz != z && xx != x) || (zz != z && yy != y))) continue
            if (!extraFilter(Point3(xx, yy, zz))) continue
            add(Point3(xx, yy, zz))
        }
    }

fun String.toPoint3(): Point3 {
    val (x, y, z) = this.split(",").map { it.toInt() }
    return Point3(x, y, z)
}

fun Point3.toList(): List<Int> = listOf(x, y, z)

fun List<Point3>.boundaries(offset: Int = 0): Triple<IntRange, IntRange, IntRange> = Triple(
    (minOf { it.x } - offset)..(maxOf { it.x } + offset),
    (minOf { it.y } - offset)..(maxOf { it.y } + offset),
    (minOf { it.z } - offset)..(maxOf { it.z } + offset)
)

fun Point3.shareSides(other: Point3): Boolean {
    val pairs = this.toList().zip(other.toList())
    val diffCount = pairs.count { abs(it.first - it.second) == 1 }
    val sameCount = pairs.count { it.first == it.second }
    return (diffCount == 1) && (sameCount == 2)
}

fun List<String>.extractString(coordinates: List<Point>): String = buildString {
    for ((x, y) in coordinates) {
        if ((x in this@extractString[0].indices) && (y in this@extractString.indices)) {
            append(this@extractString[y][x])
        }
    }
}

fun rowPoints(x: Int, y: Int, length: Int): List<Point> = buildList {
    for (i in 0..<length) {
        add(Point(x + i, y))
    }
}

fun columnPoints(x: Int, y: Int, length: Int): List<Point> = buildList {
    for (i in 0..<length) {
        add(Point(x, y + i))
    }
}

fun diagonalPoints(x: Int, y: Int, length: Int, forward: Boolean): List<Point> = buildList {
    // forward means looks like  a forward slash back means looks like a backslash
    // always starts at x and y
    if (forward) { // /
        for (i in 0..<length) {
            add(Point(x - i, y + i))
        }
    } else { // \
        for (i in 0..<length) {
            add(Point(x + i, y + i))
        }
    }

}