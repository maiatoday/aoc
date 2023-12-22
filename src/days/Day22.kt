package days

import util.readInts

object Day22 : Day<Long, List<String>> {
    override val number: Int = 22
    override val expectedPart1Test: Long = 5L
    override val expectedPart2Test: Long = -1L
    override var useTestData = true
    override val debug = false

    override fun part1(input: List<String>): Long {
        val jenga = Jenga(input)
        return -1L
    }

    class Jenga(val blocks: List<Block>)

    data class Block(val xRange: IntRange, val yRange: IntRange, val zRange: IntRange)

    fun Block(s: String): Block {
        val (start, stop) = s.split("~")
        val (x1, y1, z1) = start.readInts()
        val (x2, y2, z2) = stop.readInts()
        return Block(x1..x2, y1..y2, z1..z2)
    }


    fun Jenga(input: List<String>): Jenga = Jenga(input.map {
        Block(it)
    })

    override fun part2(input: List<String>): Long {
        return -1L
    }
}
