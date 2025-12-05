package days

import util.readLongs
import util.splitByBlankLine

object Day05 :Day<Long, List<String>> {
    override val number: Int = 5
    override val expectedPart1Test: Long = 3L
    override val expectedPart2Test: Long = 14L
    override var useTestData = true
    override val debug = false

    override fun part1(input: List<String>): Long {
        val (validIngredients, ingredients) = parseData(input)
        val freshIngredients = ingredients.filter {ingredient ->  validIngredients.any {ingredient in it}}
        return freshIngredients.count().toLong()
    }

    override fun part2(input: List<String>): Long {
        val (validIngredients, ingredients) = parseData(input)
        val freshIdCount = validIngredients.map { it.last - it.first}
        return -1L
    }

    private fun parseData(input: List<String>):Pair<List<LongRange>, List<Long>> {
        val ll = input.splitByBlankLine()
        val validIngredients = ll[0].map {
            val range = it.readLongs()
            LongRange(range[0], range[1])
        }
        val ingredients = ll[1].map { it.readLongs().first()}
        return validIngredients to ingredients
    }
}