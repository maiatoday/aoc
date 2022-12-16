package util


import kotlin.math.abs
import kotlin.math.sign

typealias PPoint = Pair<Int, Int>

fun PPoint.neighbours(
    maxM: Int,
    maxN: Int,
    diagonal: Boolean = false,
    includeSelf: Boolean = false
): List<PPoint> {
    val mRange = first - 1..first + 1
    val nRange = second - 1..second + 1
    val points = mutableListOf<PPoint>()
    for (m in mRange) for (n in nRange) {
        if (!includeSelf && this == m to n) continue // jump  over self
        if (m < 0 || n < 0) continue
        if (m >= maxM || n >= maxN) continue
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