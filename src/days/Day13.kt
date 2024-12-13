package days

import util.Point
import util.readInts
import util.splitByBlankLine

object Day13 : Day<Long, List<String>> {
    override val number: Int = 13
    override val expectedPart1Test: Long = 480L
    override val expectedPart2Test: Long = -1L
    override var useTestData = true
    override val debug = false

    override fun part1(input: List<String>): Long {
        val machines = readMachineData(input)
        val minimums = machines.map { it.getMinimumPresses() }.also(::println)
        val cost = minimums.filterNotNull().sumOf { (m, n) -> 3 * m + n }
        return cost
    }

    private fun readMachineData(input: List<String>): List<ClawMachine> =
        input.splitByBlankLine().map { m -> ClawMachine(m.map { it.readInts() }) }

    val fuBigNumber: Long = 10000000000000L

    data class ClawMachine(val a: Point, val b: Point, val prize: Point) {
        fun getMinimumPresses(isPart2: Boolean = false): Pair<Long, Long>? {
            val X = if (isPart2) prize.x.toLong() + fuBigNumber else prize.x.toLong()
            val Y = if (isPart2) prize.y.toLong() + fuBigNumber else prize.y.toLong()
            val A = a.x.toLong()
            val B = b.x.toLong()
            val C = a.y.toLong()
            val D = b.y.toLong()

            val mNominator: Long = X * D - B * Y
            val mDenominator: Long = A * D - C * B
            if (mNominator % mDenominator != 0L) return null
            val m = mNominator / mDenominator
            val n = X / B - m * A / B
            return (m to n)
        }
    }

    fun ClawMachine(specs: List<List<Int>>): ClawMachine {
        require(specs.size == 3)
        val buttonA = Point(specs[0][0], specs[0][1])
        val buttonB = Point(specs[1][0], specs[1][1])
        val prize = Point(specs[2][0], specs[2][1])
        return ClawMachine(a = buttonA, b = buttonB, prize = prize)
    }

    override fun part2(input: List<String>): Long {
        val machines = readMachineData(input)
        val minimums = machines.map { it.getMinimumPresses(true) }.also(::println)
        val cost = minimums.filterNotNull().sumOf { (m, n) -> 3 * m + n }
        return cost
    }
}

