package days

import util.readLongs

object Day22 : Day<Long, List<String>> {
    override val number: Int = 22
    override val expectedPart1Test: Long = 37327623L
    override val expectedPart2Test: Long = 23L
    override var useTestData = true
    override val debug = false

    override fun part1(input: List<String>): Long {
        val seeds = input.flatMap { it.readLongs() }
        val answer = seeds.map { numbersFor(it, 2000) }
        return answer.sum()
    }

    fun numbersFor(seed: Long, repeats: Int): Long {
        var current = seed
        repeat(repeats) {
            current = current.secretNumber()
        }
        return current
    }

    fun Long.secretNumber(): Long {
        val firstStep = (this * 64).mix(this).prune()
        val secondStep = (firstStep / 32).mix(firstStep).prune()
        val thirdStep = (secondStep * 2048).mix(secondStep).prune()
        return thirdStep
    }

    fun Long.mix(original: Long): Long = this xor original
    fun Long.prune(): Long = this % 16777216

    override fun part2(input: List<String>): Long {
        return -1L
    }
}
