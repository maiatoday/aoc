package days

object Day07 : Day<Int, List<String>> {
    override val number: Int = 7
    override val expectedPart1Test: Int = 6440
    override val expectedPart2Test: Int = 5905
    override var useTestData = true
    override val debug = false

    override fun part1(input: List<String>): Int =
            input.map { h -> h.toHand(false) }.calculateWinnings()

    override fun part2(input: List<String>): Int =
            input.map { h -> h.toHand(true) }.calculateWinnings()

    private fun List<Hand>.calculateWinnings(): Int =
            this.sortedDescending()
                    .mapIndexed { i, h ->
                        (i + 1) * h.bid
                    }
                    .sum()

    data class Hand(val bid: Int, val cards: String, val withJoker: Boolean, val rank: Rank = cards.toRank(withJoker)) : Comparable<Hand> {
        override fun compareTo(other: Hand): Int {
            if (rank < other.rank) return -1
            if (rank > other.rank) return 1
            (this.cards.toList().map { it.cardValue(withJoker) } zip other.cards.toList().map { it.cardValue(withJoker) })
                    .forEach {
                        if (it.first > it.second) return -1
                        if (it.second > it.first) return 1
                    }
            return 0
        }
    }

    private fun String.toHand(withJoker: Boolean): Hand {
        val (cards, bid) = this.split(" ")
        return Hand(bid = bid.toInt(), cards = cards, withJoker = withJoker)
    }

    enum class Rank(val value: Int) {
        FiveOfAKind(7),
        FourOfAKind(6),
        FullHouse(5),
        ThreeOfAKind(4),
        TwoPair(3),
        OnePair(2),
        HighCard(1),
    }

    private fun String.toRank(withJoker: Boolean): Rank {
        val originalMap = this.toList().distinct().associateWith { c -> this.count { it == c } }
        val cardCountMap = if (withJoker && originalMap.containsKey('J')) {
            if (originalMap['J'] == 5) {
                originalMap
            } else {
                val swapCard = originalMap.filter { (c, _) -> c != 'J' }.maxBy { (k, v) -> v }.key
                val newHand = this.replace('J', swapCard)
                val newMap = newHand.toList().distinct().associateWith { c -> newHand.count { it == c } }
                newMap
            }
        } else {
            originalMap
        }
        return when (cardCountMap.size) {
            1 -> Rank.FiveOfAKind
            2 -> if (cardCountMap.values.contains(3)) Rank.FullHouse else Rank.FourOfAKind
            3 -> if (cardCountMap.values.contains(3)) Rank.ThreeOfAKind else Rank.TwoPair
            4 -> Rank.OnePair
            5 -> Rank.HighCard
            else -> error("Oops no rank")
        }
    }

    fun Char.cardValue(withJoker: Boolean) =
            when (this) {
                'A' -> if (withJoker) 13 else 14
                'K' -> if (withJoker) 12 else 13
                'Q' -> if (withJoker) 11 else 12
                'J' -> if (withJoker) 1 else 11
                'T' -> 10
                else -> this.digitToInt()
            }

}
