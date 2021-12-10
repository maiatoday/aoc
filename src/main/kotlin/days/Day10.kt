// Day object template
object Day10 {

    private val allBracketsOpen = listOf('(', '[', '{', '<')
    private val allBracketsClosed = listOf(')', ']', '}', '>')

    private val matchingMap = mapOf(
        '(' to ')',
        '[' to ']',
        '{' to '}',
        '<' to '>',
    )

    private val illegalScores = mapOf(
        ')' to 3,
        ']' to 57,
        '}' to 1197,
        '>' to 25137
    )

    fun part1(input: List<String>): Int {
        val illegalCharacters = input.map { check(it) }.filter { it != ' ' }
        return illegalCharacters.mapNotNull { illegalScores[it] }.sum()
    }

    private fun check(chunk: String): Char {
        val bracketStack: MutableList<Char> = emptyList<Char>().toMutableList()
        for (b in chunk) {
            if (allBracketsOpen.contains(b)) {
                bracketStack.add(b)
            } else if (bracketStack.isEmpty() and allBracketsClosed.contains(b)) {
                return ' '
            } else if (matchClosing(b, bracketStack.last())) {
                bracketStack.removeLast()
            } else {
                // not and opening and not a matching close so must be a non matching close
                if (allBracketsClosed.contains(b)) return b
            }
        }
        return ' '
    }

    private fun matchClosing(b: Char, last: Char): Boolean =
        (b == matchingMap[last])

    private val completeScores = mapOf(
        ')' to 1,
        ']' to 2,
        '}' to 3,
        '>' to 4
    )

    fun part2(input: List<String>): Long {
        val autoComplete = input.mapNotNull { complete(it) }
        val scores = autoComplete.map { it.fold(0L) { total, bracket -> total * 5 + (completeScores[bracket] ?: 0) } }.sorted()
        return scores[scores.size/2]
    }

    private fun complete(chunk: String): List<Char>? {
        val bracketStack: MutableList<Char> = emptyList<Char>().toMutableList()
        for (b in chunk) {
            if (allBracketsOpen.contains(b)) {
                bracketStack.add(b)
            } else if (bracketStack.isEmpty() and allBracketsClosed.contains(b)) {
                return null
            } else if (matchClosing(b, bracketStack.last())) {
                bracketStack.removeLast()
            } else {
                // not and opening and not a matching close so must be a non matching close
                if (allBracketsClosed.contains(b)) return null
            }
        }
        return bracketStack.mapNotNull { matchingMap[it] }.reversed()
    }
}