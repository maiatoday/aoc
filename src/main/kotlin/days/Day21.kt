object Day21 {

    data class Player(val starting: Int, val name: String, val winningNumber: Int = 1000) {
        private var position = starting
        var score = 0L
        val isWinner: Boolean
            get() = score >= winningNumber

        fun turn(die: Die): Boolean {
            val steps = die.roll() + die.roll() + die.roll()
            repeat(steps) {
                position++
                if (position == 11) position = 1
            }
            score += position
            debug()
            return (isWinner)
        }

        fun debug() {
            //println("$name - $position score: $score ${if (isWinner) "I Win!" else ""} ")
        }
    }

    interface Die {
        var rollCount: Long
        fun roll(): Int
    }

    data class DeterministicDie(val sides: Int = 100) : Die {
        override var rollCount = 0L
        private var value = 0
        override fun roll(): Int {
            value++
            if (value == sides + 1) value = 1
            rollCount++
            return value
        }
    }

    fun part1(input: List<String>): Long {
        val (subStart, meStart) = getStartingPosition(input)
        val submarine = Player(subStart, "Sub")
        val me = Player(meStart, "Me ")
        val die: Die = DeterministicDie()
        while (true) {
            submarine.turn(die)
            if (submarine.isWinner) break;
            me.turn(die)
            if (me.isWinner) break;
        }
        val answer = die.rollCount * (if (submarine.isWinner) me.score else submarine.score)
        return answer
    }

    private fun getStartingPosition(input: List<String>): Pair<Int, Int> {
        val p1 = input.first { it.startsWith("Player 1") }.split(":")[1].trim().toInt()
        val p2 = input.first { it.startsWith("Player 2") }.split(":")[1].trim().toInt()
        return Pair(p1, p2)
    }


    /*
      Try to solve it with recursion however since the universes multiply so quickly I need to find another way.
      From research into the concept of dynamic programming the solution could be to keep track
      of solutions that have occurred before. Since a win depends on only the input parameters in this case
      existing score and the existing position in the circle. Looking for the wins the dice have to vary through
      all possible combinations. I did not use the distribution of the dice values as the caching would quickly populate
      all the wins matching the inputs and the dice probabilities adds to cognitive load
      Credit to Roman Elizarov solution, without which I would not have understood how to solve this puzzle
    */
    fun part2(input: List<String>): Long {
        val (subStart, meStart) = getStartingPosition(input)

        // the Submarine always starts first, so it's starting position is in the first slot
        val finalScoreBoard = playTurn(GameSnapShot(subStart, meStart, 0, 0))

        return maxOf(finalScoreBoard.first, finalScoreBoard.second)
    }

    private val dejaVuTally = mutableMapOf<GameSnapShot, WinTally>()

    private fun playTurn(snapShot: GameSnapShot): WinTally {
        // this is the secret to escaping recursion inception: remember history and skip calculations if possible
        dejaVuTally[snapShot]?.let { return it }
        val winTally = WinTally(0, 0)
        val (firstPosition, nextPosition, firstScore, nextScore) = snapShot

        for (d1 in 1..3) for (d2 in 1..3) for (d3 in 1..3) {
            // position needs to go from 1-10, the -1 and +1 adujust for this
            val newPosition = (d1 + d2 + d3 + firstPosition - 1) % 10 + 1
            val newScore = firstScore + newPosition
            if (newScore >= 21) {
                // first wins
                winTally.first++
            } else {
                // swap around as the next player goes first in the next round
                val updatedScoreBoard =
                    playTurn(GameSnapShot(nextPosition, newPosition, nextScore, newScore))
                // since we swapped start position the win tally that comes back also needs swapping
                winTally.first += updatedScoreBoard.second
                winTally.second += updatedScoreBoard.first
            }
        }
        // record history in case we pass this way again
        dejaVuTally[snapShot] = winTally
        return winTally
    }

    data class WinTally(var first: Long, var second: Long)

    data class GameSnapShot(val firstPosition: Int, val nextPosition: Int, val firstScore: Int, val nextScore: Int)
}
