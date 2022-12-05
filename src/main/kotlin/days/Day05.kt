package days

object Day05 : Day {
    override val number: Int = 5
    override val expectedPart1Test: Long = -1L
    override val expectedPart2Test: Long = -1L
    const val expectedPart1STest: String = "CMZ"
    const val expectedPart2STest: String = "MCD"
    private fun String.parseInput(): Pair<Stacks, CraneMoves> {
        val (stacksString, movesString) = this.split("\n\n")
        val columnCount = stacksString.lines().single { it.startsWith(" 1") }.columnCount() ?: 0
        val stacks = stacksString.lines().filter { !it.startsWith(" 1") }.toStacks(columnCount)
        val moves: CraneMoves = movesString.toCraneMoves()
        return Pair(stacks, moves)
    }

    data class Move(val count: Int, val start: Int, val end: Int)

    private val craneMoveRegex = """move (\d+) from (\d+) to (\d+)""".toRegex()

    private fun String.toCraneMoves(): CraneMoves =
        this.lines().map { m ->
            val (count, start, end) = craneMoveRegex
                .matchEntire(m)
                ?.destructured
                ?: throw IllegalArgumentException("Bad data $m")
            Move(count.toInt(), start.toInt() - 1, end.toInt() - 1)
        }

    private fun String.columnCount() = this.trim().split("   ")
        .maxOfOrNull { it.toInt() }

    private fun List<String>.toStacks(columnCount: Int): Stacks {
        val lines = this.reversed()
        val stacks = buildList {
            for (n in 1..columnCount) {
                this.add(Stack())
            }
            for (l in lines) {
                l.forEachIndexed { index, c ->
                    if (c.isLetter()) {
                        (this[index.toStackCount()] as Stack).add(c.toString())
                    }
                }

            }
        }
        return stacks
    }

    private fun Int.toStackCount() = (this - 1) / 4

    private fun Stacks.applyMoves9000(moves: CraneMoves) {
        for (m in moves) {
            for (n in 1..m.count) {
                val crate = this[m.start].last()
                this[m.start].removeLast()
                this[m.end].add(crate)
            }
        }
    }

    private fun Stacks.applyMoves9001(moves: CraneMoves) {
        for (m in moves) {
            val startStackSize = this[m.start].size
            val sliceRange = startStackSize - m.count until startStackSize
            val crates = this[m.start].slice(sliceRange)
            repeat(crates.size) {
                this[m.start].removeLast()
            }
            this[m.end].addAll(crates)
        }
    }

    override fun part1S(input: String): String {
        val (stacks, craneMoves) = input.parseInput()
        stacks.applyMoves9000(craneMoves)
        return stacks.joinToString("") { s -> s.last() }
    }

    override fun part2S(input: String): String {
        val (stacks, craneMoves) = input.parseInput()
        stacks.applyMoves9001(craneMoves)
        return stacks.joinToString("") { s -> s.last() }
    }

}

typealias Stack = ArrayDeque<String>
typealias Stacks = List<Stack>
typealias CraneMoves = List<Day05.Move>
