package days

object Day02 : Day {
    override val number: Int = 2
    override val expectedPart1Test: Long = 15L
    override val expectedPart2Test: Long = 12L

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

        fun playToLoose(): Play = when (this.move) {
            Move.ROCK -> Play(Move.SCISSORS)
            Move.PAPER -> Play(Move.ROCK)
            Move.SCISSORS -> Play(Move.PAPER)
        }

        fun playToWin(): Play = when (this.move) {
            Move.ROCK -> Play(Move.PAPER)
            Move.PAPER -> Play(Move.SCISSORS)
            Move.SCISSORS -> Play(Move.ROCK)
        }
    }

    class Round {
        private var elf:Play
        private lateinit var me:Play

        private constructor(elf:Play) {
            this.elf = elf
        }
        constructor(elf: Play, me: Play): this(elf) {
            this.me = me
        }

        constructor(elf: Play, outcome:Outcome): this(elf) {
            this.me = outcomeToPlay(elf, outcome)
        }

        private fun outcomeToPlay(elf: Play, outcome:Outcome): Play =
            when (outcome) {
                Outcome.WIN -> elf.playToWin()
                Outcome.DRAW -> elf
                Outcome.LOOSE -> elf.playToLoose()
            }

        fun score(): Int =
            when {
                (elf > me) -> Outcome.LOOSE.score + me.move.score
                (elf < me) -> Outcome.WIN.score + me.move.score
                else -> Outcome.DRAW.score + me.move.score
            }
    }

    private fun String.toPlay() = when (this) {
        "X" -> Play(Move.ROCK)
        "Y" -> Play(Move.PAPER)
        "Z" -> Play(Move.SCISSORS)
        "A" -> Play(Move.ROCK)
        "B" -> Play(Move.PAPER)
        "C" -> Play(Move.SCISSORS)
        else -> error("Aaargh bad bad data! $this")
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

    enum class Outcome(val score:Int) { WIN(6), DRAW(3), LOOSE(0) }

    private fun String.toOutcome() =
        when (this) {
            "X" -> Outcome.LOOSE
            "Y" -> Outcome.DRAW
            "Z" -> Outcome.WIN
            else -> error("Bad bad outcome data! $this")
        }

    private fun String.toRoundWithOutcome() = Round(
        elf = substringBefore(" ").toPlay(),
        outcome = substringAfter(" ").toOutcome()
    )

    private fun parseInputWithOutcome(input: List<String>): List<Round> = input.map { it.toRoundWithOutcome() }

    override fun part2(input: List<String>): Long {
        val game = parseInputWithOutcome(input)
        return scoreGame(game).toLong()
    }
}
