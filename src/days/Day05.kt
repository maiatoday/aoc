package days

import util.readLongs
import util.splitByBlankLine

object Day05 : Day<Long, List<String>> {
    override val number: Int = 5
    override val expectedPart1Test: Long = 3L
    override val expectedPart2Test: Long = 14L
    override var useTestData = true
    override val debug = false

    override fun part1(input: List<String>): Long {
        val (freshIngredientsRanges, ingredients) = parseData(input)
        val freshIngredients = ingredients.filter { ingredient -> freshIngredientsRanges.any { ingredient in it } }
        return freshIngredients.count().toLong()
    }

    override fun part2(input: List<String>): Long {
        val (freshIngredientsRanges, _) = parseData(input)
        val freshIdCount = compactRanges(freshIngredientsRanges)
            .sumOf { it.last + 1 - it.first }
        return freshIdCount
    }

    private fun parseData(input: List<String>): Pair<List<LongRange>, List<Long>> {
        val (ingredientsRangesData, ingredientsData) = input.splitByBlankLine()
        val validIngredients = ingredientsRangesData.map {
            val range = it.readLongs()
            LongRange(range[0], range[1])
        }
        val ingredients = ingredientsData.map { it.readLongs().first() }
        return validIngredients to ingredients
    }

    private fun compactRanges(validIngredients: List<LongRange>): List<LongRange> =
        validIngredients.sortedBy { it.first }
            .fold(mutableListOf()) { newList, nextRange ->
                if (newList.isEmpty()) {
                    newList.add(nextRange)
                } else {
                    val squashed = squash(newList.last(), nextRange)
                    newList.removeLast()
                    newList.addAll(squashed)
                }
                newList
            }

    fun squash(firstRange: LongRange, secondRange: LongRange): List<LongRange> {
        return if (secondRange.first <= firstRange.last) {
            // have an overlap
            if (secondRange.last <= firstRange.last) {
                // first completely overlaps second
                listOf(firstRange)
            } else {
                // partial overlap so combine the two ranges
                listOf(LongRange(firstRange.first, secondRange.last))
            }
        } else {
            // no overlap, both ranges are valid
            listOf(firstRange, secondRange)
        }
    }
}
