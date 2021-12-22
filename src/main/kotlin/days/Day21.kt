// Day object template
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
            println("$name - $position score: $score ${if (isWinner) "I Win!" else ""} ")
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
        //test
//        val submarine = Player(4, "Sub")
//        val me = Player(8, "Me ")
        val submarine = Player(3, "Sub")
        val me = Player(7, "Me ")
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

    data class DiracPlayer(val starting: Int, val name: String = "") {
        private var position = starting
        var score = 0L
        val isWinner: Boolean
            get() = score >= 1000

        fun turn(value: Int): Boolean {
            repeat(value) {
                position++
                if (position == 11) position = 1
            }
            score += position
            //debug()
            return (isWinner)
        }

        private fun debug() {
            println("$name - $position score: $score is winner? $isWinner")
        }
    }

    fun part2(input: List<String>): Long {
        //test
        val submarine = Player(4, "Sub", winningNumber = 21)
        val me = Player(8, "Me ", winningNumber = 21)
        val die: Die = DeterministicDie(sides=3)
//        val submarine = DiracPlayer(3, "Sub")
//        val me = DiracPlayer(7, "Me ")
        while (true) {
            submarine.turn(die)
            if (submarine.isWinner) break;
            me.turn(die)
            if (me.isWinner) break;
        }
        return -1L
    }
}
