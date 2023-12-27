package days

object Day12 : Day<Long, List<String>> {
    override val number: Int = 12
    override val expectedPart1Test: Long = 21L
    override val expectedPart2Test: Long = 525152L
    override var useTestData = true
    override val debug = false

    override fun part1(input: List<String>): Long {
        val springRecords = input.map { Record(it) }
        val results = springRecords.map {
            guessArrangements(it.row, it.workingOrder, false)
        }
        return results.sum()
    }

    data class Record(val row: String, val workingOrder: List<Int>)

    private val cache = mutableMapOf<Triple<String, List<Int>, Boolean>, Long>()

    private fun guessArrangements(row: String, counts: List<Int>, groupStarted: Boolean): Long {
        val key = Triple(row, counts, groupStarted)
        if (key in cache) return cache.getValue(key)

        val newResult = when {
            row.isEmpty() -> if (counts.isEmpty()) 1L else 0L // perfect ending or we have spring counts left
            counts.isEmpty() -> if ("#" in row) 0L else 1L // we have hashes left or perfect ending of only .? so ? can be .
            else -> {
                var result = 0L
                when (row[0]) {
                    '.' -> {
                        result += guessArrangements(row.drop(1), counts, false)
                    }

                    '#' -> {
                        if (checkHash(row, counts, groupStarted)) {
                            result += guessArrangements(row.drop(counts[0]), counts.drop(1), true)
                        }
                    }

                    '?' -> {
                        // do both the . version and the # version
                        result += guessArrangements(row.drop(1), counts, false)
                        if (checkHash(row, counts, groupStarted)) {
                            result += guessArrangements(row.drop(counts[0]), counts.drop(1), true)
                        }
                    }
                }
                result
            }
        }
        cache[key] = newResult
        return newResult
    }

    private fun checkHash(row: String, counts: List<Int>, groupStarted: Boolean): Boolean =
            (counts[0] <= (row.length) && "." !in row.substring(0, counts[0]) &&
                    (row.length == counts[0] || row[counts[0]] != '#')) && !groupStarted

    private fun Record(input: String, multiplier: Int = 0): Record {
        val (row, countString) = input.split(" ")
        val workingOrder = countString.trim().split(",").map { it.toInt() }
        return if (multiplier > 0) {
            val rowsList = buildList {
                repeat(multiplier) {
                    add(row)
                }
            }
            Record(rowsList.joinToString("?"), buildList { repeat(multiplier) { addAll(workingOrder) } })
        } else {
            Record(row, workingOrder)
        }
    }

    override fun part2(input: List<String>): Long {
        val springRecords = input.map { Record(it, 5) }
        val results = springRecords.map {
            guessArrangements(it.row, it.workingOrder, false)
        }
        return results.sum()
    }
}
