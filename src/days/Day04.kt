package days

import util.Point
import util.listFromGrid
import util.neighbours

object Day04 : Day<Long, List<String>> {
    override val number: Int = 4
    override val expectedPart1Test: Long = 13L
    override val expectedPart2Test: Long = 43L
    override var useTestData = true
    override val debug = false

    override fun part1(input: List<String>): Long {
        val paperMap = readMap(input)
        val freeRoll = getFreeRolls(paperMap)
        return freeRoll.count().toLong()
    }

    private fun getFreeRolls(paperMap: List<Point>) = paperMap.map {
        it to it.neighbours(diagonal = true, includeSelf = false).filter { neighbour -> neighbour in paperMap }
    }.filter { it.second.size < 4 }

    private fun readMap(input: List<String>) = input.listFromGrid("@")

    override fun part2(input: List<String>): Long {
        val paperMap = readMap(input)
        var rollsFreed = 0
        var canRemove = true
        var newMap = paperMap
        while (canRemove) {
            val rollsToFree = getFreeRolls(newMap)
            newMap = newMap - rollsToFree.map { it.first }.toSet()
            rollsFreed += rollsToFree.size
            if (rollsToFree.isEmpty()) canRemove = false
        }
        return rollsFreed.toLong()
    }


}