package days

typealias SchematicRow = List<Element>

object Day03 : Day<Long, List<String>> {
    override val number: Int = 1
    override val expectedPart1Test: Long = 4361L
    override val expectedPart2Test: Long = 467835L
    override var useTestData = true
    override val debug = true

    override fun part1(input: List<String>): Long {
        val engineSchematic: List<SchematicRow> = input.mapIndexed { i, s -> extractElements(s, i) }
        return engineSchematic
                .findParts()
                .sumOf { it.value }
                .toLong()
    }

    override fun part2(input: List<String>): Long {
        val engineSchematic: List<SchematicRow> = input.mapIndexed { i, s -> extractElements(s, i) }
        return engineSchematic
                .flatten()
                .findGearParts()
                .sumOf { parts -> parts.first * parts.second }
                .toLong()
    }

    private fun extractElements(input: String, row: Int): List<Element> = buildList {
        var numberStart = -1
        var currentNumber = ""
        for ((index, c) in input.withIndex()) {
            when {
                (c.isDigit()) -> {
                    currentNumber += c
                    if (numberStart == -1) numberStart = index
                }

                else -> {
                    if (c != '.') this.add(Symbol(value = c, column = index, row = row))
                    if (currentNumber.isNotEmpty()) {
                        this.add(Number(number = currentNumber, start = numberStart, end = index - 1, row = row))
                        currentNumber = ""
                        numberStart = -1
                    }
                }
            }
        }
        if (currentNumber.isNotEmpty()) {
            this.add(Number(number = currentNumber, start = numberStart, end = input.length - 1, row = row))
        }
    }

    private fun List<SchematicRow>.findParts(): Set<Number> {
        val parts = mutableSetOf<Number>()
        this.windowed(2).map { twoRows ->
            val symbols: List<Symbol> = twoRows.flatten().filterIsInstance<Symbol>()
            val numbers: List<Number> = twoRows.flatten().filterIsInstance<Number>()
            numbers.filter { n ->
                symbols.any { s ->
                    s.column in n.expandedColumn
                }
            }.forEach { parts.add(it) }
        }
        return parts
    }
}

private fun List<Element>.findGearParts(): List<Pair<Int, Int>> {
    val parts = this.filterIsInstance<Number>()
    val potentialGears = this.filterIsInstance<Symbol>().filter { it.value == '*' }
    return potentialGears
            .map { s ->
                // list of neighbours
                parts.filter { (s.row in it.expandedRow) && (s.column in it.expandedColumn) }
            }
            .filter {
                it.size == 2
            }
            .map {
                it[0].value to it[1].value
            }
}

//region Data classes
sealed class Element
data class Number(val value: Int, val xRange: IntRange, val row: Int) : Element() {
    val expandedColumn = xRange.first - 1..xRange.last + 1
    val expandedRow = row - 1..row + 1
}

fun Number(number: String, start: Int, end: Int, row: Int): Number = Number(number.toInt(), start..end, row)

data class Symbol(val value: Char, val column: Int, val row: Int) : Element()
//endregion

