package days

import util.Point
import kotlin.math.abs

typealias Day15ReturnType = Long
typealias Day15InputType = List<String>


object Day15 : Day<Day15ReturnType, Day15InputType> {
    override val number: Int = 15
    override val expectedPart1Test: Day15ReturnType = 26
    override val expectedPart2Test: Day15ReturnType = 56_000_011L

    override fun part1(input: Day15InputType): Day15ReturnType {
        val row = 10 //test
//        val row = 2_000_000
        val data = input.toData()
        val sensors: List<Sensor> = data.toSensors()
        val beacons: Set<Point> = data.map { it[1] }.toSet()
        val xRange = sensors.xRange()
        val rowLength = (xRange.last + 1) - xRange.first
        val scanCount = rowLength - sensors.countMissing(xRange, row..row)
        val beaconCount = beacons.count { it.y == row }
        return (scanCount - beaconCount).toLong()
    }

    override fun part2(input: Day15InputType): Day15ReturnType {
        val dim = 20
        // val dim = 4_000_000L
        val offset = 4_000_000L
        val data = input.toData()
        val sensors: List<Sensor> = data.toSensors()
        val beacons: Set<Point> = data.map { it[1] }.toSet()
        val limitRange = 0..dim
        val missingBeacon = sensors.findMissingBeacon(beacons, limitRange)
        val tuningFrequency = offset * missingBeacon.x + missingBeacon.y
        return tuningFrequency
    }

    private fun Day15InputType.toData() = this
        .filter { !it.startsWith("#") }
        .map { it.toSensorData() }

    //  Sensor at x=2, y=18: closest beacon is at x=-2, y=15
    //  """Sensor at x=(\d+), y=(\d+): closest beacon is at x=(\d+), y=(\d+)""".toRegex()
    private val sbRegex =
        """Sensor at x=([-+]?\d+), y=([-+]?\d+): closest beacon is at x=([-+]?\d+), y=([-+]?\d+)""".toRegex()

    private fun String.toSensorData(): List<Point> {
        val (sx, sy, bx, by) = sbRegex
            .matchEntire(this)
            ?.destructured
            ?: throw IllegalArgumentException("Bad data $this")
        return listOf(Point(sx.toInt() , sy.toInt()), Point(bx.toInt() , by.toInt()))
    }

    data class Sensor(val point: Point, val beacon: Point) {
        val distance = point.manhattanDistanceTo(beacon)

        fun inRange(p: Point): Boolean = point.manhattanDistanceTo(p) <= distance

        fun rangeAt(y: Int):IntRange? {
            val width = distance - abs(point.y -y)
            return  if (width == 0) null else (point.x-width)..(point.x+width)
        }
    }

    private fun List<List<Point>>.toSensors(): List<Sensor> =
        this.map {
            Sensor(point = it[0], beacon = it[1])
        }

    private fun List<Sensor>.xRange(): IntRange {
        val lowest = this.minOf { s -> s.point.x - s.distance }
        val highest = this.maxOf { s -> s.point.x + s.distance }
        return lowest..highest
    }

    private fun List<Sensor>.countMissing(xRange: IntRange, yRange: IntRange): Int {
        var missing = 0
        var scanned = false
        for (x in xRange) for (y in yRange) {
            scanned = false
            for (s in this) {
                if (s.inRange(Point(x,y))) {
                    scanned = true
                    break
                }
            }
            if (!scanned) missing++
        }
        return missing
    }

    private fun List<Sensor>.findMissingBeacon(
        beacons: Set<Point>,
        limitRange: IntRange
    ): Point {
        var p = (-1 to -1)
        // the plan is:
        // there can only one point so  it means there are no other blank spaces without a beacon
        // so if one sensor is next to another such that they  are exactly  one position away from each other
        // by manhattan distance then the missing  point is between then
        // however that spot may contain a beacon

        // loop through sensors nested matching them to all other sensors
        //     find those pairs are 1 from each other
        // loop through the pairs
        //      check  the shared single  edge checking that it doesn't contain a beacon
        //      break out when I find a  blank, there is only  one
        // I'll code it up later
        val oneSpaceList: List<Pair<Sensor, Sensor>> = buildList {
            for (t in this@findMissingBeacon) {
                for (o in this@findMissingBeacon) {
                    if (t != o) {
                        val sdistance = t.point.manhattanDistanceTo(o.point) - t.distance - o.distance
                        if (sdistance == 1) this.add((t to o))
                    }
                }
            }
        }
        // the next part... to find the intersection point

        return Point(14,11)
    }
}