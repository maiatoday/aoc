package days

import util.Area
import util.Point
import util.listFromGridNotEmpty
import util.minus
import util.plus
import util.contains

object Day08 : Day<Long, List<String>> {
    override val number: Int = 8
    override val expectedPart1Test: Long = 14L
    override val expectedPart2Test: Long = 34L
    override var useTestData = true
    override val debug = false

    override fun part1(input: List<String>): Long {
        val antennas = parse(input)
        val boundaries = Area(input)
        val antinodes = antennas.values.flatMap { getAntinodes(it, boundaries, ::antinode) }
        return antinodes.toSet().size.toLong() // yes they need to be unique across all lists 😬
    }

    private fun getAntinodes(
        points: List<Point>,
        area: Area,
        doIt: (Point, Point, Area) -> List<Point>
    ): Collection<Point> =
        // here we don't care how its calculated just get all the combos
        points.flatMap { a ->
            (points - a).flatMap { b -> doIt(a, b, area) }
        }.toSet()

    override fun part2(input: List<String>): Long {
        val antennas = parse(input)
        val boundaries = Area(input)
        val antinodes = antennas.values.flatMap { getAntinodes(it, boundaries, ::antinodeWithHarmonics) }
        return antinodes.toSet().size.toLong() // yes they need to be unique across all lists 😬
    }

    private fun parse(input: List<String>): Map<Char, List<Point>> = input.listFromGridNotEmpty()
        .groupBy(keySelector = { it.first }, valueTransform = { it.second })
}

private fun antinode(a: Point, b: Point, area: Area): List<Point> = buildList {
    // this could be a fold of sorts... later
//        ..........
//        ...#...... #(3,1)
//        ..........
//        ....a..... (4,3) -> (4,3) + (-1,-2) -> (3,1)✅
//        ..........
//        .....a.... (5,5)-> (5,5) + (1,2) -> (6,7)✅
//        ..........
//        ......#... #(6,7)
//        ..........
//        ..........
    if (a == b) return emptyList()
    val angleA = (a - b)
    val angleB = (b - a)
    var f1 = a + angleA.harmonic(1)
    var f2 = b + angleB.harmonic(1)
    if (f1 in area) add(f1)
    if (f2 in area) add(f2)
}

fun Point.harmonic(h: Int) = Point(h * x, h * y)

private fun antinodeWithHarmonics(a: Point, b: Point, area: Area): List<Point> = buildList {
    if (a == b) return emptyList()
    // each antenna makes a resonance that is always at the other point,
    // so I need to add both antenna points, duplicates are removed later
    add(a)
    add(b)
    val angle1 = (a - b)
    val angle2 = (b - a)
    var f1 = 1
    var h1 = a + angle1.harmonic(f1)
    while (h1 in area) {
        add(h1)
        f1++
        h1 = a + angle1.harmonic(f1)
    }
    var f2 = 1
    var h2 = b + angle2.harmonic(f2)
    while (h2 in area) {
        add(h2)
        f2++
        h2 = b + angle2.harmonic(f2)
    }
}
