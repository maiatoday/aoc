package days

import util.readLongs

object Day02 : Day<Long, List<String>> {
    override val number: Int = 2
    override val expectedPart1Test: Long = 1227775554L
    override val expectedPart2Test: Long = 4174379265L
    override var useTestData = true
    override val debug = false

    override fun part1(input: List<String>): Long {
        val ranges = input.getRanges()
        val badValues = ranges.flatMap {
            it.getBadIds()
        }
        return badValues.sum()
    }

    override fun part2(input: List<String>): Long {
        val ranges = input.getRanges()
        val badValues = ranges.flatMap {
            it.getMoreBadIds()
        }
        return badValues.sum()
    }

    fun List<String>.getRanges(): List<LongRange> =
        this.first().split(',').map {
            it.readLongs()
        }.map {
            LongRange(it[0], it[1])
        }

    fun LongRange.getBadIds(): List<Long> = this.filter { it.isBad() }

    fun Long.isBad(): Boolean {
        val s = this.toString()
        val s1 = s.take(s.length / 2)
        val s2 = s.substring(s.length / 2)
        return s1 == s2
    }

    fun LongRange.getMoreBadIds(): List<Long> = this.filter { it.isMoreBad() }

    fun Long.isMoreBad(): Boolean {
        val s = this.toString()
        val chunkLists = IntRange(1, s.length / 2).map { s.chunked(it) }
        return chunkLists.any { l -> l.all { it == l[0] } }
    }
}