package days

object Day01 : Day<Long, List<String>> {
    override val number: Int = 1
    override val expectedPart1Test: Long = 3L
    override val expectedPart2Test: Long = 6L
    override var useTestData = true
    override val debug = false

    override fun part1(input: List<String>): Long {
        val start = 50
        val turns = input.toTurns()
        return countZeros(start, turns)
    }

    private fun countZeros(start: Int, turns: List<Turn>): Long {
        var count = 0L
        var current = start
        for (turn in turns) {
            val clicks = when (turn.direction) {
                Direction.L -> {
                    100 - turn.distance
                }

                Direction.R -> {
                    turn.distance
                }
            }
            current = (current + clicks) % 100
            if (current == 0) count++
        }
        return count
    }

    override fun part2(input: List<String>): Long {
        val start = 50
        val turns = input.toTurns()
        return countZerosV2(start, turns)
    }

    private fun countZerosV2(start: Int, turns: List<Turn>): Long {
        var count = 0L
        var current = start
        for (turn in turns) {

            repeat(turn.distance) {
                when (turn.direction) {
                    Direction.L -> {
                        current--
                        if (current < 0) current = 99
                    }

                    Direction.R -> {
                        current++
                        if (current == 100) current = 0
                    }
                }
                if (current == 0) count++
            }

        }
        return count
    }

    fun List<String>.toTurns(): List<Turn> =
        this.map { Turn(Direction.valueOf(it[0].toString()), it.substring(1).toInt()) }

    data class Turn(val direction: Direction, val distance: Int)
    enum class Direction { L, R }
}