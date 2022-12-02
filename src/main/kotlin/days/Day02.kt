import days.Day

object Day02 : Day {

    enum class Move(val score: Int) { ROCK(1), PAPER(2), SCISSORS(3) }

    data class Play(val move: Move) : Comparable<Play> {
        override fun compareTo(other: Play): Int =
            when {
                (this.move == other.move) -> 0
                (this.move == Move.ROCK && other.move == Move.SCISSORS) -> 1
                (this.move == Move.PAPER && other.move == Move.ROCK) -> 1
                (this.move == Move.SCISSORS && other.move == Move.PAPER) -> 1
                else -> -1
            }

        fun toLoose(): Play = when (this.move) {
            Move.ROCK -> Play(Move.SCISSORS)
            Move.PAPER -> Play(Move.ROCK)
            Move.SCISSORS -> Play(Move.PAPER)
        }

        fun toWin(): Play = when (this.move) {
            Move.ROCK -> Play(Move.PAPER)
            Move.PAPER -> Play(Move.SCISSORS)
            Move.SCISSORS -> Play(Move.ROCK)
        }
    }

    data class Round(val elf: Play, val me: Play) {
        fun score(): Int =
            when {
                (elf == me) -> 3 + me.move.score
                (elf < me) -> 6 + me.move.score
                else -> 0 + me.move.score
            }
    }

    private fun String.toPlay() = when (this) {
        "X" -> Play(Move.ROCK)
        "Y" -> Play(Move.PAPER)
        "Z" -> Play(Move.SCISSORS)
        "A" -> Play(Move.ROCK)
        "B" -> Play(Move.PAPER)
        "C" -> Play(Move.SCISSORS)
        else -> Play(Move.ROCK)  /// this could cause an error if the data is bad
    }

    private fun String.toRound() = Round(
        elf = substringBefore(" ").toPlay(),
        me = substringAfter(" ").toPlay()
    )

    private fun parseInput(input: List<String>): List<Round> = input.map { it.toRound() }

    private fun scoreGame(game: List<Round>) = game.sumOf { round -> round.score() }

    override fun part1(input: List<String>): Long {
        val game = parseInput(input)
        return scoreGame(game).toLong()
    }

    enum class Outcome { WIN, DRAW, LOOSE }

    private fun String.toOutcome() =
        when (this) {
            "X" -> Outcome.LOOSE
            "Y" -> Outcome.DRAW
            "Z" -> Outcome.WIN
            else -> Outcome.DRAW
        }

    private fun String.toRound2() = Round2(
        elf = substringBefore(" ").toPlay(),
        outcome = substringAfter(" ").toOutcome()
    )

    data class Round2(val elf: Play, val outcome: Outcome) {
        private val me: Play = outcomeToPlay(elf)

        private fun outcomeToPlay(elf: Play): Play =
            when (outcome) {
                Outcome.WIN -> elf.toWin()
                Outcome.DRAW -> elf
                Outcome.LOOSE -> elf.toLoose()
            }

        fun score(): Int =
            when {
                (elf == me) -> 3 + me.move.score
                (elf < me) -> 6 + me.move.score
                else -> 0 + me.move.score
            }
    }

    private fun parseInput2(input: List<String>): List<Round2> = input.map { it.toRound2() }

    private fun scoreGame2(game: List<Round2>) = game.sumOf { round -> round.score() }

    override fun part2(input: List<String>): Long {
        val game = parseInput2(input)
        return scoreGame2(game).toLong()
    }
}
