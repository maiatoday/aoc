package util

typealias Point = Pair<Int, Int>

fun Point.neighbours(
    maxM: Int,
    maxN: Int,
    diagonal: Boolean = false,
    includeSelf: Boolean = false
): List<Point> {
    val mRange = first - 1..first + 1
    val nRange = second - 1..second + 1
    val points = mutableListOf<Point>()
    for (m in mRange) for (n in nRange) {
        if (!includeSelf && this == m to n) continue // jump  over self
        if (m < 0 || n < 0) continue
        if (m >= maxM || n >= maxN) continue
        if (!diagonal && (m != first && n != second)) continue
        points.add(m to n)
    }
    return points
}

fun List<String>.findInGrid(p: String): Point {
    for (index in this.indices) {
        if (this[index].contains(p)) return (index to this[index].indexOf(p))
    }
    return (-1 to -1)
}

fun List<String>.findAllInGrid(p: String): List<Point> {
    val returnList = mutableListOf<Point>()
    val c = p.first()
    for (m in this.indices) for (n in this.first().indices) {
        if (this[m][n] == c) returnList.add(m to n)
    }
    return returnList
}