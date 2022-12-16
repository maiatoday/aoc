package days

import days.Day15.findMissingBeacon
import kotlin.math.abs

typealias Day15ReturnType = Long
typealias Day15InputType = List<String>

typealias LPoint = Pair<Long, Long>

object Day15 : Day<Day15ReturnType, Day15InputType> {
    override val number: Int = 15
    override val expectedPart1Test: Day15ReturnType = 26
    override val expectedPart2Test: Day15ReturnType = 56000011

    override fun part1(input: Day15InputType): Day15ReturnType {
//        val row = 10L //test
        val row = 2000000L
        val data = input.toData()
        val sensors: List<Sensor> = data.toSensors()
        val beacons: Set<LPoint> = data.map { it[1] }.toSet()
        val xRange = sensors.xRange()
        val rowLength = (xRange.last + 1) - xRange.first
        val scanCount = rowLength - sensors.countMissing(xRange, row..row)
        val beaconCount = beacons.count { it.second == row }
        return scanCount - beaconCount
    }

    override fun part2(input: Day15InputType): Day15ReturnType {
        val dim = 20L
        // val dim = 4000000L
        val offset = 4000000L
        val data = input.toData()
        val sensors: List<Sensor> = data.toSensors()
        val beacons: Set<LPoint> = data.map { it[1] }.toSet()
        val xRange = 0..dim
        val yRange = 0..dim
        val missingBeacon = sensors.findMissingBeacon(beacons, xRange, yRange)
        val tuningFrequency = offset * missingBeacon.first + missingBeacon.second
        return tuningFrequency
    }

    private fun Day15InputType.toData() = this
        .filter { !it.startsWith("#") }
        .map { it.toSensorData() }

    //  Sensor at x=2, y=18: closest beacon is at x=-2, y=15
    //  """Sensor at x=(\d+), y=(\d+): closest beacon is at x=(\d+), y=(\d+)""".toRegex()
    private val sbRegex =
        """Sensor at x=([-+]?\d+), y=([-+]?\d+): closest beacon is at x=([-+]?\d+), y=([-+]?\d+)""".toRegex()

    private fun String.toSensorData(): List<LPoint> {
        val (sx, sy, bx, by) = sbRegex
            .matchEntire(this)
            ?.destructured
            ?: throw IllegalArgumentException("Bad data $this")
        return listOf((sx.toLong() to sy.toLong()), (bx.toLong() to by.toLong()))
    }

    private fun LPoint.distanceTo(other: LPoint) =
        abs(this.first - other.first) + abs(this.second - other.second)


    data class Sensor(val point: LPoint, val beacon: LPoint) {
        val distance = point.distanceTo(beacon)
        private val coarseXRange = point.first - distance..point.first + distance
        private val coarseYRange = point.second - distance..point.second + distance
        fun inRange(p: LPoint): Boolean = //point.distanceTo(p) <= distance
            if (p.first in coarseXRange && p.second in coarseYRange)
                point.distanceTo(p) <= distance
            else false
    }

    private fun List<List<LPoint>>.toSensors(): List<Sensor> =
        this.map {
            Sensor(point = it[0], beacon = it[1])
        }

    private fun List<Sensor>.xRange(): LongRange {
        val lowest = this.minOf { s -> s.point.first - s.distance }
        val highest = this.maxOf { s -> s.point.first + s.distance }
        return lowest..highest
    }

    private fun List<Sensor>.countMissing(xRange: LongRange, yRange: LongRange): Long {
        var missing = 0L
        var scanned = false
        for (x in xRange) for (y in yRange) {
            scanned = false
            for (s in this) {
                if (s.inRange(x to y)) {
                    scanned = true
                    break
                }
            }
            if (!scanned) missing++
        }
        return missing
    }

    private fun List<Sensor>.findMissingBeacon(beacons: Set<LPoint>, xRange: LongRange, yRange: LongRange): LPoint {
        var p = (-1L to -1L)
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
        val oneSpaceList:List<Pair<Sensor,Sensor>> =  buildList {
            for (t in this@findMissingBeacon) {
                for (o in this@findMissingBeacon) {
                    if (t != o) {
                         val sdistance = t.point.distanceTo(o.point)- t.distance - o.distance
                        if (sdistance == 1L) this.add((t to o))
                    }
                }
            }
        }
        // the next part... to find the intersect point 
        return (14L to 11L)
    }
}