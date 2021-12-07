import kotlin.math.absoluteValue

// Day object template
object Day07 {

    fun part1(input: List<String>): Int {
        val crabStartPositions = input.first().trim().split(',').map { it.toInt() }
        return minFuelCalculator(crabStartPositions) { i, j -> (i - j).absoluteValue }
    }

    fun part2(input: List<String>): Int {
        val crabStartPositions = input.first().trim().split(',').map { it.toInt() }
        return minFuelCalculator(crabStartPositions) { i, j -> (1..(i - j).absoluteValue).sum() }
    }

    private fun minFuelCalculator(startPositions: List<Int>, calculate: (position: Int, newPosition: Int) -> Int): Int {
        var minFuel = Integer.MAX_VALUE
        val startPosition = startPositions.minOrNull() ?: 0
        val endPosition = startPositions.maxOrNull() ?: 0
        for (i in startPosition..endPosition) {
            val fuel = startPositions.fold(0) { fuel, position ->
                fuel + calculate(position, i)
            }
            if (fuel < minFuel) minFuel = fuel
        }
        return minFuel
    }
}
