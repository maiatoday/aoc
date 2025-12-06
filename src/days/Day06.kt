package days

import util.readLongs
import util.splitByBlankLine

object Day06 : Day<Long, List<String>> {
    override val number: Int = 6
    override val expectedPart1Test: Long = 4277556L
    override val expectedPart2Test: Long = 3263827L
    override var useTestData = true
    override val debug = false

    override fun part1(input: List<String>): Long {
        val homework = parseHomework(input)
        return homework.getAnswers().sum()
    }

    private fun parseHomework(input: List<String>): Homework {
        val operations = input.last().filter { it != ' ' }.toCharArray().toList()
        val allRows = input.map { it.readLongs() }.dropLast(1)
        val numbers = buildList {
            for (i in allRows[0].indices) {
                val oneCol = buildList {
                    allRows.map { add(it[i]) }
                }
                add(oneCol)
            }
        }
        return Homework(operations, numbers)
    }

    override fun part2(input: List<String>): Long {
        val homework = parseHomeworkCorrectly(input)
        return homework.getAnswers().sum()
    }

    private fun parseHomeworkCorrectly(input: List<String>): Homework {
        val operations = input.last().filter { it != ' ' }.toCharArray().toList()
        val allRows = input.dropLast(1)
        val maxLength = allRows.maxOfOrNull { it.length } ?: -1
        val paddedRows = allRows.map { it.padEnd(maxLength) }
        // flip rows
        val flippedList = buildList {
            for (ii in paddedRows[0].indices) {
                val newRow = buildList {
                    paddedRows.map { add(it[ii]) }
                }
                add(newRow.joinToString(""))
            }
        }
        val numbersLists = flippedList.splitByBlankLine()
        val numbers = numbersLists.map { ss -> ss.flatMap { it.readLongs() } }
        return Homework(operations, numbers)
    }

    data class Homework(val operations: List<Char>, val numbers: List<List<Long>>) {
        fun getAnswers(): List<Long> =
            buildList {
                for (ii in operations.indices) {
                    if (operations[ii] == '*') {
                        val tt: Long = numbers[ii].reduce { total, num -> total * num }
                        add(tt)
                    } else if (operations[ii] == '+') {
                        add(numbers[ii].sum())
                    }
                }
            }
    }
}